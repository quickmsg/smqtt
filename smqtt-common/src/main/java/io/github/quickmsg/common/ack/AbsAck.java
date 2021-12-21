package io.github.quickmsg.common.ack;

import io.netty.util.Timeout;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author luxurong
 */
@Slf4j
public abstract class AbsAck implements Ack {

    private final int maxRetrySize;

    private int count = 1;

    private volatile boolean died = false;

    private final Runnable runnable;

    private final AckManager ackManager;

    private final int period;

    protected AbsAck(int maxRetrySize, int period, Runnable runnable, AckManager ackManager) {
        this.maxRetrySize = maxRetrySize;
        this.period = period;
        this.runnable = runnable;
        this.ackManager = ackManager;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        if (++count <= maxRetrySize+1 && !died ) {
            try {
                runnable.run();
                ackManager.addAck(this);
            } catch (Exception e) {
                log.error("Ack error ", e);
            }

        }
    }

    @Override
    public void stop() {
        died = true;
        ackManager.deleteAck(getId());
    }


    @Override
    public void start() {
        this.ackManager.addAck(this);
    }

    @Override
    public int getTimed() {
        return this.period * this.count;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }
}
