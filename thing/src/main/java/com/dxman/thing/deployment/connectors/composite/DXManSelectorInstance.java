package com.dxman.thing.deployment.connectors.composite;

import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.DXManWfNodeMapper;
import com.dxman.execution.selector.DXManWfCondition;
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
public class DXManSelectorInstance extends DXManConnectorInstance {
  
  private final DXManConnectorDataManager connectorDataManager;

  public DXManSelectorInstance(DXManCompositeServiceTemplate managedService, 
    DXManConnectorDataManager connectorDataManager, 
    DXManConnectorRequester requester, Gson gson) {
    
    super(managedService, managedService.getCompositionConnector().getName(), 
      requester, gson);
    
    this.connectorDataManager = connectorDataManager;
  }

  @Override
  public void activate(String workflowJSON) {
    
    System.err.println(this.getManagedService().getInfo().getName() 
      + " (Selector Connector) activated [" + new Date() + "]");
    
    DXManWfSelector flow = gson.fromJson(workflowJSON, DXManWfSelector.class);
    
    for(DXManWfNodeMapper subNodeMapper: flow.getSubnodeMappers()) {
      
      DXManWfCondition condition = ((DXManWfSelectorCustom)subNodeMapper
        .getCustom()).getCondition();
      System.out.println(condition);
      
      if(connectorDataManager.matches(flow.getWorkflowId(), condition)) {
        
        transferControl(
          subNodeMapper.getNode(), subNodeMapper.getNode().getUri()
        );
      }
    }
  }
    
}
