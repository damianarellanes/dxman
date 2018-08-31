package com.dxman.thing.server.coap;

import com.dxman.execution.common.DXManWfNode;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.google.gson.Gson;
import org.eclipse.californium.core.*;
import org.springframework.core.task.AsyncTaskExecutor;

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
    // TODO the timeout should be infinite
    // Sets a large timeout for the execution
    client.setTimeout(AsyncTaskExecutor.TIMEOUT_INDEFINITE);
        
    String json = gson.toJson(subworkflow);
    CoapResponse response = client.post(json, 0);

    // TODO do something with the response
  }
}
