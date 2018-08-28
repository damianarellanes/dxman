package com.dxman.deployment.common;

import com.dxman.design.connectors.common.DXManConnectorType;
import com.dxman.design.connectors.composition.DXManCompositionConnectorTemplate;
import com.dxman.design.connectors.composition.DXManParallelTemplate;
import com.dxman.design.connectors.composition.DXManSelectorTemplate;
import com.dxman.design.connectors.composition.DXManSequencerTemplate;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.DXManWfInvocation;
import com.dxman.execution.DXManWfNode;
import com.dxman.execution.DXManWfNodeCustom;
import com.dxman.execution.DXManWfParallel;
import com.dxman.execution.DXManWfParallelCustom;
import com.dxman.execution.DXManWfSequencer;
import com.dxman.execution.DXManWfSequencerCustom;
import com.dxman.execution.selector.DXManWfSelector;
import com.dxman.execution.selector.DXManWfSelectorCustom;
import com.dxman.utils.RuntimeTypeAdapterFactory;

/**
 * @author Damian Arellanes
 */
public class DXManDeploymentUtils {
    
  public static RuntimeTypeAdapterFactory<DXManServiceTemplate> 
    getServiceAdapterDeserialization() {
    
    return RuntimeTypeAdapterFactory
      .of(DXManServiceTemplate.class, "classTypeServ")
      .registerSubtype(DXManCompositeServiceTemplate.class, 
        DXManCompositeServiceTemplate.class.getName())
      .registerSubtype(DXManAtomicServiceTemplate.class, 
        DXManAtomicServiceTemplate.class.getName());
  }
    
    public static RuntimeTypeAdapterFactory<DXManWfNode> 
      getWfNodeAdapterDeserialization() {
      
      return RuntimeTypeAdapterFactory
      .of(DXManWfNode.class, "classTypeWfNode")
      .registerSubtype(DXManWfParallel.class, DXManWfParallel.class.getName())
      .registerSubtype(DXManWfSelector.class, DXManWfSelector.class.getName())
      .registerSubtype(DXManWfSequencer.class, DXManWfSequencer.class.getName())
      .registerSubtype(DXManWfInvocation.class, DXManWfInvocation.class.getName());
    }
      
    public static RuntimeTypeAdapterFactory<DXManWfNode> 
      getWfNodeAdapterSerialization() {
      
      return RuntimeTypeAdapterFactory
      .of(DXManWfNode.class)
      .registerSubtype(DXManWfParallel.class)
      .registerSubtype(DXManWfSelector.class)
      .registerSubtype(DXManWfSequencer.class)
      .registerSubtype(DXManWfInvocation.class);
    }
      
    public static RuntimeTypeAdapterFactory<DXManWfNodeCustom>
      getWfNodeCustomAdapterSerialization() {
        
      return RuntimeTypeAdapterFactory
      .of(DXManWfNodeCustom.class)
      .registerSubtype(DXManWfParallelCustom.class)
      .registerSubtype(DXManWfSelectorCustom.class)
      .registerSubtype(DXManWfSequencerCustom.class);
    }
      
    public static RuntimeTypeAdapterFactory<DXManCompositionConnectorTemplate> 
      getCompositionConnectorAdapterDeserialization() {
        
      return RuntimeTypeAdapterFactory
      .of(DXManCompositionConnectorTemplate.class, "classType")
      .registerSubtype(DXManParallelTemplate.class, 
        DXManConnectorType.PARALLEL.name())
      .registerSubtype(DXManSelectorTemplate.class, 
          DXManConnectorType.SELECTOR.name())
      .registerSubtype(DXManSequencerTemplate.class, 
          DXManConnectorType.SEQUENCER.name());
    }
}
