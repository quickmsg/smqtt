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
public class AppTest3
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
                        .config(opts -> opts.memberAlias("Dan3"))

                        .membership(opts -> opts.seedMembers(Address.from("192.168.124.10:8777")))
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
        bob.members().iterator().forEachRemaining(member -> {
            bob.send(member,Message.withData("hahhas").build()).subscribe();
        });
        bob.spreadGossip(Message.withData("hahhas").build()).subscribe();
        countDownLatch.await();
    }
}
