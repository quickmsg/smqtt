package io.github.quickmsg.common.acl.filter;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.casbin.jcasbin.util.BuiltInFunctions;
import org.casbin.jcasbin.util.function.CustomFunction;

import java.util.Map;

/**
 * ip{}
 * id{}
 * all
 *
 * @author luxurong
 */
public class AclFunction extends CustomFunction {


    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        String requestSubject = FunctionUtils.getStringValue(arg1, env);
        String subject = FunctionUtils.getStringValue(arg2, env);
        if (subject.startsWith("ip")) {
            int startIndex = subject.indexOf("{");
            int endIndex = subject.indexOf("}");
            String ip = requestSubject.split(":")[1];
            return   AviatorBoolean.valueOf(BuiltInFunctions.ipMatch(ip, subject.substring(startIndex + 1, endIndex)));
        } else if (subject.equals("all")) {
            return AviatorBoolean.valueOf(true);
        } else {
            String clientId = requestSubject.split(":")[0];
            return AviatorBoolean.valueOf(clientId.equals(subject));
        }
    }

    @Override
    public String getName() {
        return "filter";
    }
}
