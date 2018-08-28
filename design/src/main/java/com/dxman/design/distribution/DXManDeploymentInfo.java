package com.dxman.design.distribution;

/**
 * @author Damian Arellanes
 */
public class DXManDeploymentInfo {

  private String thingAlias;
  private String thingIp;
  private int thingPort;

  public DXManDeploymentInfo() {}

  public DXManDeploymentInfo(String thingAlias, String thingIp, int thingPort) {
    this.thingAlias = thingAlias;
    this.thingIp = thingIp;
    this.thingPort = thingPort;
  }
  
  public String getThingAlias() { return thingAlias; }
  public void setThingAlias(String thingAlias) { this.thingAlias = thingAlias; }

  public String getThingIp() { return thingIp; }
  public void setThingIp(String thingIp) { this.thingIp = thingIp; }

  public int getThingPort() { return thingPort; }
  public void setThingPort(int thingPort) { this.thingPort = thingPort; }
}
