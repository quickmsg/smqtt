package io.github.quickmsg.rule;

import reactor.util.context.ContextView;

import java.util.Optional;

/**
 * @author luxurong
 */
public interface RuleNode extends RuleExecute {

    /***
     * 获取下一个RuleNode
     * @return {@link RuleNode}
     */
    RuleNode getNextRuleNode();


    /***
     * 设置下一个RuleNode
     * @param ruleNode {@link RuleNode}
     */
    void setNextRuleNode(RuleNode ruleNode);


    /**
     * 执行下个Node
     *
     * @param context {@link ContextView}
     */
    default void executeNext(ContextView context) {
        Optional.ofNullable(getNextRuleNode())
                .ifPresent(ruleNode -> ruleNode.execute(context));
    }



}
