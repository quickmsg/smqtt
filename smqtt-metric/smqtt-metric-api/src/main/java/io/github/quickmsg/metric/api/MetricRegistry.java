package io.github.quickmsg.metric.api;

/**
 * @author luxurong
 */
public interface MetricRegistry {


    WholeCounter getTopicCounter();


    WholeCounter getConnectCounter();


    WindowCounter getReadCounter();


    WindowCounter getWriteCounter();


}
