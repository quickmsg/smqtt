package io.github.quickmsg.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luxurong
 * @date 2021/8/25 15:50
 * @description
 */
public class SourceManager {

   private static final Map<Source,SourceBean>  cacheBeans = new ConcurrentHashMap<>();


   public static SourceBean getSourceBean(Source source){
       return cacheBeans.get(source);
   }


}
