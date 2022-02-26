package io.github.quickmsg.common.utils;

/**
 * @author luxurong
 */
public class TopicRegexUtils {

    public static final TopicRegexUtils instance = new TopicRegexUtils();

    public static String regexTopic(String topic) {
        if (topic.startsWith("$")) {
            topic = "\\" + topic;
        }
        return topic
                .replaceAll("/", "\\\\/")
                .replaceAll("\\+", "[^/]+")
                .replaceAll("#", "(.+)") + "$";
    }

    public boolean match(String sourcesTopic, String targetTopic) {
        if (sourcesTopic == null || "".equals(sourcesTopic) || targetTopic == null || "".equals(targetTopic)) {
            return false;
        }
        return sourcesTopic.matches(TopicRegexUtils.regexTopic(targetTopic));
    }

}
