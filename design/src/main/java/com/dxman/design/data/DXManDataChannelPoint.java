package com.dxman.design.data;

/**
 * @author Damian Arellanes
 */
public class DXManDataChannelPoint {
    
  private final String dataEntityId;
  private final DXManDataEntityType dataEntityType;
  private final DXManDataProcessor dataProcessor;
  
  public DXManDataChannelPoint(String dataEntityId) {
    this.dataEntityId = dataEntityId;
    this.dataEntityType = DXManDataEntityType.PARAMETER;
    this.dataProcessor = null;
  }
  
  public DXManDataChannelPoint(DXManDataProcessor dataProcessor) {
    this.dataEntityId = dataProcessor.getId();
    this.dataEntityType = dataProcessor.getDataEntityType();
    this.dataProcessor = dataProcessor;
  }
  
  public boolean isDataProcessor() {
    return dataEntityType.equals(DXManDataEntityType.REDUCER) || 
      dataEntityType.equals(DXManDataEntityType.MAPPER);
  }

  public String getDataEntityId() { return dataEntityId; }
  public DXManDataEntityType getDataEntityType() { return dataEntityType; }
  public DXManDataProcessor getDataProcessor() { return dataProcessor; }

  @Override
  public String toString() {
    return dataEntityId;
  }
}
