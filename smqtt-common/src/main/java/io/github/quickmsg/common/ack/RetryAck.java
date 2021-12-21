package io.github.quickmsg.common.ack;

/**
 * @author luxurong
 */

public class RetryAck extends AbsAck {

    private final long id;

    public RetryAck(long id, int maxRetrySize, int period, Runnable runnable, AckManager ackManager) {
        super(maxRetrySize, period, runnable, ackManager);
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
