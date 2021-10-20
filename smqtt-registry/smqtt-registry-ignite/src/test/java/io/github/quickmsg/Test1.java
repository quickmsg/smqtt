package io.github.quickmsg;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Collections;

/**
 * @author luxurong
 * @date 2021/10/18 17:50
 * @description
 */
public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        // Preparing IgniteConfiguration using Java APIs
        IgniteConfiguration cfg = new IgniteConfiguration();

        // The node will be started as a client node.
        cfg.setClientMode(false);
        cfg.setLocalHost("127.0.0.1");
        // Classes of custom Java logic will be transferred over the wire from this app.
        cfg.setPeerClassLoadingEnabled(true);

        // Setting up an IP Finder to ensure the client can locate the servers.
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

        // Starting the node
        Ignite ignite = Ignition.start(cfg);

        // Create an IgniteCache and put some values in it.
        IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCache");
        cache.put(1, "Hello");
        cache.put(2, "World!");

        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            System.out.println(">> " + cache.get(3));
        }

        long time2 = System.currentTimeMillis();


        System.out.println("cost time " + ((time2 - time1) / 1000) + "s");

        System.out.println(">> Compute task is executed, check for output on the server nodes.");

        // Disconnect from the cluster.
        Thread.sleep(1000000L);

        ignite.close();
    }

    /**
     * A compute tasks that prints out a node ID and some details about its OS and JRE.
     * Plus, the code shows how to access data stored in a cache from the compute task.
     */
    private static class RemoteTask implements IgniteRunnable {
        @IgniteInstanceResource
        Ignite ignite;

        @Override
        public void run() {
            System.out.println(">> Executing the compute task");


        }
    }
}
