package io.github.quickmsg.common.utils;

/**
 * @author luxurong
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
