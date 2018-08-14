package leds;

import com.dxman.execution.*;
import java.util.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class CompositeWfSeq0 {
  
  private static final String SEQ0 = "SEQ0";
  private static final String LED0_CONNECTOR = "IC0";
  private static final String LED1_CONNECTOR = "IC1";
  
  public static DXManWfSpec getWf() throws JSONException {  
        
    // Defines the subnodes for the workflow tree
    DXManWfInvocation on1 = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED0_CONNECTOR
    );
    DXManWfInvocation off1 = new DXManWfInvocation(
      "off", "coap://192.168.0.5:5683/" + LED0_CONNECTOR
    );
    DXManWfInvocation on2 = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfInvocation off2 = new DXManWfInvocation(
      "off", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    
    // Defines a sequence
    DXManWfSequencer seq0 = new DXManWfSequencer(
      "seq0", "coap://192.168.0.5:5683/" + SEQ0
    );    
    /*seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(on1, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(1)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(on2, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(0)))
      )
    );    
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(off2, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(2)))
      )
    );*/
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(on1, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(1,4,8,10,12,14)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(on2, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(0,5)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(off1, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(2,6,9,11,13)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(off2, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(3,7)))
      )
    );
    
    
    seq0.finishSequence();

    return new DXManWfSpec("wf0", seq0);
  }
  
  public static DXManWorkflowInputs getInputs() {
    return new DXManWorkflowInputs();    
  }
  
  public static DXManWorkflowOutputs getOutputs() {
    return new DXManWorkflowOutputs();
  }    
}
