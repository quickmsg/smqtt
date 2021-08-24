package io.github.quickmsg.rule;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import java.util.function.Consumer;

/**
 * @author luxurong
 */
public interface RuleExecute {

    JexlEngine JEXL_ENGINE = new JexlBuilder().create();


    /**
     * 执行
     *
     * @param param   参数
     * @return Object
     */
    Object execute(Object param);


    default Object triggerScript(String script, Object param, Consumer<MapContext> mapContextConsumer) {
        JexlExpression e = JEXL_ENGINE.createExpression(script);
        MapContext context = new MapContext();
        mapContextConsumer.accept(context);
        context.set("$", param);
        return e.evaluate(context);
    }

}
