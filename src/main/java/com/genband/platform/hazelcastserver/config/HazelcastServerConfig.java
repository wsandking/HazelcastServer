package com.genband.platform.hazelcastserver.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.spi.discovery.DiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryStrategyFactory;
import com.noctarius.hazelcast.kubernetes.HazelcastKubernetesDiscoveryStrategyFactory;

/**
 * Initialize the configuration for hazelcast
 * 
 * @author sewang
 *
 */
public class HazelcastServerConfig {

  /**
   * Load it from environment variable
   * 
   * @param cfg
   */
  private void loadProperties(Config cfg) {

    System.out.println(cfg.getInstanceName());

    for (DiscoveryStrategyConfig dsc : cfg.getNetworkConfig().getJoin().getDiscoveryConfig()
        .getDiscoveryStrategyConfigs()) {
      
      System.out.println("DSC Name: " + dsc.getClassName());
      for (String key : dsc.getProperties().keySet()) {

        System.out.println("DSC key: " + key + " value: " + dsc.getProperties().get(key));

      }
        
      dsc.getProperties().replace("namespace", "notdefault");
      
      for (String key : dsc.getProperties().keySet()) {

        System.out.println("DSC key: " + key + " value: " + dsc.getProperties().get(key));

      }

    }

  }

  public Config composeConfiguration() {

    Config cfg = null;

    InputStream in = this.getClass().getClassLoader().getResourceAsStream("hazelcast.xml");
    cfg = new XmlConfigBuilder(in).build();
    System.out.println();

    loadProperties(cfg);



    return cfg;

  }

}
