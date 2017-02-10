package com.genband.util.hazelcastserver.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hazelcast.config.NetworkConfig;
 
import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;

import io.fabric8.kubernetes.api.model.EndpointAddress;
import io.fabric8.kubernetes.api.model.EndpointSubset;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;



public class KubernetesHazelcastTest {

  private final String serviceName;
  private final String namespace;
  private KubernetesClient client;

  private static Logger log = Logger.getLogger(KubernetesHazelcastTest.class.getName());

  public KubernetesHazelcastTest() {

    this.serviceName = "";
    this.namespace = "default";

    init();

  }

  private void init() {
    
    try {
      
      String accountToken = getAccountToken();
      log.info("Kubernetes Discovery: Bearer Token { " + accountToken + " }");
      Config config = new ConfigBuilder().withOauthToken(accountToken).build();
      this.client = new DefaultKubernetesClient(config);
      log.error("Initialization finished... ");
      
    } catch (Exception e) {
      
      log.error("cannot load anything... Halt");
      e.printStackTrace();
      
    }
  
  }

  public List<DiscoveryNode> resolve() {

    log.info(String.format("Service discovery part %s ",
        client.services().inNamespace(namespace).withName(serviceName)));

    List<DiscoveryNode> result = Collections.emptyList();
    if (serviceName != null && !serviceName.isEmpty()) {
      result = getSimpleDiscoveryNodes(
          client.endpoints().inNamespace(namespace).withName(serviceName).get());
    }

    return result;
  }


  private List<DiscoveryNode> getSimpleDiscoveryNodes(Endpoints endpoints) {
    if (endpoints == null) {
      return Collections.emptyList();
    }
    List<DiscoveryNode> discoveredNodes = new ArrayList<DiscoveryNode>();
    for (EndpointSubset endpointSubset : endpoints.getSubsets()) {
      for (EndpointAddress endpointAddress : endpointSubset.getAddresses()) {
        Map<String, Object> properties = endpointAddress.getAdditionalProperties();
        String ip = endpointAddress.getIp();
        InetAddress inetAddress = mapAddress(ip);
        int port = getServicePort(properties);
        Address address = new Address(inetAddress, port);
        discoveredNodes.add(new SimpleDiscoveryNode(address, properties));
      }
    }
    return discoveredNodes;
  }

  protected InetAddress mapAddress(String address) {
    if (address == null) {
      return null;
    }
    try {
      return InetAddress.getByName(address);
    } catch (UnknownHostException e) {
      log.warn("Address '" + address + "' could not be resolved");
    }
    return null;
  }


  protected int getServicePort(Map<String, Object> properties) {
    int port = NetworkConfig.DEFAULT_PORT;
    return port;
  }

  private String getAccountToken() {
    try {
      String tokenFile = "/var/run/secrets/kubernetes.io/serviceaccount/token";
      File file = new File(tokenFile);
      byte[] data = new byte[(int) file.length()];
      InputStream is = new FileInputStream(file);
      is.read(data);
      return new String(data);

    } catch (IOException e) {
      throw new RuntimeException("Could not get token file", e);
    }
  }
}
