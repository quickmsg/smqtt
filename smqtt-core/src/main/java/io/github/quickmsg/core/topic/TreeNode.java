package io.github.quickmsg.core.topic;

import io.github.quickmsg.common.topic.SubscribeTopic;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author luxurong
 */
@Getter
@Setter
public class TreeNode {

    private final String topic;

    private int subscribeTopicNumber;


    private CopyOnWriteArrayList<SubscribeTopic> subscribes = new CopyOnWriteArrayList<>();

    private Map<String, TreeNode> childNodes = new ConcurrentHashMap<>();

    public TreeNode(String topic) {
        this.topic = topic;
    }

    private final String ONE_SYMBOL = "+";

    private final String MORE_SYMBOL = "#";

    public boolean addSubscribeTopic(SubscribeTopic subscribeTopic) {
        String[] topics = subscribeTopic.getTopicFilter().split("/");
        return addIndex(subscribeTopic, topics, 0);
    }


    private boolean addTreeSubscribe(SubscribeTopic subscribeTopic) {
        return subscribes.add(subscribeTopic);
    }


    private boolean addIndex(SubscribeTopic subscribeTopic, String[] topics, Integer index) {
        String lastTopic = topics[index];
        TreeNode treeNode = childNodes.computeIfAbsent(lastTopic, tp -> new TreeNode(lastTopic));
        if (index == topics.length - 1) {
            return treeNode.addTreeSubscribe(subscribeTopic);
        } else {
            return treeNode.addIndex(subscribeTopic, topics, index + 1);
        }
    }


    public List<SubscribeTopic> getSubscribeByTopic(String topicFilter) {
        String[] topics = topicFilter.split("/");
        return searchTree(topics);
    }


    private List<SubscribeTopic> searchTree(String[] topics) {
        LinkedList<SubscribeTopic> subscribeTopicList = new LinkedList<>();
        loadTreeSubscribes(this, subscribeTopicList, topics, 0);
        return subscribeTopicList;
    }

    private void loadTreeSubscribes(TreeNode treeNode, LinkedList<SubscribeTopic> subscribeTopics, String[] topics, Integer index) {
        String lastTopic = topics[index];
        TreeNode moreTreeNode = treeNode.getChildNodes().get(MORE_SYMBOL);
        if (moreTreeNode != null) {
            subscribeTopics.addAll(moreTreeNode.getSubscribes());
        }
        if (index == topics.length - 1) {
            TreeNode localTreeNode = treeNode.getChildNodes().get(lastTopic);
            if (localTreeNode != null) {
                CopyOnWriteArrayList<SubscribeTopic> subscribes = localTreeNode.getSubscribes();
                if (subscribes != null && subscribes.size() > 0) {
                    subscribeTopics.addAll(subscribes);
                }
            }
            localTreeNode = treeNode.getChildNodes().get(ONE_SYMBOL);
            if (localTreeNode != null) {
                CopyOnWriteArrayList<SubscribeTopic> subscribes = localTreeNode.getSubscribes();
                if (subscribes != null && subscribes.size() > 0) {
                    subscribeTopics.addAll(subscribes);
                }
            }

        } else {
            TreeNode oneTreeNode = treeNode.getChildNodes().get(ONE_SYMBOL);
            if (oneTreeNode != null) {
                loadTreeSubscribes(oneTreeNode, subscribeTopics, topics, index + 1);
            }
            TreeNode node = treeNode.getChildNodes().get(lastTopic);
            if (node != null) {
                loadTreeSubscribes(node, subscribeTopics, topics, index + 1);
            }
        }

    }

    public boolean removeSubscribeTopic(SubscribeTopic subscribeTopic) {
        TreeNode node = this;
        String[] topics = subscribeTopic.getTopicFilter().split("/");
        for (String topic : topics) {
            if (node != null) {
                node = node.getChildNodes().get(topic);
            }
        }
        if (node != null) {
            CopyOnWriteArrayList<SubscribeTopic> subscribeTopics = node.getSubscribes();
            if (subscribeTopics != null) {
                return subscribeTopics.remove(subscribeTopic);
            }
        }
        return false;
    }

}
