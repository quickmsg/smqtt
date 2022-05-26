package io.github.quickmsg.common.retry;

/**
 * @author luxurong
 */
public interface AckManager {

    void addAck(Ack ack);

    Ack getAck(Long id);

    void deleteAck(Long id);



}
