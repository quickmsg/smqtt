package io.github.quickmsg.rule;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 * Created by  lxr.
 * User: luxurong
 * Date: 2021/8/22
 */
public class Test {

    public static void main(String[] args) {
        // 创建表达式引擎对象
        JexlEngine engine = new JexlEngine();
// 创建表达式语句
        String expressionStr = "money.test";
// 创建Context对象，为表达式中的未知数赋值
        JexlContext context = new MapContext();
        context.set("money",new EasyRuleFactory(null));
// 使用表达式引擎创建表达式对象
        Expression expression = engine.createExpression(expressionStr);
// 使用表达式对象计算
        Object evaluate = expression.evaluate(context);
// 输出结果：true
        System.out.println(evaluate);

    }

}
