package com.dxman.thing.deployment.connectors.adapters;

import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.common.DXManWfNodeMapper;
import com.dxman.execution.common.DXManWfCondition;
import com.dxman.execution.guard.DXManWfGuard;
import com.dxman.execution.guard.DXManWfGuardCustom;
import com.dxman.execution.selector.DXManWfSelector;
import com.dxman.execution.selector.DXManWfSelectorCustom;
import com.dxman.thing.deployment.connectors.common.DXManConnectorDataManager;
import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.google.gson.Gson;
import java.util.Date;

/**
 * @author Damian Arellanes
 */
public class DXManGuardInstance extends DXManConnectorInstance {
  
  private final DXManConnectorDataManager connectorDataManager;

  public DXManGuardInstance(String connectorId, String connectorName, 
    DXManConnectorDataManager connectorDataManager, 
    DXManConnectorRequester requester, Gson gson) {
    
    super(connectorId, connectorName, requester, gson);
    
    this.connectorDataManager = connectorDataManager;
  }

  @Override
  public void activate(String workflowJSON) {
    
    System.err.println(getName()
      + " (Guard Connector) activated [" + new Date() + "]");
    
    DXManWfGuard flow = gson.fromJson(workflowJSON, DXManWfGuard.class);
    
    DXManWfNodeMapper subNodeMapper = flow.getSubnodeMappers().get(0);
    DXManWfCondition condition = ((DXManWfGuardCustom)subNodeMapper
        .getCustom()).getCondition();
    
    if(connectorDataManager.matches(flow.getWorkflowId(), 
      flow.getWorkflowTimestamp(), condition)) {

      transferControl(
        subNodeMapper.getNode(), subNodeMapper.getNode().getUri()
      );
    }
  }
    
}
