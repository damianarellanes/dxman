package com.dxman.autonomicsamples.smarthome;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class SmarthomeSensorEnergy extends Observable implements Runnable {
  
  @Override
  public void run() {
    
    TimerTask repeatedTask = new TimerTask() {
        public void run() {
          
          DecimalFormat df = new DecimalFormat("#.#####");
          double randomValue = 0.00020 * new Random().nextDouble();
          String energyCost = df.format(randomValue);
          
          JSONObject energyState = new JSONObject();  
          try {
            energyState.put("date", new SimpleDateFormat("MMMM dd, y").format(new Date()));
            energyState.put("energyProvider", "Eon");
            energyState.put("energyCost", String.valueOf(energyCost));
          } catch (JSONException ex) { System.out.println(ex); }
         
          setChanged();
          notifyObservers(energyState);
        }
    };
    Timer timer = new Timer("Timer");
     
    long delay = 1000L;
    long period = 1000L * 60L * 60L * 24L;
    timer.scheduleAtFixedRate(repeatedTask, delay, period);    
  }
}
