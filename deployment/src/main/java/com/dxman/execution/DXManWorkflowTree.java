package com.dxman.execution;

import com.dxman.design.data.DXManDataChannel;
import com.dxman.design.data.DXManDataChannelPoint;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.utils.*;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWorkflowTree extends  DXManMap<String, DXManWfNode> {
  
  private final DXManCompositeServiceTemplate compositeService;

  public DXManWorkflowTree(DXManCompositeServiceTemplate compositeService) {
    this.compositeService = compositeService;
  }
  
  public void customiseOrder(String parentKey, String childKey, int... order) {    
    
    get(parentKey).customiseOrder(childKey, new DXManWfSequencerCustom(order));        
    ((DXManWfSequencer)get(parentKey)).increaseSequenceBy(order.length);
  }
  
  public void addDataChannel(String parentKey, DXManDataChannelPoint origin, 
    DXManDataChannelPoint destination) {
    
    get(parentKey).getDataChannels().add(
      new DXManDataChannel(origin, destination)
    );
  }
  
  public void design() {
    designControl();
    designData();
  }
  
  public abstract void designControl();
  public abstract void designData();
  public abstract DXManWfInputs getInputs();
  public abstract DXManWfOutputs getOutputs();

  public DXManCompositeServiceTemplate getCompositeService() {
    return compositeService;
  }
}
