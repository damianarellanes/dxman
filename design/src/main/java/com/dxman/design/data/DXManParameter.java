package com.dxman.design.data;

import com.dxman.utils.DXManIDGenerator;

/**
 * @author Damian Arellanes
 */
public class DXManParameter {
        
  private String id;
  private String name;
  private DXManParameterType parameterType;
  private String valueType;

  public DXManParameter() {}

  public DXManParameter(String name, DXManParameterType parameterType, 
    String valueType) {        

    this.id = DXManIDGenerator.generateParameterID(name);
    this.name = name;
    this.parameterType = parameterType;
    this.valueType = valueType;
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public DXManParameterType getParameterType() { return parameterType; }
  public void setParameterType(DXManParameterType parameterType) { 
    this.parameterType = parameterType; 
  }

  public String getValueType() { return valueType; }
  public void setValueType(String valueType) { this.valueType = valueType; }

  @Override
  public String toString() {                
    return name + "|" + parameterType + "|" + valueType;
  }
}
