package io.github.quickmsg.persistent.registry;

import io.github.quickmsg.common.bootstrap.BootstrapKey;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.environment.EnvContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.RetainMessage;
import io.github.quickmsg.common.message.SessionMessage;
import io.github.quickmsg.common.utils.TopicRegexUtils;
import io.github.quickmsg.persistent.factory.ClientFactory;
import io.github.quickmsg.persistent.message.RetainMessageEntity;
import io.github.quickmsg.persistent.message.SessionMessageEntity;
import io.github.quickmsg.persistent.strategy.ClientStrategy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * 消息
 *
 * @author zhaopeng
 */
@Slf4j
public class RedisMessageRegistry implements MessageRegistry {

    /**
     * redisson客户端
     */
    private RedissonClient redissonClient = null;

    @Override
    public void startUp(BootstrapConfig bootstrapConfig) {
        try {
            BootstrapConfig.RedisConfig redisConfig = bootstrapConfig.getSmqttConfig().getRedisConfig();
            // 获取客户端策略
            ClientStrategy clientStrategy = ClientFactory.getClientStrategy(redisConfig.getMode());
            // 获取redisson客户端
            redissonClient = clientStrategy.getRedissonClient(redisConfig);
        } catch (Exception e) {
            log.error("startUp error message", e);
        }
    }

    @Override
    public List<SessionMessage> getSessionMessage(String clientIdentifier) {
        try {
            RList<SessionMessageEntity> list = redissonClient.getList(BootstrapKey.Redis.REDIS_SESSION_MESSAGE_PREFIX_KEY + clientIdentifier);
            List<SessionMessage> resList = list.stream().map(record -> SessionMessage.builder()
                    .topic(record.getTopic())
                    .clientIdentifier(record.getClientId())
                    .qos(record.getQos())
                    .retain(record.getRetain())
                    .body(record.getBody())
                    .build()
            ).collect(Collectors.toList());

            if (list.size() > 0) {
                redissonClient.getBucket(BootstrapKey.Redis.REDIS_SESSION_MESSAGE_PREFIX_KEY + clientIdentifier).delete();
            }
            return resList;
        } catch (Exception e) {
            log.error("getSessionMessage error clientIdentifier:{}", clientIdentifier, e);
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

        try {
            SessionMessageEntity sessionMessageEntity = SessionMessageEntity.builder()
                    .topic(topic)
                    .clientId(clientIdentifier)
                    .qos(qos)
                    .body(body)
                    .retain(retain)
                    .createTime(new Date()).build();

            RList<SessionMessageEntity> list = redissonClient.getList(BootstrapKey.Redis.REDIS_SESSION_MESSAGE_PREFIX_KEY + clientIdentifier);
            list.add(sessionMessageEntity);
        } catch (Exception e) {
            log.error("saveSessionMessage error message: {}", clientIdentifier, e);
        }
    }

    @Override
    public void saveRetainMessage(RetainMessage retainMessage) {
        try {
            String topic = retainMessage.getTopic();
            int qos = retainMessage.getQos();

            if (retainMessage.getBody() == null || retainMessage.getBody().length == 0) {
                redissonClient.getBucket(BootstrapKey.Redis.REDIS_RETAIN_MESSAGE_PREFIX_KEY + topic).delete();
            } else {
                Date date = new Date();
                RetainMessageEntity retainMessageEntity = RetainMessageEntity.builder()
                        .topic(topic)
                        .qos(qos)
                        .body(retainMessage.getBody())
                        .createTime(date)
                        .updateTime(date).build();

                RBucket<RetainMessageEntity> bucket = redissonClient.getBucket(BootstrapKey.Redis.REDIS_RETAIN_MESSAGE_PREFIX_KEY + topic);
                bucket.set(retainMessageEntity);
            }
        } catch (Exception e) {
            log.error("saveRetainMessage error message: {}", retainMessage, e);
        }
    }

    @Override
    public List<RetainMessage> getRetainMessage(String topic) {
        try {
            RKeys keys = redissonClient.getKeys();
            Iterable<String> foundedKeys = keys.getKeysByPattern(BootstrapKey.Redis.REDIS_RETAIN_MESSAGE_PREFIX_KEY + "*");
            List<RetainMessage> list = StreamSupport.stream(foundedKeys.spliterator(), false)
                    .map(tp -> tp.replaceAll(BootstrapKey.Redis.REDIS_RETAIN_MESSAGE_PREFIX_KEY, ""))
                    .filter(tp -> tp.matches(TopicRegexUtils.regexTopic(topic)))
                    .map(tp -> {
                        RBucket<RetainMessageEntity> bucket = redissonClient.getBucket(BootstrapKey.Redis.REDIS_RETAIN_MESSAGE_PREFIX_KEY + tp);
                        RetainMessageEntity retainMessageEntity = bucket.get();
                        return Optional.ofNullable(retainMessageEntity).map(item ->
                                RetainMessage.builder()
                                        .topic(item.getTopic())
                                        .qos(item.getQos())
                                        .body(item.getBody())
                                        .build()
                        ).orElse(null);
                    })
                    .collect(Collectors.toList());
            return list;
        } catch (Exception e) {
            log.error("getRetainMessage error topic: {}", topic, e);
        }
        return Collections.emptyList();
    }

}
