package com.dxman.autonomicsamples.smarthome;

import java.util.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class SmarthomeMonitor extends Observable implements Runnable, Observer {
  
  private JSONObject userState = new JSONObject();
  private JSONObject energyState = new JSONObject();
  
  public SmarthomeMonitor() {}
  
  @Override
  public void update(Observable o, Object state) {
        
    if(o.getClass().getSimpleName().equals("SmarthomeSensorUser")) {
      this.userState = (JSONObject) state;
    } else {
      this.energyState = (JSONObject) state;
    }
  }
  
  @Override
  public void run() {
    
    TimerTask repeatedTask = new TimerTask() {
      public void run() {
        
        JSONObject contextModel = new JSONObject();
        try {

          String date = userState.getString("date");
          String userPresence = userState.getString("userAtHome").equalsIgnoreCase("yes") ? "1" : "0";
          String energyCost = energyState.getString("energyCost");
          String threshold = userState.getString("maxAmountAllowed");
          
          contextModel.put("day", date);
          contextModel.put("userPresence", userPresence);
          contextModel.put("energyCost", energyCost);
          contextModel.put("threshold", threshold);
          
          setChanged();
          notifyObservers(contextModel);
          
        } catch (JSONException ex) {}
      }
    };
    Timer timer = new Timer("Timer");
     
    long delay = 1000L;
    long period = 1000L*10;// * 60L * 60L * 24L;
    timer.scheduleAtFixedRate(repeatedTask, delay, period);
    
  }
}
