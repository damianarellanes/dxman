package leds;

import java.util.HashMap;

/**
 * @author Damian Arellanes
 */
public class Config {
    
  /* Things */
  public static final String PI = "192.168.0.23";
  public static final String ALIENWARE = "192.168.0.5";
  
  /* Service Impl */
  public static final String LED_IMPL_ENDPOINT = ALIENWARE;
  
  /* Connectors deployment */  
  public static final HashMap<String, ConnectorConfig> CONNECTOR_CONFIGS;
  static {
    CONNECTOR_CONFIGS = new HashMap<>();
    CONNECTOR_CONFIGS.put("Led0", new ConnectorConfig("IC0", PI, LED_IMPL_ENDPOINT));
    CONNECTOR_CONFIGS.put("Led1", new ConnectorConfig("IC1", ALIENWARE, LED_IMPL_ENDPOINT));
    CONNECTOR_CONFIGS.put("Led2", new ConnectorConfig("IC2", PI, LED_IMPL_ENDPOINT));
    CONNECTOR_CONFIGS.put("Led3", new ConnectorConfig("IC3", PI, LED_IMPL_ENDPOINT));
    CONNECTOR_CONFIGS.put("SEQ0", new ConnectorConfig("SEQ0", ALIENWARE));
    CONNECTOR_CONFIGS.put("PAR0", new ConnectorConfig("PAR0", ALIENWARE));
    CONNECTOR_CONFIGS.put("SEQ1", new ConnectorConfig("SEQ1", ALIENWARE));
  }
  
  public static class ConnectorConfig {
  
    private final String resource;
    private final String targetThing;
    private final String uri;    
    private final String implEndpoint;
    
    public ConnectorConfig(String resource, String targetThing) {
      this.resource = resource;
      this.targetThing = targetThing;
      this.uri = "coap://" + targetThing + ":5683/" + resource;
      this.implEndpoint = "";
    }
    
    public ConnectorConfig(String resource, String targetThing, String implEndpoint) {
      this.resource = resource;
      this.targetThing = targetThing;
      this.uri = "coap://" + targetThing + ":5683/" + resource;
      this.implEndpoint = implEndpoint;
    }

    public String getResource() { return resource; }
    public String getTargetThing() { return targetThing; }
    public String getUri() { return uri; }
    public String getImplEndpoint() { return implEndpoint; }
  }
}
