package esp8266composite;

import com.dxman.thing.runtime.*;

/**
 * @author Damian Arellanes
 */
public class Server {
    
  public static void main(String[] args) {
    
    DXManStarter.start(
      "/home/darellanes/DX-MAN-Platform/examples/alienware.properties"
    );
  }
}
