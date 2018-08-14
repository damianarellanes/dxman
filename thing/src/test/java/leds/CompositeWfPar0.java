package leds;

import com.dxman.execution.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class CompositeWfPar0 {
  
  private static final String PAR0 = "PAR0";
  private static final String LED2_CONNECTOR = "IC2";
  private static final String LED3_CONNECTOR = "IC3";
    
  public static DXManWfSpec getWf() throws JSONException {  
        
    // Defines the subnodes for the workflow tree
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
    
    // Defines a parallel workflow
    DXManWfParallel par0 = new DXManWfParallel("par0", "coap://192.168.0.5:5683/" + PAR0);
    par0.getSubnodeMappers().add(new DXManWfNodeMapper(on2, new DXManWfParallelCustom(3)));
    par0.getSubnodeMappers().add(new DXManWfNodeMapper(off2, new DXManWfParallelCustom(2)));
    par0.getSubnodeMappers().add(new DXManWfNodeMapper(on3, new DXManWfParallelCustom(1)));
        
    return new DXManWfSpec("wf3", par0);
  }
  
  public static DXManWorkflowInputs getInputs() {
    return new DXManWorkflowInputs();
  }
  
  public static DXManWorkflowOutputs getOutputs() {
    return new DXManWorkflowOutputs();
  }
}
