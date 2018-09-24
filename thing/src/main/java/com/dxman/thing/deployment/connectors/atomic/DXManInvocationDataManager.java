package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.dataspace.base.*;
import com.dxman.design.data.*;
import com.dxman.design.distribution.DXManBindingContent;
import com.dxman.thing.deployment.connectors.atomic.DXManDataUtil.PrecompiledOperation;
import com.dxman.utils.*;
import java.util.*;
import java.util.regex.Matcher;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class DXManInvocationDataManager {
  
  private final DXManDataSpace dataSpace;  
  private final DXManDataUtil dataUtil;
  private final String dSpaceParamType;
  
  public DXManInvocationDataManager(DXManDataSpace dataSpace, 
    DXManMap<String, DXManOperation> operations) {
    
    this.dataSpace = dataSpace;    
    dataUtil = new DXManDataUtil(operations, dataSpace.getDataEntityFactory());
    
    dSpaceParamType = dataSpace.getDataEntityFactory().createDataParameter("", "", "")
      .getType();
    
  }
  
  public String read(String workflowId, String workflowTimestamp, 
    DXManOperation operationToInvoke) {
    
    String request = operationToInvoke.getBindingInfo().getRequestTemplate();
    
    if(operationToInvoke.getInputs().size() > 0) {
    
      List<String> inputNames = new ArrayList<>();
      JSONArray inputRefs = new JSONArray();
      for(DXManParameter input: operationToInvoke.getInputs().values()) {

        inputNames.add(input.getName());
        inputRefs.put("resource:" + dSpaceParamType + "#" 
            + DXManIDGenerator.generateParameterUUID(input.getId(), workflowId));
      }
      
      // Gets the values for all inputs from the dataspace
      JSONArray values = dataSpace.readParameters(inputRefs, workflowTimestamp); 
      
      // Parses input values for the request
      for(int i = 0; i < values.length(); i++) {
        try {
            // Replaces input values in the request template
            request = request.replaceAll(
              DXManConfiguration.WILDCARD_START
              + inputNames.get(i) 
              + DXManConfiguration.WILDCARD_END,
              values.getString(i) 
          );
        } catch (JSONException ex) { System.err.println(ex); }
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

      List<DXManDataEntity> updates = new ArrayList<>();
      Matcher matcher = precompiledOp.getPattern().matcher(response);
      if(matcher.matches()) {

        for(int i = 1; i <= matcher.groupCount(); i++) {

          System.out.println(
            precompiledOp.getOutputIds()[i-1] + "-->" + matcher.group(i)
          );
          updates.add(
            dataSpace.getDataEntityFactory().createDataParameter(
              precompiledOp.getOutputIds()[i-1], workflowId, matcher.group(i)
            )
          );
        }

        dataSpace.writeDataEntities(updates);

      } else {
        //System.out.println("The JSON response does not match the template!");
      }
    }
  }
}
