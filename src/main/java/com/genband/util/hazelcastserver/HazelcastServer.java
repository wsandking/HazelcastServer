package com.genband.util.hazelcastserver;

import com.genband.util.hazelcastserver.config.HazelcastServerConfig;
import com.genband.util.log.slf4j.GbLogger;
import com.genband.util.log.slf4j.GbLoggerFactory;
// import com.genband.util.hazelcastserver.test.KubernetesHazelcastTest;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Entry point of the hazelcast server application
 * 
 * @author sewang
 *
 */
public class HazelcastServer {

    private static GbLogger log = GbLoggerFactory.getGbLogger(HazelcastServer.class.getName());

    public static void main(String args[]) {

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(new HazelcastServerConfig().composeConfiguration());
        log.info(String.format("Hazelcast server: %s started successfully", instance.getName()));

    }

}
