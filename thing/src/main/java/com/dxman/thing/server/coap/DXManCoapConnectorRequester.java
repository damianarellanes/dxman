package com.dxman.thing.server.coap;

import com.dxman.execution.common.DXManWfNode;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.google.gson.Gson;
import org.eclipse.californium.core.*;

/**
 * @author Damian Arellanes
 */
public class DXManCoapConnectorRequester implements DXManConnectorRequester {
  
  private final Gson gson;
  
  public DXManCoapConnectorRequester(Gson gson) {    
    this.gson = gson;
  }

  @Override
  public void transferControl(DXManWfNode subworkflow, String uri) {
    
    CoapClient client = new CoapClient(uri);
        
    String json = gson.toJson(subworkflow);
    CoapResponse response = client.post(json, 0);

    // TODO do something with the response
  }
}
