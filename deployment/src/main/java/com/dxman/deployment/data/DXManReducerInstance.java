package com.dxman.deployment.data;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.design.data.*;
import com.dxman.utils.DXManIDGenerator;
import java.net.URISyntaxException;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class DXManReducerInstance extends DXManDataProcessorInstance {

  public DXManReducerInstance(DXManDataProcessor template, 
    DXManDataSpace dataspace) throws URISyntaxException {
    super(template, dataspace);
  }

  @Override
  public void onMessage(String message) {
    
    //System.out.println("REDUCER:" + message);

    try {
      
      JSONObject jsonEvent = new JSONObject(message);    
      String updater = jsonEvent.getString("updater");
      
      if(template.getWriterIdsTmp().containsKey(updater)) {
      
        template.getInputs().add(jsonEvent.getString("newValue"));
        template.getWriterIdsTmp().remove(updater);
        
        if(template.getWriterIdsTmp().isEmpty()) {
          String result = ((DXManDataReducer)template.getComputation()).reduce(template.getInputs());
          dataspace.writeParameter(template.getId(), template.getWfId(), result,outputId);
          template.resetInputs();
          template.resetWriterIdsTmp();
        }
      }
      
    } catch (JSONException ex) { System.out.println(ex); }
  }    
}
