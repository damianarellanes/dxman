package esp8266composite;

import com.dxman.execution.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class ClientCompositeMultipleLeds extends DXManWfTreeSeq {
  
  private static final String LED1_CONNECTOR = "LedService1";  
  private static final String LED2_CONNECTOR = "LedService2";  
  private static final String LED3_CONNECTOR = "LedService3";
  private static final String SEQ_CONNECTOR = "MyComposite";
  
  public ClientCompositeMultipleLeds(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public void design() {  
        
    // Defines the subnodes for the workflow tree
    DXManWfTreeInv on1 = new DXManWfTreeInv(
      "on", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfTreeInv off1 = new DXManWfTreeInv(
      "off", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfTreeInv on2 = new DXManWfTreeInv(
      "on", "coap://192.168.0.5:5683/" + LED2_CONNECTOR
    );
    DXManWfTreeInv off2 = new DXManWfTreeInv(
      "off", "coap://192.168.0.5:5683/" + LED2_CONNECTOR
    );
    DXManWfTreeInv on3 = new DXManWfTreeInv(
      "on", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );
    DXManWfTreeInv off3 = new DXManWfTreeInv(
      "off", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );
    
    composeWf(off1, 0,1,2,3,4,5);
    composeWf(on1, 6,7);
    composeWf(off2, 8,9,10,11,12,21);
    composeWf(on3, 13,14,15,16,17,18,19,20);
  }

  @Override
  public DXManWfInputs getInputs() {
    return new DXManWfInputs();
  }

  @Override
  public DXManWfOutputs getOutputs() {
    return new DXManWfOutputs();
  }
  
  public static void main(String[] args) throws JSONException {    
    new ClientComposite("seq0", "coap://192.168.0.5:5683/MyComposite").execute(new DXManWfManager());
  }
}
