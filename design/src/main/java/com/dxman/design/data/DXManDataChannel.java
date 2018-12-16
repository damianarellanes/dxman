package com.dxman.design.data;

/**
 * @author Damian Arellanes
 */
public class DXManDataChannel {
  
  private final DXManDataChannelPoint origin;
  private final DXManDataChannelPoint destination;

  public DXManDataChannel(DXManDataChannelPoint origin, 
    DXManDataChannelPoint destination) {
    this.origin = origin;
    this.destination = destination;
  }
  
  public DXManDataChannelPoint getOrigin() { return origin; } 
  public DXManDataChannelPoint getDestination() { return destination; }
 
  @Override
  public String toString() {
    return origin + "-->" + destination;
  }
}
