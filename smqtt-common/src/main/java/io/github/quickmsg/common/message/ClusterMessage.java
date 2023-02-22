package io.github.quickmsg.common.message;

import lombok.Data;

/**
 * @author luxurong
 */
@Data
public class ClusterMessage {

    private ClusterEvent clusterEvent;

    private Object message;

    public static enum ClusterEvent{

        PUBLISH,

        CLOSE

    }

    public ClusterMessage(HeapMqttMessage heapMqttMessage){
        this.clusterEvent = ClusterEvent.PUBLISH;
        this.message= heapMqttMessage;
    }

    public ClusterMessage(CloseMqttMessage closeMqttMessage){
        this.clusterEvent = ClusterEvent.CLOSE;
        this.message= closeMqttMessage;
    }

}
