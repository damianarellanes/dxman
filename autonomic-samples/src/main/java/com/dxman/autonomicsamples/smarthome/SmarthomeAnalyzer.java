package com.dxman.autonomicsamples.smarthome;

import java.util.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class SmarthomeAnalyzer extends Observable implements Observer {
  
  private JSONObject contextModel = new JSONObject();
  
  public SmarthomeAnalyzer() {}
  
  @Override
  public void update(Observable o, Object contextModel) {
        
    try {
      this.contextModel = (JSONObject) contextModel;
      
      JSONObject yesterdayContextModel = SmarthomeKnowledge.yesterdayContextModel;
      
      
      if(yesterdayContextModel.length() == 0 || (
        !yesterdayContextModel.getString("userPresence").equals(this.contextModel.get("userPresence")) &&
        !yesterdayContextModel.getString("energyCost").equals(this.contextModel.get("energyCost")) &&
        !yesterdayContextModel.getString("threshold").equals(this.contextModel.get("threshold"))
        )) {        
        
        System.out.println("NEW PLAN!");
        setChanged();
        notifyObservers(contextModel);        
      } else {
        
        System.out.println("YESTERDAY'S PLAN!");
        setChanged();
        notifyObservers(SmarthomeKnowledge.yesterdayPlan);
      }
      
      SmarthomeKnowledge.yesterdayContextModel = this.contextModel;      
    } catch (JSONException ex) { System.out.println(ex); }
  }
}
