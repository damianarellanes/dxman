package leds;

import com.dxman.execution.*;
import static leds.Config.CONNECTOR_CONFIGS;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class User {
  
  public static void main(String[] args) throws JSONException {
    
    new DXManWfManager().executeWorkflow(
      new WfTreeSeq1("seq1", CONNECTOR_CONFIGS.get("SEQ1").getUri())
    );
  }    
}
