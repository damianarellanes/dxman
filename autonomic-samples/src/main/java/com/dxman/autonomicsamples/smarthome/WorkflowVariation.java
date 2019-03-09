package com.dxman.autonomicsamples.smarthome;

/**
 * @author Damian Arellanes
 */
public final class WorkflowVariation {
  
  private final Operation[] operations;
  private final Properties properties;

  public WorkflowVariation(Operation... operations) {
    this.operations = operations;
    properties = new Properties();
    computeProperties();
  }
  
  public void computeProperties() {
    computeUserPresence();
    computeEnergyConsumption();
    computeTidiness();
  }
  
  // Average of user presences: u(w_i)
  private void computeUserPresence() {
    
    double average = 0.0;
    for(Operation op: operations) { 
      average += op.getProperties().getValueFor("userPresence"); 
    }    
    properties.get("userPresence").setValue(average / operations.length);
  }
  
  // Sum of energy consumptions: e(w_i)
  private void computeEnergyConsumption() {
    
    double sum = 0.0;
    for(Operation op: operations) { 
      sum += op.getProperties().getValueFor("energyConsumption"); 
    }    
    properties.get("energyConsumption").setValue(sum);
  }
  
  // Sum of tidiness: t(w_i)
  private void computeTidiness() {
    
    double sum = 0.0;
    for(Operation op: operations) { 
      sum += op.getProperties().getValueFor("tidinessSupport");
    }    
    properties.get("tidinessSupport").setValue(sum);
  }

  public Properties getProperties() { return properties; }
  
  @Override
  public String toString() {
    
    StringBuilder sb = new StringBuilder();
    sb.append("Variation --> ");
    for(Operation op: operations) {
      sb.append(" | ").append(op.getName());
    }
    properties.forEach((name,property)->{
      sb.append("\n\t").append(property);
    });
    
    return sb.toString();
  }
}
