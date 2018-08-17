package leds;

import org.javalite.http.Get;
import org.javalite.http.Http;

/**
 * @author Damian Arellanes
 */
public class Reset {
    
  public static void main(String[] args) {
    
    for(int i = 1; i <= 4; i++) {
      
      System.out.println("Switching off led" + (i-1));
      Get response = Http.get("http://localhost:808" + i + "/led-microservice/api/off");
      
      System.out.println(response.responseCode());
    }
  }
}
