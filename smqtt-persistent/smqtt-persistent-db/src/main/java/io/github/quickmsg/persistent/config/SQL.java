package io.github.quickmsg.persistent.config;

/**
 *  sql语句
 *
 * @author zhaopeng
 * @date 2021/06/04
 */
public interface SQL {
    /**
     * 创建mqtt消息历史记录表sql
     */
    String MQTT_MSG_LOG = "CREATE TABLE IF NOT EXISTS `mqtt_msg_log` (" +
            "  `message_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '消息ID'," +
            "  `clientId` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户端ID'," +
            "  `topic` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '话题'," +
            "  `qos` int(10) DEFAULT NULL COMMENT '消息质量'," +
            "  `retain` int(10) DEFAULT NULL COMMENT '保留标志'," +
            "  `payload` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '消息内容'," +
            "  `create_time` datetime DEFAULT NULL COMMENT '记录保存时间'" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;";

}
