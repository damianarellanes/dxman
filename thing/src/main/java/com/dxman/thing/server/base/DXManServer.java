package com.dxman.thing.server.base;

import com.dxman.thing.deployment.common.DXManDeployer;
import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;

/**
 * @author Damian Arellanes
 */
public interface DXManServer { 
  
  public void start();
  public void initDeployerDispatcher(String resourceName, DXManDeployer deployer);
  public DXManConnectorDispatcher deploy(DXManConnectorInstance connectorInstance);
}
