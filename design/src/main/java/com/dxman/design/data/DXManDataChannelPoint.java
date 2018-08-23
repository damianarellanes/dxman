package com.dxman.design.data;

/**
 * @author Damian Arellanes
 */
public class DXManDataChannelPoint {
    
  private final String parameterId;

  public DXManDataChannelPoint(String parameterId) {    
    this.parameterId = parameterId;
  }

  public String getParameterId() { return parameterId; }

  @Override
  public String toString() {
    return parameterId;
  }
}
