package io.github.quickmsg.common.ack;

import java.util.function.Consumer;

/**
 * @author luxurong
 */

public class RetryAck extends AbsAck {

    private final long id;


    public RetryAck(long id, int maxRetrySize, int period, Runnable runnable, AckManager ackManager, Consumer<Boolean> consumer) {
        super(maxRetrySize, period, runnable, ackManager,consumer);
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
