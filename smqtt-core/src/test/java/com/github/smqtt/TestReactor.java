package com.github.smqtt;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author luxurong
 * @date 2021/3/30 20:39
 * @description
 */
public class TestReactor {


    @Test
    public void testContext() throws InterruptedException {

         Mono.deferContextual(contextView ->{
            System.out.println(Thread.currentThread().getName()+"C1:"+contextView.get("test").toString());
            return Mono.deferContextual(contextView1 -> {
                System.out.println(Thread.currentThread().getName()+"C2:"+contextView1.get("test").toString());
                return Mono.just(1);
            });
        }).contextWrite(context -> context.put("test","haha")).subscribeOn(Schedulers.single()).subscribe();
//        Flux<Integer> flux =Flux.range(1,100);
//        flux.doOnNext();
        Thread.sleep(100000l);
    }

}
