package com.github.quickmsg;

import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Cluster alice =
                new ClusterImpl()
                        .config(opts -> opts.memberAlias("Dan1"))
                        .membership(opts -> opts.seedMembers(Address.from("localhost:8778"),Address.from("localhost:8779")))
                        .transport(transportConfig -> transportConfig.port(8777))
                        .handler(
                                cluster -> {
                                    return new ClusterMessageHandler() {
                                        @Override
                                        public void onGossip(Message gossip) {
                                            System.out.println( gossip.sender()+"gossip: " + gossip.data());
                                        }

                                        @Override
                                        public void onMembershipEvent(MembershipEvent event) {
                                            System.out.println("MembershipEvent event: " + event);

                                        }

                                        @Override
                                        public void onMessage(Message message) {
                                            System.out.println(message.sender()+"message: " + message.data());
                                        }
                                    };
                                })
                        .start().block();
        for(;;){
            Thread.sleep(1000);
            alice.spreadGossip(Message.fromQualifier("ads").fromData("ad")).subscribe();

        }
//        countDownLatch.await();
    }
}
