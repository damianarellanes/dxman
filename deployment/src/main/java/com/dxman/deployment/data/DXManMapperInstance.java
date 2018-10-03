package com.dxman.deployment.data;

import com.dxman.dataspace.base.*;
import com.dxman.design.data.*;
import java.net.URISyntaxException;
import java.util.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class DXManMapperInstance extends DXManDataProcessorInstance {

  public DXManMapperInstance(DXManDataProcessor template, 
    DXManDataSpace dataspace) throws URISyntaxException {
    super(template, dataspace);
  }

  @Override
  public void onMessage(String message) {

    try {
      
      JSONObject jsonEvent = new JSONObject(message);    
      String parameter = jsonEvent.getString("parameter");
      
      if(template.getWriterIdsTmp().containsKey(parameter)) {
      
        template.getInputs().add(jsonEvent.getString("newValue"));
        template.getWriterIdsTmp().remove(parameter);
        
        if(template.getWriterIdsTmp().isEmpty()) {
                  
          HashMap<String, String> mapping 
            = ((DXManDataMapper)template.getComputation()).map(template.getInputs());
          
          ArrayList<DXManDataParameter> parameters  = new ArrayList<>();
          mapping.forEach((key, value)-> {
            parameters.add(dataspace.createDataParameter(key, template.getWfId(), value));
          });
          
          dataspace.writeParameters(parameters);
          template.resetInputs();
          template.resetWriterIdsTmp();
        }
      }
      
    } catch (JSONException ex) { System.out.println(ex); }
  }    
}
