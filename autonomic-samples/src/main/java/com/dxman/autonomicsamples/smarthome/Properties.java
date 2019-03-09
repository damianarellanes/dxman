package com.dxman.autonomicsamples.smarthome;

import java.util.HashMap;

/**
 * @author Damian Arellanes
 */
public class Properties extends HashMap<String, Property> {
  
  public Properties() {
    put("userPresence", new Property("User Presence Required (u)"));    
    put("energyConsumption", new Property("Energy Consumption (e)"));
    put("tidinessSupport", new Property("Tidiness Support (t)"));
  }
  
  public double getValueFor(String property) {
    return get(property).getValue();
  }
  
  public void setValueFor(String property, double value) {
    get(property).setValue(value);
  }
}
