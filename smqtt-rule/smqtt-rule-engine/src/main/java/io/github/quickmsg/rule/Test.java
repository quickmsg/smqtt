package io.github.quickmsg.rule;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

/**
 * Created by  lxr.
 * User: luxurong
 * Date: 2021/8/22
 */
public class Test {

    public static void main(String[] args) {
        String expressionStr = "1+7,8+2";


        // Create a JexlEngine (could reuse one instead)
        JexlEngine jexl = new JexlBuilder().create();
        // Create an expression object equivalent to 'car.getEngine().checkStatus()':
        String jexlExp = "car.engine.checkStatus();";
        JexlExpression e = jexl.createExpression(expressionStr);
        JexlContext context1 = new MapContext();
        context1.set("$", RuleChain.class);
        // Now evaluate the expression, getting the result
        Object o = e.evaluate(context1);
        System.out.println(o);

    }

}
