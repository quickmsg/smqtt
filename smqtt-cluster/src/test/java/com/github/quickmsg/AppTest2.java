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
public class AppTest2
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //noinspection unused
        Cluster bob =
                new ClusterImpl()
                        .membership(opts -> opts.seedMembers(Address.from("192.168.124.10:8777")))
                        .config(opts -> opts.memberAlias("Dan2"))
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
        countDownLatch.await();
    }
}
