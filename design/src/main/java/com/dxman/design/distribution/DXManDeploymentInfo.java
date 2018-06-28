package com.dxman.design.distribution;

/**
 * @author Damian Arellanes
 */
public class DXManDeploymentInfo {
  
  private String thingIp;
  private int thingPort;

  public DXManDeploymentInfo() {}

  public DXManDeploymentInfo(String thingIp, int thingPort) {
    this.thingIp = thingIp;
    this.thingPort = thingPort;
  }

  public String getThingIp() { return thingIp; }
  public void setThingIp(String thingIp) { this.thingIp = thingIp; }

  public int getThingPort() { return thingPort; }
  public void setThingPort(int thingPort) { this.thingPort = thingPort; }
}
