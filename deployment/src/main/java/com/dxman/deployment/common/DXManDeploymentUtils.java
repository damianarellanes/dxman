package com.dxman.deployment.common;

import com.dxman.design.connectors.common.DXManConnectorType;
import com.dxman.design.connectors.composition.*;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.*;
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
    .registerSubtype(DXManWfInvocation.class, DXManWfInvocation.class.getName());
  }
      
  private static RuntimeTypeAdapterFactory<DXManWfNode> 
    getWfNodeAdapterSerialization() {

    return RuntimeTypeAdapterFactory
    .of(DXManWfNode.class)
    .registerSubtype(DXManWfParallel.class)
    .registerSubtype(DXManWfSelector.class)
    .registerSubtype(DXManWfSequencer.class)
    .registerSubtype(DXManWfInvocation.class);
  }
      
  private static RuntimeTypeAdapterFactory<DXManWfNodeCustom>
    getWfNodeCustomAdapterSerialization() {

    return RuntimeTypeAdapterFactory
    .of(DXManWfNodeCustom.class)
    .registerSubtype(DXManWfParallelCustom.class)
    .registerSubtype(DXManWfSelectorCustom.class)
    .registerSubtype(DXManWfSequencerCustom.class);
  }
      
  private static RuntimeTypeAdapterFactory<DXManCompositionConnectorTemplate> 
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
