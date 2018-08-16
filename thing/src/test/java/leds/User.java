package leds;

import com.dxman.execution.*;
import static leds.Config.CONNECTOR_CONFIGS;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class User {
  
  public static void main(String[] args) throws JSONException {
    
    //new WfTreeSeq0("seq0", CONNECTOR_CONFIGS.get("SEQ0").getUri()).execute(new DXManWfManager());
    //new WfTreePar0("par0", CONNECTOR_CONFIGS.get("PAR0").getUri()).execute(new DXManWfManager());
    
    WfTreeSeq1 wfSeq1 = new WfTreeSeq1(
      "seq1", CONNECTOR_CONFIGS.get("SEQ1").getUri(),
      new WfTreeSeq0("seq0", CONNECTOR_CONFIGS.get("SEQ0").getUri()),
      new WfTreePar0("par0", CONNECTOR_CONFIGS.get("PAR0").getUri())
    );
    
    wfSeq1.execute(new DXManWfManager());  
  }    
}
