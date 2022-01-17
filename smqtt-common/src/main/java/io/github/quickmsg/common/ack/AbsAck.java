package io.github.quickmsg.common.ack;

import io.netty.util.Timeout;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author luxurong
 */
@Slf4j(topic = "ack")
public abstract class AbsAck implements Ack {

    private final int maxRetrySize;

    private int count = 1;

    private volatile boolean died = false;

    private final Runnable runnable;

    private final AckManager ackManager;

    private final int period;


    private final Runnable cleaner;



    protected AbsAck(int maxRetrySize, int period, Runnable runnable, AckManager ackManager, Runnable cleaner) {
        this.maxRetrySize = maxRetrySize;
        this.period = period;
        this.runnable = runnable;
        this.ackManager = ackManager;
        this.cleaner= cleaner;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        if (++count <= maxRetrySize+1 && !died ) {
            try {
                log.info("task retry send ...........");
                runnable.run();
                ackManager.addAck(this);
            } catch (Exception e) {
                log.error("Ack error ", e);
            }

        }
        else {
            cleaner.run();
        }
    }

    @Override
    public void stop() {
        died = true;
        log.info("retry task  stop ...........");
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
