package com.dxman.design.data;

/**
 * @author Damian Arellanes
 */
public class DXManDataChannelPoint {
    
  private final String dataEntityId;
  private final DXManDataEntityType dataEntityType;

  public DXManDataChannelPoint(String dataEntityId, DXManDataEntityType dataEntityType) {
    this.dataEntityId = dataEntityId;
    this.dataEntityType = dataEntityType;
  }

  public String getDataEntityId() { return dataEntityId; }
  public DXManDataEntityType getDataEntityType() { return dataEntityType; }

  @Override
  public String toString() {
    return dataEntityId;
  }
}
