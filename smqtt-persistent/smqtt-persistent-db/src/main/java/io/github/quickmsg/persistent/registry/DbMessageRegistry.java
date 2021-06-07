package io.github.quickmsg.persistent.registry;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.environment.EnvContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.persistent.DbConnectionHolder;
import io.github.quickmsg.persistent.config.DruidConnectionProvider;
import io.github.quickmsg.persistent.sql.SqlLoader;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class DbMessageRegistry implements MessageRegistry {

    private static final Integer INIT_SIZE_DB = 10;

    private static final Integer MAX_ACTIVE_DB = 300;

    private static final Integer MAX_WAIT_DB = 60000;

    private static final Integer MIN_IDLE_DB = 2;

    @Override
    public void startUp(EnvContext envContext) {
        Properties properties = new Properties();
        properties.putAll(envContext.getEnvironments());
        DruidConnectionProvider
                .singleTon()
                .init(properties)
                .then(Mono.when(SqlLoader.loadSql().stream().map(sql ->
                        DbConnectionHolder.getDslContext().doOnNext(dslContext -> dslContext.execute(sql))
                ).collect(Collectors.toList())))
                .subscribe();
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
//        if (dbConnection != null) {
//            try (Connection connection = dbConnection.getConnection()) {
//                DSLContext dslContext = dbConnection.getDSLContext(connection);
//                String payload = messages.payload().toString(CharsetUtil.UTF_8);
//                dslContext.insertInto(Tables.MQTT_MSG_LOG)
//                        .columns(Tables.MQTT_MSG_LOG.MESSAGE_ID,
//                                Tables.MQTT_MSG_LOG.CLIENTID,
//                                Tables.MQTT_MSG_LOG.TOPIC,
//                                Tables.MQTT_MSG_LOG.QOS,
//                                Tables.MQTT_MSG_LOG.RETAIN,
//                                Tables.MQTT_MSG_LOG.PAYLOAD,
//                                Tables.MQTT_MSG_LOG.CREATE_TIME)
//                        .values("", "", topic, 1, 1, payload, LocalDateTime.now())
//                        .execute();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            log.error("not found DbConnection implementation class");
//        }
    }

    @Override
    public Optional<List<MqttPublishMessage>> getRetainMessage(String s, MqttChannel mqttChannel) {
        return Optional.empty();
    }


}
