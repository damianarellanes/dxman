package esp8266multilevel;

import com.dxman.execution.*;
import java.util.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class CompositeWfSeq2 extends DXManWfTreeSeq {
  
  public CompositeWfSeq2(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public void design() {  
        
    CompositeWfSeq1 seq1 = new CompositeWfSeq1("seq1", "coap://192.168.0.5:5683/SEQ1");
    CompositeWfPar0 par0 = new CompositeWfPar0("par0", "coap://192.168.0.5:5683/PAR0");
    
    composeWf(seq1, 0);
    composeWf(par0, 1);
  }
  
  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
    /*wfInputs.putAll(CompositeWfSeq1.getInputs());
    wfInputs.putAll(CompositeWfPar0.getInputs());*/
    return wfInputs;
  }
  
  @Override
  public DXManWfOutputs getOutputs() {    
    DXManWfOutputs wfOutputs = new DXManWfOutputs();
    /*wfOutputs.addAll(CompositeWfSeq1.getOutputs());
    wfOutputs.addAll(CompositeWfPar0.getOutputs());*/
    return wfOutputs;
  }    
}
