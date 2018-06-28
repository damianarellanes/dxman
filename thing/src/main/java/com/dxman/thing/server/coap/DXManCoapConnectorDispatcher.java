package com.dxman.thing.server.coap;

import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import com.dxman.thing.server.base.DXManConnectorDispatcher;

/**
 * @author Damian Arellanes
 */
public class DXManCoapConnectorDispatcher extends CoapResource 
  implements DXManConnectorDispatcher {
    
  private final DXManConnectorInstance connectorInstance;

  public DXManCoapConnectorDispatcher(DXManConnectorInstance connectorInstance, 
    String connectorResourceName) {
    
    super(connectorResourceName);
    this.connectorInstance = connectorInstance;
  }

  @Override
  public void handlePOST(CoapExchange exchange) {

    exchange.accept();
    connectorInstance.init(exchange.getRequestText());
    exchange.respond(CoAP.ResponseCode.CREATED);
  }
}
