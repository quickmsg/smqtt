package io.github.quickmsg.rule;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import reactor.util.context.ContextView;

import java.util.function.Consumer;

/**
 * @author luxurong
 */
public interface RuleExecute {

    JexlEngine JEXL_ENGINE = new JexlBuilder().create();


    /**
     * 执行
     *
     * @param context 上下文容器
     * @return Boolean 是否往下走
     */
    Boolean execute(ContextView context);


    default Object triggerScript(String script, Object param, Consumer<MapContext> mapContextConsumer) {
        JexlExpression e = JEXL_ENGINE.createExpression(script);
        MapContext context = new MapContext();
        mapContextConsumer.accept(context);
        context.set("$", param);
        return e.evaluate(context);
    }

}
