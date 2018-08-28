package com.dxman.design.connectors.common;

import com.dxman.design.data.DXManParameter;
import com.dxman.utils.DXManMap;

/**
 * @author Damian Arellanes
 */
public class DXManConnectorTemplate {
  
  private String classType = getClass().getName();
  
  private final String name;
  private final DXManConnectorType type;
  private final DXManMap<String, DXManParameter> inputs;
    
  public DXManConnectorTemplate(String name, DXManConnectorType type) {

    this.name = name;
    this.type = type;
    inputs = new DXManMap<>();
  }
  
  public void addInput(DXManParameter input) {
    inputs.put(input.getName(), input);
  }
  
  public String getName() { return name; }
  public DXManConnectorType getType() { return type; }
  public DXManMap<String, DXManParameter> getInputs() { return inputs; }

  public String getClassType() { return classType; }
  public void setClassType() { this.classType = getClass().getName(); }
}
