package io.github.quickmsg.persistent.registry;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.RetainMessage;
import io.github.quickmsg.common.message.SessionMessage;
import io.github.quickmsg.common.utils.TopicRegexUtils;
import io.github.quickmsg.persistent.config.HikariCPConnectionProvider;
import io.github.quickmsg.persistent.tables.Tables;
import io.netty.util.CharsetUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
@Slf4j
public class DbMessageRegistry implements MessageRegistry {

    private static final String DEFAULT_DATABASE_NAME = "smqtt_db";

    private static final String DEFAULT_LIQUIBASE_PATH = "classpath:liquibase/smqtt_db.xml";

    public final static String DB_PREFIX = "db.";

    @Override
    public void startUp(Map<Object, Object> environmentMap) {
        BootstrapConfig.DatabaseConfig dbConfig = (BootstrapConfig.DatabaseConfig) environmentMap.get(BootstrapConfig.DatabaseConfig.class);
        Properties properties = new Properties();
        properties.put("jdbcUrl", dbConfig.getJdbcUrl());
        properties.put("username", dbConfig.getUsername());
        properties.put("password", dbConfig.getPassword());
        properties.put("dataSource.cachePrepStmts", dbConfig.getDataSourceCachePrepStmts());
        properties.put("dataSource.prepStmtCacheSize", dbConfig.getDataSourcePrepStmtCacheSize());
        properties.put("dataSource.prepStmtCacheSqlLimit", dbConfig.getDataSourcePrepStmtCacheSqlLimit());
        properties.put("dataSource.useServerPrepStmts", dbConfig.getDataSourceUseServerPrepStmts());
        properties.put("dataSource.useLocalSessionState", dbConfig.getDataSourceUseLocalSessionState());
        properties.put("dataSource.rewriteBatchedStatements", dbConfig.getDataSourceRewriteBatchedStatements());
        properties.put("dataSource.cacheResultSetMetadata", dbConfig.getDataSourceCacheResultSetMetadata());
        properties.put("dataSource.cacheServerConfiguration", dbConfig.getDataSourceCacheServerConfiguration());
        properties.put("dataSource.elideSetAutoCommits", dbConfig.getDataSourceElideSetAutoCommits());
        properties.put("dataSource.maintainTimeStats", dbConfig.getDataSourceMaintainTimeStats());

        // to add more
        HikariCPConnectionProvider
                .singleTon()
                .init(properties);
        ClassLoaderResourceAccessor classLoaderResourceAccessor = new ClassLoaderResourceAccessor(this.getClass().getClassLoader());
        try (Connection connection = HikariCPConnectionProvider.singleTon().getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(DEFAULT_LIQUIBASE_PATH, classLoaderResourceAccessor, database);
            liquibase.update(DEFAULT_DATABASE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SessionMessage> getSessionMessage(String clientIdentifier) {
        try (Connection connection = HikariCPConnectionProvider.singleTon().getConnection()) {
            DSLContext dslContext = DSL.using(connection);

            List<SessionMessage> list = dslContext
                    .selectFrom(Tables.SMQTT_SESSION)
                    .where(Tables.SMQTT_SESSION.CLIENT_ID.eq(clientIdentifier))
                    .fetch()
                    .stream()
                    .map(record ->
                            SessionMessage.builder()
                                    .qos(record.getQos())
                                    .topic(record.getTopic())
                                    .body(record.getBody().getBytes())
                                    .clientIdentifier(record.getClientId())
                                    .retain(record.getRetain())
                                    .build())
                    .collect(Collectors.toList());

            if (list.size() > 0) {
                // 删除记录
                dslContext.deleteFrom(Tables.SMQTT_SESSION)
                        .where(Tables.SMQTT_SESSION.CLIENT_ID.eq(clientIdentifier))
                        .execute();
            }
            return list;
        } catch (Exception e) {
            log.error("getSessionMessages error clientIdentifier:{}", clientIdentifier, e);
            return Collections.emptyList();
        }
    }

    @Override
    public void saveSessionMessage(SessionMessage sessionMessage) {
        String topic = sessionMessage.getTopic();
        String clientIdentifier = sessionMessage.getClientIdentifier();
        int qos = sessionMessage.getQos();
        boolean retain = sessionMessage.isRetain();
        byte[] body = sessionMessage.getBody();

        try (Connection connection = HikariCPConnectionProvider.singleTon().getConnection()) {
            DSLContext dslContext = DSL.using(connection);
            String bodyMsg = new String(body, CharsetUtil.UTF_8);
            dslContext.insertInto(Tables.SMQTT_SESSION)
                    .columns(Tables.SMQTT_SESSION.TOPIC,
                            Tables.SMQTT_SESSION.CLIENT_ID,
                            Tables.SMQTT_SESSION.QOS,
                            Tables.SMQTT_SESSION.RETAIN,
                            Tables.SMQTT_SESSION.BODY,
                            Tables.SMQTT_SESSION.CREATE_TIME)
                    .values(topic, clientIdentifier, qos, retain, bodyMsg, LocalDateTime.now())
                    .execute();
        } catch (Exception e) {
            log.error("sendSessionMessages error message: {}", clientIdentifier, e);
        }
    }

    @Override
    public void saveRetainMessage(RetainMessage retainMessage) {
        String topic = retainMessage.getTopic();
        int qos = retainMessage.getQos();

        try (Connection connection = HikariCPConnectionProvider.singleTon().getConnection()) {
            DSLContext dslContext = DSL.using(connection);
            if (retainMessage.getBody() == null || retainMessage.getBody().length == 0) {
                // 消息为空, 删除话题
                dslContext.deleteFrom(Tables.SMQTT_RETAIN).where(Tables.SMQTT_RETAIN.TOPIC.eq(topic)).execute();
            } else {
                Record1<Integer> integerRecord1 = dslContext.selectCount()
                        .from(Tables.SMQTT_RETAIN)
                        .where(Tables.SMQTT_RETAIN.TOPIC.eq(topic))
                        .fetchAny();
                if (integerRecord1 != null && integerRecord1.value1() != null && integerRecord1.value1() > 0) {
                    // 更新记录
                    String bodyMsg = new String(retainMessage.getBody(), CharsetUtil.UTF_8);
                    dslContext.update(Tables.SMQTT_RETAIN)
                            .set(Tables.SMQTT_RETAIN.QOS, qos)
                            .set(Tables.SMQTT_RETAIN.BODY, bodyMsg)
                            .set(Tables.SMQTT_RETAIN.UPDATE_TIME, LocalDateTime.now())
                            .where(Tables.SMQTT_RETAIN.TOPIC.eq(topic))
                            .execute();
                } else {
                    // 新增记录
                    String bodyMsg = new String(retainMessage.getBody(), CharsetUtil.UTF_8);
                    dslContext.insertInto(Tables.SMQTT_RETAIN)
                            .columns(Tables.SMQTT_RETAIN.TOPIC,
                                    Tables.SMQTT_RETAIN.QOS,
                                    Tables.SMQTT_RETAIN.BODY,
                                    Tables.SMQTT_RETAIN.CREATE_TIME)
                            .values(topic, qos, bodyMsg, LocalDateTime.now())
                            .execute();
                }
            }
        } catch (Exception e) {
            log.error("saveRetainMessage error message: {}", retainMessage, e);
        }
    }

    @Override
    public List<RetainMessage> getRetainMessage(String topic) {
        try (Connection connection = HikariCPConnectionProvider.singleTon().getConnection()) {
            DSLContext dslContext = DSL.using(connection);
            return dslContext
                    .selectFrom(Tables.SMQTT_RETAIN)
                    .fetch()
                    .stream()
                    .filter(record -> record.getTopic().matches(TopicRegexUtils.regexTopic(topic)))
                    .map(record -> RetainMessage.builder()
                            .topic(record.getTopic())
                            .qos(record.getQos())
                            .body(getBody(record.getBody()))
                            .build()
                    )
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("getRetainMessage error  topic: {}", topic, e);
            return Collections.emptyList();

        }
    }

    public byte[] getBody(String body) {
        return StringUtils.isBlank(body) ? null : body.getBytes(CharsetUtil.UTF_8);
    }

}
