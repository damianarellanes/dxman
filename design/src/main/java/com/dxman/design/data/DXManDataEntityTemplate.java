package com.dxman.design.data;

import com.dxman.utils.DXManIDGenerator;

/**
 * @author Damian Arellanes
 */
public class DXManDataEntityTemplate {
    
  private String id;
  private DXManDataEntityType dataEntityType;
  private String name;
  
  public DXManDataEntityTemplate() {}

  public DXManDataEntityTemplate(String name, DXManDataEntityType dataEntityType) {
    this.id = DXManIDGenerator.generateParameterID();
    this.name = name;
    this.dataEntityType = dataEntityType;
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  public DXManDataEntityType getDataEntityType() { return dataEntityType; }  
  public void setDataEntityType(DXManDataEntityType dataEntityType) {
    this.dataEntityType = dataEntityType;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
}
