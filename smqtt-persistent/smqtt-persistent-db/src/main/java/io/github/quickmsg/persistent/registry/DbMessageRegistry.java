package io.github.quickmsg.persistent.registry;

import io.github.quickmsg.common.bootstrap.BootstrapKey;
import io.github.quickmsg.common.environment.EnvContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.RetainMessage;
import io.github.quickmsg.common.message.SessionMessage;
import io.github.quickmsg.persistent.config.DruidConnectionProvider;
import io.github.quickmsg.persistent.tables.Tables;
import io.github.quickmsg.persistent.tables.tables.records.SmqttRetainRecord;
import io.netty.util.CharsetUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class DbMessageRegistry implements MessageRegistry {

    @Override
    public void startUp(EnvContext envContext) {
        Map<String, String> environments = envContext.getEnvironments();

        Properties properties = new Properties();
        for (String key : environments.keySet()) {
            // 过滤以db.开头的数据库参数配置
            if (key.startsWith(BootstrapKey.DB_PREFIX)) {
                properties.put(key.replaceAll(BootstrapKey.DB_PREFIX, ""), environments.get(key));
            }
        }

        DruidConnectionProvider
                .singleTon()
                .init(properties);

        ClassLoaderResourceAccessor classLoaderResourceAccessor = new ClassLoaderResourceAccessor(this.getClass().getClassLoader());
        try (Connection connection = DruidConnectionProvider.singleTon().getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("classpath:liquibase/smqtt_db.xml", classLoaderResourceAccessor, database);
            liquibase.update("smqtt_db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SessionMessage> getSessionMessages(String clientIdentifier) {
        return null;
    }

    @Override
    public void sendSessionMessages(SessionMessage sessionMessage) {
        String topic = sessionMessage.getTopic();
        String clientIdentifier = sessionMessage.getClientIdentifier();
        int qos = sessionMessage.getQos();
        byte[] body = sessionMessage.getBody();

        try (Connection connection = DruidConnectionProvider.singleTon().getConnection()) {
            DSLContext dslContext = DSL.using(connection);
            String bodyMsg = new String(body, CharsetUtil.UTF_8);
            dslContext.insertInto(Tables.SMQTT_SESSION)
                    .columns(Tables.SMQTT_SESSION.TOPIC,
                            Tables.SMQTT_SESSION.CLIENT_ID,
                            Tables.SMQTT_SESSION.QOS,
                            Tables.SMQTT_SESSION.BODY,
                            Tables.SMQTT_SESSION.CREATE_TIME)
                    .values(topic, clientIdentifier, qos, bodyMsg, LocalDateTime.now())
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveRetainMessage(RetainMessage retainMessage) {
        String topic = retainMessage.getTopic();
        int qos = retainMessage.getQos();

        try (Connection connection = DruidConnectionProvider.singleTon().getConnection()) {
            DSLContext dslContext = DSL.using(connection);
            if (retainMessage.getBody() == null || retainMessage.getBody().length <= 0) {
                // 消息为空, 删除话题
                dslContext.deleteFrom(Tables.SMQTT_RETAIN).where(Tables.SMQTT_RETAIN.TOPIC.eq(topic)).execute();
            }else{
                Record1<Integer> integerRecord1 = dslContext.selectCount().from(Tables.SMQTT_RETAIN).where(Tables.SMQTT_RETAIN.TOPIC.eq(topic)).fetchOne();
                if (integerRecord1.value1() > 0) {
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
            e.printStackTrace();
        }
    }

    @Override
    public List<RetainMessage> getRetainMessage(String topic) {
        return null;
    }
}
