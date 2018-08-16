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
  
  public ClientCompositeMultipleLeds(String id, String uri, DXManWfTree... subWorkflows) {
    super(id, uri, subWorkflows);
  }
  
  @Override
  public void design() {  
        
    // Defines the subnodes for the workflow tree
    DXManWfInvocation on1 = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfInvocation off1 = new DXManWfInvocation(
      "off", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfInvocation on2 = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED2_CONNECTOR
    );
    DXManWfInvocation off2 = new DXManWfInvocation(
      "off", "coap://192.168.0.5:5683/" + LED2_CONNECTOR
    );
    DXManWfInvocation on3 = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );
    DXManWfInvocation off3 = new DXManWfInvocation(
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
