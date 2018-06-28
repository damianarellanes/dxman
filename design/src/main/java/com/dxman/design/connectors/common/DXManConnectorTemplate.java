package com.dxman.design.connectors.common;

import com.dxman.design.data.DXManParameter;
import com.dxman.utils.DXManMap;

/**
 * @author Damian Arellanes
 */
public class DXManConnectorTemplate {
  
  private final DXManConnectorType type;
  private final DXManMap<String, DXManParameter> inputs;
    
  public DXManConnectorTemplate(DXManConnectorType type) {

    this.type = type;
    inputs = new DXManMap<>();
  }
  
  public void addInput(DXManParameter input) {
    inputs.put(input.getName(), input);
  }
  
  public DXManConnectorType getType() { return type; }
  public DXManMap<String, DXManParameter> getInputs() { return inputs; }
}
