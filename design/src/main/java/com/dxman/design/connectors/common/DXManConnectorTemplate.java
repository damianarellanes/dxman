package com.dxman.design.connectors.common;

import com.dxman.design.data.DXManParameter;
import com.dxman.utils.*;

/**
 * @author Damian Arellanes
 */
public class DXManConnectorTemplate {
  
  private DXManConnectorType classType; // For deserializing using Gson
  
  private final String id;
  private final String name;
  private final DXManConnectorType type;
  private final DXManMap<String, DXManParameter> inputs;
    
  public DXManConnectorTemplate(String name, DXManConnectorType type) {

    this.id = DXManIDGenerator.generateConnectorID();
    this.classType = type;
    this.name = name;
    this.type = type;
    inputs = new DXManMap<>();
  }
  
  public void addInput(DXManParameter input) {
    inputs.put(input.getName(), input);
  }
    
  public String getId() { return id; }
  public String getName() { return name; }
  public DXManConnectorType getType() { return type; }
  public DXManMap<String, DXManParameter> getInputs() { return inputs; }

  public DXManConnectorType getClassType() { return classType; }
  public void setClassType() { this.classType = type; }
}
