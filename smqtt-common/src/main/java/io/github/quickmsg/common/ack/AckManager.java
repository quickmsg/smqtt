package io.github.quickmsg.common.ack;

/**
 * @author luxurong
 */
public interface AckManager {

    void addAck(Ack ack);

    Ack getAck(Long id);

    void deleteAck(Long id);



}
