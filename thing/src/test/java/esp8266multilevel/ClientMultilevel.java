package esp8266multilevel;

import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class ClientMultilevel {
  
  public static void main(String[] args) throws JSONException {
    
    DXManWfManager wfManager = new DXManWfManager();        
    
    CompositeWfSeq0 seq0 = new CompositeWfSeq0("seq0", "coap://192.168.0.5:5683/SEQ0");    
    CompositeWfSeq1 seq1 = new CompositeWfSeq1("seq1", "coap://192.168.0.5:5683/SEQ1");
    CompositeWfPar0 par0 = new CompositeWfPar0("par0", "coap://192.168.0.5:5683/PAR0");
    CompositeWfSeq1 seq2 = new CompositeWfSeq1("seq1", "coap://192.168.0.5:5683/SEQ1");
    
    wfManager.executeWorkflow(seq2).forEach((outputId, outputVal) -> {    
      System.out.println(outputId + " --> " + outputVal);
    });
  }    
}
