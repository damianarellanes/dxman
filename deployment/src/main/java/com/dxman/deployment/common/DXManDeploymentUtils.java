package com.dxman.deployment.common;

import com.dxman.design.connectors.adapters.DXManGuardTemplate;
import com.dxman.design.connectors.adapters.DXManLooperTemplate;
import com.dxman.execution.sequencer.DXManWfSequencerCustom;
import com.dxman.execution.sequencer.DXManWfSequencer;
import com.dxman.execution.parallel.DXManWfParallelCustom;
import com.dxman.execution.parallel.DXManWfParallel;
import com.dxman.execution.invocation.DXManWfInvocation;
import com.dxman.execution.common.DXManWfNodeCustom;
import com.dxman.execution.common.DXManWfNode;
import com.dxman.design.connectors.atomic.DXManInvocationTemplate;
import com.dxman.design.connectors.common.*;
import com.dxman.design.connectors.composition.*;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.guard.DXManWfGuard;
import com.dxman.execution.guard.DXManWfGuardCustom;
import com.dxman.execution.looper.DXManWfLooper;
import com.dxman.execution.looper.DXManWfLooperCustomDyn;
import com.dxman.execution.looper.DXManWfLooperCustomStatic;
import com.dxman.execution.selector.*;
import com.dxman.utils.RuntimeTypeAdapterFactory;
import com.google.gson.*;

/**
 * @author Damian Arellanes
 */
public class DXManDeploymentUtils {
  
  public static Gson buildSerializationGson() {
    
    return new GsonBuilder().disableHtmlEscaping()
      .registerTypeAdapterFactory(
        DXManDeploymentUtils.getWfNodeAdapterSerialization())
      .registerTypeAdapterFactory(
        DXManDeploymentUtils.getWfNodeCustomAdapterSerialization()
      )
      .create();
  }
  
  public static Gson buildDeserializationGson() {
    
    return new GsonBuilder().disableHtmlEscaping()
      .registerTypeAdapterFactory(
        DXManDeploymentUtils.getServiceAdapterDeserialization())
      .registerTypeAdapterFactory(
        DXManDeploymentUtils.getWfNodeAdapterDeserialization())
      .registerTypeAdapterFactory(
        DXManDeploymentUtils.getCompositionConnectorAdapterDeserialization())
      .create();
  }
    
  private static RuntimeTypeAdapterFactory<DXManServiceTemplate> 
    getServiceAdapterDeserialization() {
    
    return RuntimeTypeAdapterFactory
      .of(DXManServiceTemplate.class, "classTypeServ")
      .registerSubtype(DXManCompositeServiceTemplate.class, 
        DXManCompositeServiceTemplate.class.getName())
      .registerSubtype(DXManAtomicServiceTemplate.class, 
        DXManAtomicServiceTemplate.class.getName());
  }
    
  private static RuntimeTypeAdapterFactory<DXManWfNode> 
    getWfNodeAdapterDeserialization() {

    return RuntimeTypeAdapterFactory
    .of(DXManWfNode.class, "classTypeWfNode")
    .registerSubtype(DXManWfParallel.class, DXManWfParallel.class.getName())
    .registerSubtype(DXManWfSelector.class, DXManWfSelector.class.getName())
    .registerSubtype(DXManWfSequencer.class, DXManWfSequencer.class.getName())
    .registerSubtype(DXManWfInvocation.class, DXManWfInvocation.class.getName())
    .registerSubtype(DXManWfGuard.class, DXManWfGuard.class.getName())
    .registerSubtype(DXManWfLooper.class, DXManWfLooper.class.getName());
  }
      
  private static RuntimeTypeAdapterFactory<DXManWfNode> 
    getWfNodeAdapterSerialization() {

    return RuntimeTypeAdapterFactory
    .of(DXManWfNode.class)
    .registerSubtype(DXManWfParallel.class)
    .registerSubtype(DXManWfSelector.class)
    .registerSubtype(DXManWfSequencer.class)
    .registerSubtype(DXManWfInvocation.class)
    .registerSubtype(DXManWfGuard.class)
    .registerSubtype(DXManWfLooper.class);
  }
      
  private static RuntimeTypeAdapterFactory<DXManWfNodeCustom>
    getWfNodeCustomAdapterSerialization() {

    return RuntimeTypeAdapterFactory
    .of(DXManWfNodeCustom.class)
    .registerSubtype(DXManWfParallelCustom.class)
    .registerSubtype(DXManWfSelectorCustom.class)
    .registerSubtype(DXManWfSequencerCustom.class)
    .registerSubtype(DXManWfGuardCustom.class)
    .registerSubtype(DXManWfLooperCustomDyn.class)
    .registerSubtype(DXManWfLooperCustomStatic.class);
  }
      
  private static RuntimeTypeAdapterFactory<DXManConnectorTemplate> 
    getCompositionConnectorAdapterDeserialization() {

    return RuntimeTypeAdapterFactory
    .of(DXManConnectorTemplate.class, "classType")
    .registerSubtype(DXManParallelTemplate.class, 
      DXManConnectorType.PARALLEL.name())
    .registerSubtype(DXManSelectorTemplate.class, 
      DXManConnectorType.SELECTOR.name())
    .registerSubtype(DXManSequencerTemplate.class, 
      DXManConnectorType.SEQUENCER.name())
    .registerSubtype(DXManInvocationTemplate.class, 
      DXManConnectorType.INVOCATION.name())
    .registerSubtype(DXManGuardTemplate.class, 
      DXManConnectorType.GUARD.name())
    .registerSubtype(DXManLooperTemplate.class, 
      DXManConnectorType.LOOPER.name());
  }
}
