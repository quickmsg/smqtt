package io.github.quickmsg.common.ack;

import io.netty.util.HashedWheelTimer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author luxurong
 */
public class TimeAckManager extends HashedWheelTimer implements AckManager {

    private final Map<Long, Ack> ackMap = new ConcurrentHashMap<>();

    public TimeAckManager(long tickDuration, TimeUnit unit, int ticksPerWheel) {
        super( tickDuration, unit, ticksPerWheel);
    }

    @Override
    public void addAck(Ack ack) {
        ackMap.put(ack.getId(),ack);
        this.newTimeout(ack,ack.getTimed(),ack.getTimeUnit());
    }

    @Override
    public Ack getAck(Long id) {
        return ackMap.get(id);
    }

    @Override
    public void deleteAck(Long id) {
        ackMap.remove(id);
    }

}
