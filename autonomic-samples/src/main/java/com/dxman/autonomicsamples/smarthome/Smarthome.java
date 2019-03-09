package com.dxman.autonomicsamples.smarthome;

/**
 * @author Damian Arellanes
 */
public class Smarthome {

  public static void main(String[] args) {
    
    SmarthomeSensorUser userSensor = new SmarthomeSensorUser();
    SmarthomeSensorEnergy energySensor = new SmarthomeSensorEnergy();
    SmarthomeMonitor monitor = new SmarthomeMonitor();
    SmarthomeAnalyzer analyzer = new SmarthomeAnalyzer();
    SmarthomePlanner planner = new SmarthomePlanner();
    SmarthomeEffector effector = new SmarthomeEffector();
    SmarthomeExecutor executor = new SmarthomeExecutor(effector);
    
    userSensor.addObserver(monitor);
    energySensor.addObserver(monitor);
    monitor.addObserver(analyzer);
    analyzer.addObserver(planner);
    analyzer.addObserver(executor);
    planner.addObserver(executor);
    
    new Thread(userSensor).start();
    new Thread(energySensor).start();
    new Thread(monitor).start();
  }
}
