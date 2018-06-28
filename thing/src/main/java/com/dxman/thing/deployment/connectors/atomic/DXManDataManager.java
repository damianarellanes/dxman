package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.design.data.DXManOperation;
import com.dxman.design.data.DXManParameter;
import com.dxman.design.distribution.DXManBindingContent;
import com.dxman.thing.deployment.connectors.atomic.DXManDataUtil.Precompiled;
import com.dxman.utils.DXManConfiguration;
import com.dxman.utils.DXManMap;
import java.util.regex.Matcher;

/**
 * @author Damian Arellanes
 */
public class DXManDataManager {
  
  private final String readerId;
  private final DXManDataSpace dataSpace;
  
  private final DXManDataUtil dataUtil;
  
  public DXManDataManager(String readerId, DXManDataSpace dataSpace, 
    DXManMap<String, DXManOperation> operations) {
    
    this.readerId = readerId;
    this.dataSpace = dataSpace;
    
    dataUtil = new DXManDataUtil(operations);
  }
  
  public String read(DXManOperation operationToInvoke) {
    
    String request = operationToInvoke.getBindingInfo().getRequestTemplate();
    
    // Parses input values for the request
    for(DXManParameter input: operationToInvoke.getInputs().values()) {

      // Gets the value from the dataspace
      String value = dataSpace.readParameter(input.getId(), readerId);

      // Replaces input values in the request template
      request = request.replaceAll(
        DXManConfiguration.WILDCARD_START 
          + input.getName() + DXManConfiguration.WILDCARD_END, 
        value
      );
    }
      
    return request;
  }
  
  public void write(DXManOperation operationToInvoke, String response) {
    
    if(operationToInvoke.getBindingInfo().getAcceptType()
      .equals(DXManBindingContent.NO_CONTENT)) return;
    
    // Fixes the response to avoid any issue when matching with the pattern
    response = response.replace("\n", "").replace("\r", "");
            
    Precompiled precompiled = dataUtil.getOperationPatterns()
      .get(operationToInvoke.getName());
    
    Matcher matcher = precompiled.getPattern().matcher(response);
    if(matcher.matches()) {
      
      for(int i = 1; i <= matcher.groupCount(); i++) {
        
        System.out.println(
          precompiled.getOutputIds()[i-1] + "-->" + matcher.group(i)
        );
        dataSpace.writeParameter(
          precompiled.getOutputIds()[i-1], matcher.group(i)
        );
      }
    } else {
      System.out.println("The JSON response does not match the template!");
    }
  }
}
