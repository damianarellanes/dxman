package com.dxman.deployment.data;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.design.data.DXManDataProcessor;
import com.dxman.utils.DXManIDGenerator;
import java.net.*;
import java.nio.ByteBuffer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * @author Damian Arellanes
 */
public abstract class DXManDataProcessorInstance extends WebSocketClient {
  
  protected final String outputId;
  protected final DXManDataProcessor template;
  protected final DXManDataSpace dataspace;
  
  public DXManDataProcessorInstance(DXManDataProcessor template, 
    DXManDataSpace dataspace) throws URISyntaxException {
    
    super(new URI(dataspace.getEndpoint().replace("http", "ws")));
    
    outputId = DXManIDGenerator.generateParameterUUID(template.getId(), template.getWfId());
    this.template = template;
    this.dataspace = dataspace;
  }
    
  @Override
  public void onOpen(ServerHandshake handshakedata) {
    System.out.println(template.getName() + " is listening...");    
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println(template.getName() + " closed with exit code " + code 
      + "| additional info: " + reason);
  }
  
  @Override
  public void onMessage(ByteBuffer message) {}

  @Override
  public void onError(Exception ex) {
    System.err.println("An error occurred in " + template.getName() + ":" + ex);
  }
}
