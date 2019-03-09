package com.dxman.autonomicsamples.smarthome;

/**
 * @author Damian Arellanes
 */
public class Property {
  
  private final String name;
  private double value;

  public Property(String name) {
    this.name = name;
  }

  public String getName() { return name; }
  public double getValue() { return value; }
  public void setValue(double value) { this.value = value; }

  @Override
  public String toString() {    
    return name + " --> " + value;
  }
}
