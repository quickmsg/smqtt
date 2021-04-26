package com.github.quickmsg.common.utils;

/**
 * @author luxurong
 * @date 2021/4/7 17:11
 * @description
 */
public class TopicRegexUtils {

    public static String regexTopic(String topic) {
        if (topic.startsWith("$")) {
            topic = "\\" + topic;
        }
        return topic
                .replaceAll("/", "\\\\/")
                .replaceAll("\\+", "[^/]+")
                .replaceAll("#", "(.+)") + "$";
    }


}
