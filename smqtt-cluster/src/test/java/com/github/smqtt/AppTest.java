package com.github.smqtt;

import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
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
                        .config(opts -> opts.memberAlias("Dan1").externalPort(9777))
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
                        .startAwait();
        System.out.println(alice.address().toString());
        alice.members().forEach(System.out::println);
        countDownLatch.await();
    }
}
