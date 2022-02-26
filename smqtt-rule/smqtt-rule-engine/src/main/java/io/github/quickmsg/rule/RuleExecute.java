package io.github.quickmsg.rule;

import io.github.quickmsg.common.utils.TopicRegexUtils;
import org.apache.commons.jexl3.*;
import reactor.util.context.ContextView;

import java.util.function.Consumer;

/**
 * @author luxurong
 */
public interface RuleExecute {

    JexlEngine J_EXL_ENGINE = new JexlBuilder().create();

    JxltEngine T_J_EXL_ENGINE = new JexlBuilder().create().createJxltEngine();


    /**
     * 执行
     *
     * @param context 上下文容器
     *                \
     */
    void execute(ContextView context);


    /**
     * 执行脚本
     *
     * @param script             脚本
     * @param mapContextConsumer 设置参数
     * @return Object 返回值
     */
    default Object triggerScript(String script, Consumer<MapContext> mapContextConsumer) {
        JexlExpression e = J_EXL_ENGINE.createExpression(script);
        MapContext context = new MapContext();
        mapContextConsumer.accept(context);
        context.set("TopicUtils", TopicRegexUtils.instance);
        return e.evaluate(context);
    }

    /**
     * 执行模版
     *
     * @param script             模版
     * @param mapContextConsumer 设置参数
     * @return Object 返回值
     */
    default Object triggerTemplate(String script, Consumer<MapContext> mapContextConsumer) {
        J_EXL_ENGINE.createJxltEngine();
        JxltEngine.Expression expression = T_J_EXL_ENGINE.createExpression(script);
        MapContext context = new MapContext();
        mapContextConsumer.accept(context);
        return expression.evaluate(context);
    }

}
