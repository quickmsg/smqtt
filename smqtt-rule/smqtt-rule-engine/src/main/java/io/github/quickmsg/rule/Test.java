package io.github.quickmsg.rule;

import io.github.quickmsg.common.utils.JacksonUtil;
import org.apache.commons.jexl3.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by  lxr.
 * User: luxurong
 * Date: 2021/8/22
 */
public class Test {

    public static void main(String[] args) {
        testTemplate();
    }

    public static void testMethod(){
        String expressionStr = "test";
        // Create a JexlEngine (could reuse one instead)
        JexlEngine jexl = new JexlBuilder().create();
        // Create an expression object equivalent to 'car.getEngine().checkStatus()':
        JexlExpression e = jexl.createExpression(expressionStr);
        JexlContext context1 = new MapContext();
        context1.set("test", "难受啊");
        // Now evaluate the expression, getting the result
        Object o = e.evaluate(context1).toString();
        System.out.println(o);
    }


    public static void testTemplate(){


        JexlContext context1 = new MapContext();
        Map<String,Object> msp = new HashMap<>();
        msp.put("hah",2);
        context1.set("test", msp);
        JexlEngine jexl2 = new JexlBuilder().create();
        JxltEngine jxlt = jexl2.createJxltEngine();
        String exp = "{'key' :${test.hah}}";
        JxltEngine.Expression expr = jxlt.createExpression(exp);

        String hello = expr.evaluate(context1).toString();
        Map map=JacksonUtil.json2Bean(hello,Map.class);

        System.out.println(map);
    }

}
