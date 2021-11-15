package io.github.quickmsg.common.metric;

public interface MetircConstant {
    String COMMON_TAG_NAME = "application";
    String COMMON_TAG_VALUE = "smqtt";

    String CONNECT_COUNTER_NAME = "smqtt.connect.count";
    String TOPIC_COUNTER_NAME = "smqtt.topic.count";

    String SYSTEM_CPU_COUNT = "system.cpu.count";
    String SYSTEM_CPU_USAGE = "system.cpu.usage";
    String PROCESS_CPU_USAGE = "process.cpu.usage";

    String REACTOR_NETTY_TCP_SERVER_DATA_RECEIVED ="reactor.netty.tcp.server.data.received";
    String REACTOR_NETTY_TCP_SERVER_DATA_SENT ="reactor.netty.tcp.server.data.sent";



}
