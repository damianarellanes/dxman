package com.dxman.autonomicsamples.smarthome;

import java.text.SimpleDateFormat;
import java.util.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class SmarthomeSensorUser extends Observable implements Runnable {
  
  @Override
  public void run() {
    
    TimerTask repeatedTask = new TimerTask() {
        public void run() {
          
          String date = new SimpleDateFormat("MMMM dd, y").format(new Date());
          System.out.println("Today is: " + date);

          String userAtHome = "";
          while(!userAtHome.equalsIgnoreCase("YES") && !userAtHome.equalsIgnoreCase("NO")) {

            System.out.print("Are you at home? ");
            userAtHome = new Scanner(System.in).next();
          }

          System.out.print("How much do you want to spend? ");
          String maxAmountAllowed = new Scanner(System.in).next();
          System.out.println("");

          System.out.println("------------------------------------------------");     
          JSONObject userState = new JSONObject();  
          try {
            userState.put("date", date);
            userState.put("userAtHome", userAtHome);
            userState.put("maxAmountAllowed", maxAmountAllowed);
          } catch (JSONException ex) { System.out.println(ex); }
          
          System.out.println("User preferences done!");
          System.out.println("------------------------------------------------");
         
          setChanged();
          notifyObservers(userState);
        }
    };
    Timer timer = new Timer("Timer");
     
    long delay = 1000L;
    long period = 1000L * 60L * 60L * 24L;
    timer.scheduleAtFixedRate(repeatedTask, delay, period);    
  }
}
