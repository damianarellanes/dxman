package com.dxman.thing.server.coap;

import com.dxman.thing.deployment.common.DXManDeployer;
import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;
import com.dxman.thing.server.base.DXManServer;
import com.dxman.thing.server.base.DXManConnectorDispatcher;

/**
 * @author Damian Arellanes
 */
public class DXManCoapServer implements DXManServer {
  
  private final String alias;
  private final String ip;
  private final int port;

  public DXManCoapServer(String alias, String ip, int port) {
    this.alias = alias;
    this.ip = ip;
    this.port = port;
  }
  
  @Override
  public void start() {
    
    // Only starts the Coap server once
    try {      
      DXManCoapSingleton.get().start();      
      System.out.println(alias + " listening at " + "http://" + ip + ":" + port);
    } catch(Exception e) {
      System.out.println("The server is already running");
      System.exit(0);
    }
  }
  
  @Override
  public void initDeployerDispatcher(String resourceName, DXManDeployer deployer) {
    
    DXManCoapDeployerDispatcher d = new DXManCoapDeployerDispatcher(
      resourceName, deployer
    );
    DXManCoapSingleton.get().add(d);
    
    System.out.println("Deployer is ready: /" + resourceName);
  }

  @Override
  public DXManConnectorDispatcher deploy(DXManConnectorInstance connectorInstance) {

    String connectorResourceName = connectorInstance.getId();
    
    DXManCoapConnectorDispatcher d = new DXManCoapConnectorDispatcher(
      connectorInstance, connectorResourceName
    );
    
    DXManCoapSingleton.get().add(d);
    
    System.out.println("Connector " + connectorInstance.getName() 
      + " deployed: /" + connectorResourceName);

    return d;
  }
}
