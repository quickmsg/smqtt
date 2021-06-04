package io.github.quickmsg.persistent.registry;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.environment.EnvContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.spi.DynamicLoader;
import io.github.quickmsg.persistent.DbConnection;
import io.github.quickmsg.persistent.config.DruidConnection;
import io.github.quickmsg.persistent.config.SQL;
import io.github.quickmsg.persistent.tables.Tables;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class DbMessageRegistry implements MessageRegistry {

    DbConnection dbConnection = DynamicLoader.findFirst(DbConnection.class).orElse(null);

    public static final int INITIALSIZE_DB = 10;
    public static final int MAXACTIVE_DB = 300;
    public static final int MAXWAIT_DB = 60000;
    public static final int MINIDLE_DB = 2;

    @Override
    public void startUp(EnvContext envContext) {
        Map<String, String> environments = envContext.getEnvironments();
        // 数据库驱动
        String driverClassName = Optional.ofNullable(environments.get("mysql.driverClassName")).orElse(null);
        // 数据库连接
        String url = Optional.ofNullable(environments.get("mysql.url")).orElse(null);
        // 数据库用户名
        String username = Optional.ofNullable(environments.get("mysql.username")).orElse(null);
        // 数据库密码
        String password = Optional.ofNullable(environments.get("mysql.password")).orElse("");
        // 连接池初始化连接数
        String initialSize = Optional.ofNullable(environments.get("mysql.initialSize")).orElse(String.valueOf(INITIALSIZE_DB));
        // 连接池中最多支持多少个活动会话
        String maxActive = Optional.ofNullable(environments.get("mysql.maxActive")).orElse(String.valueOf(MAXACTIVE_DB));
        // 程序向连接池中请求连接时,超过maxWait的值后，认为本次请求失败，即连接池
        String maxWait = Optional.ofNullable(environments.get("mysql.maxWait")).orElse(String.valueOf(MAXWAIT_DB));
        // 回收空闲连接时，将保证至少有minIdle个连接.
        String minIdle = Optional.ofNullable(environments.get("mysql.minIdle")).orElse(String.valueOf(MINIDLE_DB));

        Properties properties = new Properties();
        properties.put("driverClassName", driverClassName);
        properties.put("url", url);
        properties.put("username", username);
        properties.put("password", password);
        properties.put("initialSize", initialSize);
        properties.put("maxActive", maxActive);
        properties.put("maxWait", maxWait);
        properties.put("minIdle", minIdle);
        // 初始化连接池
        DruidConnection.getInstace().initDatasource(properties);

        if (dbConnection != null) {
            try (Connection connection = dbConnection.getConnection()) {
                DSLContext dslContext = dbConnection.getDSLContext(connection);
                dslContext.execute(SQL.MQTT_MSG_LOG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.error("not found DbConnection implementation class");
        }
    }

    @Override
    public Optional<List<MqttPublishMessage>> getSessionMessages(String clientIdentifier) {
        return Optional.empty();
    }

    @Override
    public void sendSessionMessages(String clientIdentifier, MqttPublishMessage messages) {

    }

    @Override
    public void saveRetainMessage(String topic, MqttPublishMessage messages) {
        // TODO 修改MqttPublishMessage类型
        if (dbConnection != null) {
            try (Connection connection = dbConnection.getConnection()) {
                DSLContext dslContext = dbConnection.getDSLContext(connection);
                String payload = messages.payload().toString(CharsetUtil.UTF_8);
                dslContext.insertInto(Tables.MQTT_MSG_LOG)
                        .columns(Tables.MQTT_MSG_LOG.MESSAGE_ID,
                                Tables.MQTT_MSG_LOG.CLIENTID,
                                Tables.MQTT_MSG_LOG.TOPIC,
                                Tables.MQTT_MSG_LOG.QOS,
                                Tables.MQTT_MSG_LOG.RETAIN,
                                Tables.MQTT_MSG_LOG.PAYLOAD,
                                Tables.MQTT_MSG_LOG.CREATE_TIME)
                        .values("", "", topic, 1, 1, payload, LocalDateTime.now())
                        .execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.error("not found DbConnection implementation class");
        }
    }

    @Override
    public Optional<List<MqttPublishMessage>> getRetainMessage(String s, MqttChannel mqttChannel) {
        return Optional.empty();
    }


}
