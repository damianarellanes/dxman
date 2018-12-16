package test;

import java.util.HashMap;

/**
 * @author Damian Arellanes
 */
public class Config {
    
  /* Things */
  public static final String PI = "192.168.0.23";
  public static final String ALIENWARE = "192.168.0.5";
  
  /* Service Impl */
  public static final String COURIER_IMPL_ENDPOINT = ALIENWARE;
  
  /* Connectors deployment */  
  public static final HashMap<String, ConnectorConfig> CONNECTOR_CONFIGS;
  static {
    CONNECTOR_CONFIGS = new HashMap<>();
    CONNECTOR_CONFIGS.put("Courier1", new ConnectorConfig("IC2", ALIENWARE, COURIER_IMPL_ENDPOINT));
    CONNECTOR_CONFIGS.put("Courier2", new ConnectorConfig("IC3", ALIENWARE, COURIER_IMPL_ENDPOINT));
    CONNECTOR_CONFIGS.put("SEQ1", new ConnectorConfig("SEQ1", ALIENWARE, COURIER_IMPL_ENDPOINT));
  }
  
  public static class ConnectorConfig {
  
    private final String resource;
    private final String thingIP;
    private final String thingPort;
    private final String uri;    
    private final String implEndpoint;
    
    public ConnectorConfig(String resource, String thingIP, String thingPort) {
      this.resource = resource;
      this.thingIP = thingIP;
      this.thingPort = thingPort;
      this.uri = "coap://" + thingIP + ":" + thingPort + "/" + resource;
      this.implEndpoint = "";
    }
    
    public ConnectorConfig(String resource, String thingIP, String thingPort, String implEndpoint) {
      this.resource = resource;
      this.thingIP = thingIP;
      this.thingPort = thingPort;
      this.uri = "coap://" + thingIP + ":" + thingPort + "/" + resource;
      this.implEndpoint = implEndpoint;
    }

    public String getResource() { return resource; }
    public String getTargetThing() { return thingIP; }
    public String getUri() { return uri; }
    public String getImplEndpoint() { return implEndpoint; }
  }
}
