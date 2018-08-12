package esp8266multilevel;

import com.dxman.execution.*;
import java.util.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class CompositeWfSeq2 {
  
  private static final String SEQ2 = "SEQ2";
  
  public static DXManWfSpec getWf() throws JSONException {  
        
    // Defines a sequence using sub-workflows
    DXManWfSequencer seq2 = new DXManWfSequencer(
      "seq2", "coap://192.168.0.5:5683/" + SEQ2
    );
    seq2.getSubnodeMappers().add(
      new DXManWfNodeMapper(CompositeWfSeq1.getWf().getFlow(), new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(0)))
      )
    );
    seq2.getSubnodeMappers().add(
      new DXManWfNodeMapper(CompositeWfPar0.getWf().getFlow(), new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(1)))
      )
    );
    seq2.finishSequence();

    return new DXManWfSpec("wf2", seq2);
  }
  
  public static DXManWorkflowInputs getInputs() {
    DXManWorkflowInputs wfInputs = new DXManWorkflowInputs();
    wfInputs.putAll(CompositeWfSeq1.getInputs());
    wfInputs.putAll(CompositeWfPar0.getInputs());
    return wfInputs;
  }
  
  public static DXManWorkflowOutputs getOutputs() {    
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    wfOutputs.addAll(CompositeWfSeq1.getOutputs());
    wfOutputs.addAll(CompositeWfPar0.getOutputs());
    return wfOutputs;
  }    
}
