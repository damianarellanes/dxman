package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.dataspace.base.*;
import com.dxman.design.data.*;
import com.dxman.design.distribution.DXManBindingContent;
import com.dxman.thing.deployment.connectors.atomic.DXManDataUtil.PrecompiledOperation;
import com.dxman.utils.*;
import java.util.*;
import java.util.regex.Matcher;

/**
 * @author Damian Arellanes
 */
public class DXManInvocationDataManager {
  
  private final DXManDataSpace dataSpace;  
  private final DXManDataUtil dataUtil;
  
  public DXManInvocationDataManager(DXManMap<String, DXManOperation> operations, 
    DXManDataSpace dataspace) {    
    this.dataSpace = dataspace;
    dataUtil = new DXManDataUtil(operations);    
  }
  
  public String read(String workflowId, String workflowTimestamp, 
    DXManOperation operationToInvoke) {
    
    String request = operationToInvoke.getBindingInfo().getRequestTemplate();
    
    if(operationToInvoke.getInputs().size() > 0) {
      
      // Gets the values for all inputs from the dataspace
      DXManReadResult result = dataSpace.readParameters(
        operationToInvoke.getInputs().values(), workflowId, workflowTimestamp
      ); 
      
      // Parses input values for the request
      for(int i = 0; i < result.getValues().length(); i++) {
        // Replaces input values in the request template
        request = request.replaceAll(
          DXManConfiguration.WILDCARD_START
          + result.getInputNames().get(i) 
          + DXManConfiguration.WILDCARD_END,
          result.getValue(i));
      }
    }
      
    return request;
  }
  
  public void write(String workflowId, DXManOperation operationToInvoke, 
    String response) {    
    
    if(operationToInvoke.getBindingInfo().getAcceptType()
      .equals(DXManBindingContent.NO_CONTENT)) return;
    
    PrecompiledOperation precompiledOp = dataUtil.getPrecompiledOperations()
      .get(operationToInvoke.getId());
    
    if(precompiledOp.getOutputIds().length > 0) {
      
      // Fixes the response to avoid any issue when matching with the pattern
      response = response.replace("\n", "").replace("\r", "");

      List<DXManDataParameter> updates = new ArrayList<>();
      Matcher matcher = precompiledOp.getPattern().matcher(response);
      if(matcher.matches()) {

        for(int i = 1; i <= matcher.groupCount(); i++) {

          /*System.out.println(
            precompiledOp.getOutputIds()[i-1] + "-->" + matcher.group(i)
          );*/
          updates.add(
            dataSpace.createDataParameter(
              precompiledOp.getOutputIds()[i-1], workflowId, matcher.group(i),
              DXManIDGenerator.generateParameterUUID(precompiledOp.getOutputIds()[i-1], workflowId)
            )
          );
        }

        dataSpace.writeParameters(updates);

      } else {
        //System.out.println("The JSON response does not match the template!");
      }
    }
  }
}
