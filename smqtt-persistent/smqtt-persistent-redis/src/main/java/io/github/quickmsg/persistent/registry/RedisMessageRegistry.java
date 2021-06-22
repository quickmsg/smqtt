package io.github.quickmsg.persistent.registry;

import io.github.quickmsg.common.bootstrap.BootstrapKey;
import io.github.quickmsg.common.environment.EnvContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.RetainMessage;
import io.github.quickmsg.common.message.SessionMessage;
import io.github.quickmsg.persistent.factory.ClientFactory;
import io.github.quickmsg.persistent.message.RetainMessageEntity;
import io.github.quickmsg.persistent.message.SessionMessageEntity;
import io.github.quickmsg.persistent.strategy.ClientStrategy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.util.*;
import java.util.stream.Collectors;


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
    public void startUp(EnvContext envContext) {
        Map<String, String> environments = envContext.getEnvironments();
        // 获取客户端策略
        ClientStrategy clientStrategy = ClientFactory.getClientStrategy(environments.get(BootstrapKey.Redis.REDIS_MODE));
        // 获取redisson客户端
        redissonClient = clientStrategy.getRedissonClient(environments);

        redissonClient.shutdown();
    }

    @Override
    public List<SessionMessage> getSessionMessage(String clientIdentifier) {
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
    }

    @Override
    public void saveSessionMessage(SessionMessage sessionMessage) {
        String topic = sessionMessage.getTopic();
        String clientId = sessionMessage.getClientIdentifier();
        int qos = sessionMessage.getQos();
        boolean retain = sessionMessage.isRetain();
        byte[] body = sessionMessage.getBody();

        SessionMessageEntity sessionMessageEntity = SessionMessageEntity.builder()
                .topic(topic)
                .clientId(clientId)
                .qos(qos)
                .body(body)
                .retain(retain)
                .createTime(new Date()).build();

        RList<SessionMessageEntity> list = redissonClient.getList(BootstrapKey.Redis.REDIS_SESSION_MESSAGE_PREFIX_KEY + clientId);
        list.add(sessionMessageEntity);
    }

    @Override
    public void saveRetainMessage(RetainMessage retainMessage) {
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
    }

    @Override
    public List<RetainMessage> getRetainMessage(String topic) {
        RBucket<RetainMessageEntity> bucket = redissonClient.getBucket(BootstrapKey.Redis.REDIS_RETAIN_MESSAGE_PREFIX_KEY + topic);
        RetainMessageEntity retainMessageEntity = bucket.get();

        RetainMessage retainMessage = Optional.ofNullable(retainMessageEntity).map(item ->
                RetainMessage.builder()
                        .topic(item.getTopic())
                        .qos(item.getQos())
                        .body(item.getBody())
                        .build()
        ).orElse(null);

        if (retainMessage != null) {
            List<RetainMessage> list = new ArrayList<>();
            list.add(retainMessage);
            return list;
        }
        return null;
    }

}
