package com.dxman.thing.runtime;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.thing.server.base.DXManServer;
import com.dxman.utils.DXManIDGenerator;

/**
 * @author Damian Arellanes
 */
public class DXManThing {

  private final String id;
  private final String alias;
  private final String ip;
  private final int port;
  
  private final DXManServer server;
  private final DXManDataSpace dataspace;

  public DXManThing(String alias, String ip, int port, DXManServer server, 
    DXManDataSpace dataspace) {

    id = DXManIDGenerator.generateHostId();
    this.alias = alias;
    this.ip = ip;
    this.port = port;
    this.server = server;
    this.dataspace = dataspace;
  }
  
  public void start() {
    server.start();
  }
  
  public void shutdown() {
    // TODO shutdown stuff, e.g., dataspace
  }
  
  public String getId() { return id; }
  public String getAlias() { return alias; }
  public String getIp() { return ip; }
  public int getPort() { return port; }
  public DXManServer getServer() { return server; }
  public DXManDataSpace getDataspace() { return dataspace; }
}
