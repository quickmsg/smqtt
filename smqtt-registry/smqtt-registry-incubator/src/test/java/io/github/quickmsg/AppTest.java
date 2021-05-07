package io.github.quickmsg;

import static org.junit.Assert.assertTrue;

import com.codahale.metrics.MetricRegistry;
import org.apache.gossip.GossipMember;
import org.apache.gossip.GossipService;
import org.apache.gossip.GossipSettings;
import org.apache.gossip.RemoteGossipMember;
import org.apache.gossip.event.GossipListener;
import org.apache.gossip.event.GossipState;
import org.apache.gossip.model.SharedGossipDataMessage;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws URISyntaxException, UnknownHostException, InterruptedException {

        GossipSettings settings = new GossipSettings();
        int seedNodes = 3;
        List<GossipMember> startupMembers = new ArrayList<>();
        for (int i = 1; i < seedNodes+1; ++i) {
            URI uri = new URI("udp://" + "127.0.0.1" + ":" + (50000 + i));
            startupMembers.add(new RemoteGossipMember("cluster", uri, i + ""));
        }
        List<GossipService> clients = new ArrayList<>();
        int clusterMembers = 5;
        GossipService gossipService = null;
        for (int i = 1; i < clusterMembers+1; ++i) {
            URI uri = new URI("udp://" + "127.0.0.1" + ":" + (50000 + i));
             gossipService = new GossipService("cluster", uri, i + "",null,
                    startupMembers, settings, new GossipListener() {
                @Override
                public void gossipEvent(GossipMember member, GossipState state) {
                    System.out.println(member+state.name());
                }
            }, new MetricRegistry());
            gossipService.start();
            SharedGossipDataMessage sharedGossipDataMessage= new SharedGossipDataMessage();
            sharedGossipDataMessage.setPayload(i);
            sharedGossipDataMessage.setKey(i+"");
            sharedGossipDataMessage.setTimestamp(System.currentTimeMillis());
            sharedGossipDataMessage.setExpireAt(Long.MAX_VALUE);
            gossipService.gossipSharedData(sharedGossipDataMessage);
        }
        Thread.sleep(2000);
        if(gossipService !=null){
            SharedGossipDataMessage sharedGossipDataMessage = gossipService.findSharedData("asdsa");
            System.out.println(sharedGossipDataMessage.getPayload());

        }

        Thread.sleep(1000000);
    }
}
