package com.github.smqtt;

import org.junit.Test;
import reactor.core.publisher.Mono;

/**
 * @author luxurong
 * @date 2021/3/30 20:39
 * @description
 */
public class TestReactor {


    @Test
    public void testContext(){


        Mono.deferContextual(contextView ->{
            System.out.println(contextView.get("test").toString());
            return Mono.deferContextual(contextView1 -> {
                System.out.println(contextView1.get("test").toString());
                return Mono.just("1");
            });
        }).contextWrite(context ->
            context.put("test","test")
        ).subscribe(ce->System.out.println(ce));

    }

}
