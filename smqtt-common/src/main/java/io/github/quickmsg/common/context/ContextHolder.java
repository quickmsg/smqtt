package io.github.quickmsg.common.context;

import io.github.quickmsg.common.config.Configuration;

/**
 * @author luxurong
 */
public class ContextHolder {

   private static  ReceiveContext<?> context ;

   public static void setReceiveContext(ReceiveContext<?> context){
       ContextHolder.context = context;
   }

    public static ReceiveContext<?> getReceiveContext(){
        return ContextHolder.context;
    }


}
