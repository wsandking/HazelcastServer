package com.genband.util.hazelcastserver.config;

 
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.XmlConfigBuilder;

/**
 * Initialize the configuration for hazelcast
 * 
 * @author sewang
 *
 */
public class HazelcastServerConfig {

    private static Logger log = Logger.getLogger(HazelcastServerConfig.class.getName());

    /**
     * Load it from environment variable
     * 
     * @param cfg
     */
    private void loadProperties(Config cfg) {

        for (DiscoveryStrategyConfig dsc : cfg.getNetworkConfig().getJoin().getDiscoveryConfig()
                .getDiscoveryStrategyConfigs()) {

            /**
             * Part 1: cluster discovery through service
             */
            if ("com.noctarius.hazelcast.kubernetes.HazelcastKubernetesDiscoveryStrategy".equals(dsc.getClassName())) {

                log.info("Initialize kubernetes hazelcast server. ");
                /**
                 * Load system level properties
                 */
                if (null != System.getenv("NAMESPACE")) {

                    dsc.addProperty("namespace", System.getenv("NAMESPACE"));
                    log.info("Properties namespace: " + System.getenv("NAMESPACE"));

                }

                if (null != System.getenv("SERVICE_NAME")) {

                    dsc.addProperty("service-name", System.getenv("SERVICE_NAME"));
                    log.info("Properties service-name: " + System.getenv("SERVICE_NAME"));

                }

            }

        }

    }

    public Config composeConfiguration() {

        Config cfg = null;
        cfg = new Config();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("hazelcast.xml");
        cfg = new XmlConfigBuilder(in).build();
        loadProperties(cfg);
        log.info(cfg.toString());

        return cfg;

    }

}
