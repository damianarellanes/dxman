package com.dxman.thing.runtime;

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

  public DXManThing(String alias, String ip, int port, DXManServer server) {

    id = DXManIDGenerator.generateHostId();
    this.alias = alias;
    this.ip = ip;
    this.port = port;
    this.server = server;
  }
  
  public String getId() { return id; }
  public String getAlias() { return alias; }
  public String getIp() { return ip; }
  public int getPort() { return port; }
  public DXManServer getServer() { return server; }
}
