package com.genband.util.hazelcastserver.config;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.MapConfig;
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

		/**
		 * 
		 * Hazelcast server map configuration
		 * 
		 */
		String timeToLive = System.getenv("MAP_TIME_TO_LIVE");
		String nameSpace = System.getenv("NAMESPACE");
		String serviceName = System.getenv("SERVICE_NAME");

		if (null != cfg.getMapConfig("MobilePushEventMap")) {

			log.info("Configuring Mobile Push Event Map ");
			MapConfig mapCfg = cfg.getMapConfig("MobilePushEventMap");

			if (null != timeToLive && timeToLive.matches("^-?\\d+$")) {

				mapCfg.setTimeToLiveSeconds(Integer.parseInt(System.getenv("MAP_TIME_TO_LIVE")));

			}

		}
		/**
		 * 
		 * Hazelcast Discovery
		 * 
		 */
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
				if (null != nameSpace) {

					dsc.addProperty("namespace", nameSpace);
					log.info("Properties namespace: " + nameSpace);

				}
				if (null != serviceName) {

					dsc.addProperty("service-name", serviceName);
					log.info("Properties service-name: " + serviceName);

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
