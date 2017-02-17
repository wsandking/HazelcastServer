package com.genband.util.hazelcastserver;

import org.apache.log4j.Logger;

import com.genband.util.hazelcastserver.config.HazelcastServerConfig;
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

    private static Logger log = Logger.getLogger(HazelcastServer.class.getName());

    public static void main(String args[]) {

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(new HazelcastServerConfig().composeConfiguration());

        // HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        
        /*
         * Testing code, will be removed when things got stable Map<String, String> theMap = instance.getMap("TempMap");
         * theMap.put("haha", "notveryhaha"); log.info(String.format("%s : %s", "haha", theMap.get("haha"))); try {
         * while (null != theMap.get("haha")) { Thread.sleep(2000); log.info(String.format("loop %s : %s", "haha",
         * theMap.get("haha"))); } } catch (InterruptedException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); }
         */

        log.info(String.format("Hazelcast server: %s started successfully", instance.getName()));

    }

}
