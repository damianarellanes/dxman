package com.dxman.design.data;

import com.dxman.utils.DXManIDGenerator;

/**
 * @author Damian Arellanes
 */
public class DXManParameter implements Cloneable {
        
  private String id;
  private DXManDataEntityType dataEntityType;
  private String name;
  private DXManParameterType parameterType;
  private String valueType;

  public DXManParameter() {}

  public DXManParameter(String name, DXManParameterType parameterType, 
    String valueType) {        

    this.dataEntityType = DXManDataEntityType.PARAMETER;
    this.id = DXManIDGenerator.generateParameterID();
    this.name = name;
    this.parameterType = parameterType;
    this.valueType = valueType;
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  public DXManDataEntityType getDataEntityType() { return dataEntityType; }  
  public void setDataEntityType(DXManDataEntityType dataEntityType) {
    this.dataEntityType = dataEntityType;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public DXManParameterType getParameterType() { return parameterType; }
  public void setParameterType(DXManParameterType parameterType) { 
    this.parameterType = parameterType; 
  }

  public String getValueType() { return valueType; }
  public void setValueType(String valueType) { this.valueType = valueType; }
  
  @Override
  public DXManParameter clone() {
        
    Object clone = null;
    try { clone = super.clone(); } 
    catch(CloneNotSupportedException e) { System.err.println(e.toString()); }

    // Parameters are unique regarless they make reference to an existent one
    ((DXManParameter) clone).setId(DXManIDGenerator.generateParameterID());

    return (DXManParameter) clone;
  }
  
  @Override
  public String toString() {                
    return name + "|" + parameterType + "|" + valueType + "-->" + id;
  }
}
