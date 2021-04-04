package com.github.smqtt;

import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author luxurong
 * @date 2021/3/30 20:39
 * @description
 */
public class TestReactor {


    @Test
    public void testContext() throws InterruptedException {

        Mono.deferContextual(contextView -> {
            System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().getId() + "C1:" + contextView.get("test").toString());
            return Mono.deferContextual(contextView1 -> {
                System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().getId() + "C2:" + contextView1.get("test").toString());
                return Mono.just(1);
            });
        }).contextWrite(context -> context.put("test", "haha")).subscribe();
//        Flux<Integer> flux =Flux.range(1,100);
//        flux.doOnNext();
        System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().getId());
        Thread.sleep(100000l);
    }


    @Test
    public void testSink() {

        Sinks.Many<String> replaySink = Sinks.many().replay().all();
        replaySink.asFlux().subscribe(System.out::println);
        replaySink.tryEmitNext("sd1");
        replaySink.tryEmitNext("sd2");
        replaySink.tryEmitNext("sd3");
        replaySink.asFlux().subscribe(System.out::println);


        Sinks.Many<String> stringMany = Sinks.many().multicast().onBackpressureBuffer(3);

        stringMany.tryEmitNext("test1");
        stringMany.tryEmitNext("test2");
        stringMany.tryEmitNext("test3");
        stringMany.asFlux().subscribe(System.out::println);


    }


    @Test
    public void testRetry() throws InterruptedException {



         Disposable disposable= Mono.fromRunnable(() -> {throw new RuntimeException("123");})
                    .delaySubscription(Duration.ofSeconds(1)).doOnError(throwable ->System.out.println("cuowu qu xiao le") ).doOnCancel(()->System.out.println("qu xiao le")).repeat().subscribe();
        Thread.sleep(10000);

        disposable.dispose();
        Thread.sleep(100000l);

    }

    @Test
    public  void njj(){
        Mono.fromRunnable(()->System.out.println("asdds")).then(Mono.empty())
                .subscribe();

    }


    @Test
    public void testWindow() {

        Flux<Long> times = Flux.create(fluxSink -> {
            for (; ; ) {
                try {
                    Thread.sleep(100);
                    fluxSink.next(System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        AtomicInteger atomicInteger = new AtomicInteger(1);
        times
                .windowTimeout(Integer.MAX_VALUE, Duration.ofSeconds(10))
//                windowUntil(time -> (time - System.currentTimeMillis()) < 1000)
                .buffer(Duration.ofSeconds(10))
                .subscribe(longFlux -> {

                    atomicInteger.incrementAndGet();
//                    longFlux.subscribe(t -> {
//                        System.out.println(atomicInteger.get() + ":" + t);
//                    }) ;
                });

    }


    @Test
    public void testAllMono() throws InterruptedException {

        List<Mono<Integer>> list = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        for(int i=0;i<10;i++){
            list.add( Mono.fromRunnable(()->{
                System.out.println("haha:"+atomicInteger.incrementAndGet());
            }));
        }
        Mono.when(list).subscribe(System.out::println);
//        Mono.firstWithSignal(list).subscribe(System.out::println);

        Thread.sleep(100000l);

    }

}
