package com.dxman.design.data;

import com.dxman.utils.DXManIDGenerator;

/**
 * @author Damian Arellanes
 */
public class DXManParameter extends DXManDataEntity implements Cloneable {
          
  private DXManParameterType parameterType;
  private String valueType;

  public DXManParameter() {}

  public DXManParameter(String name, DXManParameterType parameterType, 
    String valueType) {
    super(name, DXManDataEntityType.PARAMETER);
    this.parameterType = parameterType;
    this.valueType = valueType;
  }
  
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
    return getName() + "|" + getParameterType() + "|" + getValueType() + "-->" 
      + getId();
  }
}
