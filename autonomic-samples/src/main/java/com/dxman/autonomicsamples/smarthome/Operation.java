package com.dxman.autonomicsamples.smarthome;

/**
 * @author Damian Arellanes
 */
public class Operation {  
  
  private final String name;
  private final Properties properties;

  public Operation(String name, boolean requiresUserPresence,     
    double energyConsumption, double tidinessSupport) {
    this.name = name;
    
    properties = new Properties();
    properties.setValueFor("userPresence", requiresUserPresence ? 1.0 : 0.0);
    properties.setValueFor("energyConsumption", energyConsumption);
    properties.setValueFor("tidinessSupport", tidinessSupport);
  }

  public String getName() { return name; }
  public Properties getProperties() { return properties; }

  @Override
  public String toString() {
    
    StringBuilder sb = new StringBuilder();
    sb.append("Operation ").append(name).append(":");
    properties.forEach((name,property)->{
      sb.append("\n\t").append(property);
    });
    
    return sb.toString();
  }
}
