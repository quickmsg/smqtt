package io.github.quickmsg.common.metric;


/**
 * @author luxurong
 */
public interface MetricRegistry {


    WholeCounter getTopicCounter();


    WholeCounter getConnectCounter();


    WindowCounter getReadCounter();


    WindowCounter getWriteCounter();


}
