package io.github.quickmsg.persistent.factory;

import io.github.quickmsg.persistent.strategy.ClientStrategy;
import io.github.quickmsg.persistent.strategy.ClusterClientStrategy;
import io.github.quickmsg.persistent.strategy.SentinelClientStrategy;
import io.github.quickmsg.persistent.strategy.SingleClientStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 客戶端公長類
 *
 * @author zhaopeng
 */
public class ClientFactory {

    public static Map<String, ClientStrategy> strategyMap = new HashMap<>();

    static {
        strategyMap.put("single", new SingleClientStrategy());
        strategyMap.put("sentinel", new SentinelClientStrategy());
        strategyMap.put("cluster", new ClusterClientStrategy());
    }

    /**
     * 获取客户端策略
     *
     * @param key
     * @return {@link ClientStrategy}
     */
    public static ClientStrategy getClientStrategy(String key) {
        return strategyMap.get(key);
    }
}
