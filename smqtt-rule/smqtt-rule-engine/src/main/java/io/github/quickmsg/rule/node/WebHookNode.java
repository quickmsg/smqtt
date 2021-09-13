package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 * @date 2021/9/10 16:39
 * @description
 */
public class WebHookNode implements RuleNode {

    private RuleNode ruleNode;


    @Override
    public RuleNode getNextRuleNode() {
        return this.ruleNode;
    }

    @Override
    public void setNextRuleNode(RuleNode ruleNode) {
        this.ruleNode = ruleNode;
    }

    @Override
    public void execute(ContextView context) {
        //todo 待实现http客户端
        executeNext(context);
    }
}
