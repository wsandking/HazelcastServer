package com.genband.platform.hazelcastserver;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.genband.platform.hazelcastserver.config.HazelcastServerConfig;
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

    BasicConfigurator.configure();
    
//    HazelcastInstance instance = Hazelcast.newHazelcastInstance();
    HazelcastInstance instance =
        Hazelcast.newHazelcastInstance(new HazelcastServerConfig().composeConfiguration());
    log.info(String.format("Hazelcast: %s running successfully", instance.getName()));


  }

}
