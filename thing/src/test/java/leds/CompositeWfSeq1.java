package leds;

import com.dxman.execution.*;
import java.util.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class CompositeWfSeq1 {
  
  private static final String SEQ1 = "SEQ1";
  private static final String SEQ0_CONNECTOR = "SEQ0";
  private static final String PAR0_CONNECTOR = "PAR0";
  
  public static DXManWfSpec getWf() throws JSONException {  
        
    // Defines the subnodes for the workflow tree
    DXManWfSequencer seq0 = new DXManWfSequencer(
      "seq0", "coap://192.168.0.5:5683/" + SEQ0_CONNECTOR
    );
    DXManWfSequencer par0 = new DXManWfSequencer(
      "par0", "coap://192.168.0.5:5683/" + PAR0_CONNECTOR
    );
    
    // Defines a sequence
    DXManWfSequencer seq1 = new DXManWfSequencer(
      "seq1", "coap://192.168.0.5:5683/" + SEQ1
    );
    seq1.getSubnodeMappers().add(
      new DXManWfNodeMapper(CompositeWfSeq0.getWf().getFlow(), new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(0)))
      )
    );
    seq1.getSubnodeMappers().add(
      new DXManWfNodeMapper(CompositeWfPar0.getWf().getFlow(), new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(1)))
      )
    );    
    seq1.finishSequence();

    return new DXManWfSpec("wf1", seq1);
  }
  
  public static DXManWorkflowInputs getInputs() {
    return new DXManWorkflowInputs();
  }
  
  public static DXManWorkflowOutputs getOutputs() {
    return new DXManWorkflowOutputs();
  }    
}
