package com.github.smqtt.cluster.test;

import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;

import java.util.concurrent.CountDownLatch;

/**
 * Unit test for simple App.
 */
public class AppTest2
{
    /**
     * Rigorous Test :-)
     */
    public static void main(String[] a) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Cluster bob =
                new ClusterImpl()
                        .membership(opts -> opts.seedMembers(Address.from("172.18.54.65:8777")))
                        .config(opts -> opts.memberAlias("Dan111"))
                        .handler(
                                cluster -> {
                                    return new ClusterMessageHandler() {
                                        @Override
                                        public void onGossip(Message gossip) {
                                            System.out.println("gossip: " + gossip.data());
                                        }

                                        @Override
                                        public void onMembershipEvent(MembershipEvent event) {
                                            System.out.println("MembershipEvent event: " + event);

                                        }

                                        @Override
                                        public void onMessage(Message message) {
                                            System.out.println("message: " + message.data());
                                        }
                                    };
                                })
                        .startAwait();
        countDownLatch.await();
    }
}
