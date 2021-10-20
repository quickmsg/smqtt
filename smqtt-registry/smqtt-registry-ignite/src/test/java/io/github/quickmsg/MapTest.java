package io.github.quickmsg;

import org.apache.ignite.IgniteCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;

/**
 * @author luxurong
 * @date 2021/10/19 08:31
 * @description
 */
public class MapTest {
    static Map<String,Object> hashMap = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        hashMap.put("3","adsasdasdasd");
        System.out.println(">> Executing the compute task");
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            System.out.println(">> " + hashMap.get("3"));
        }

        long time2 = System.currentTimeMillis();


        System.out.println("cost time " + ((time2 - time1) / 1000) + "s");
    }
}
