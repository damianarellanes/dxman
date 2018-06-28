package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.design.distribution.DXManBindingInfo;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Damian Arellanes
 */
public class SocketInvocator implements InvocationHandler {

  @Override
  public String invokeJSON(DXManBindingInfo bindingInfo, String jsonRequest) {

    Socket clientSocket = null;
    DataInputStream fromClient = null;
    DataOutputStream toClient = null;
    try {

      clientSocket = new Socket(bindingInfo.getEndpoint().getHost(), 
        bindingInfo.getEndpoint().getPort());

      toClient = new DataOutputStream(clientSocket.getOutputStream());            

      PrintWriter pw = new PrintWriter(toClient);
      pw.println(jsonRequest);
      pw.flush();

      fromClient = new DataInputStream(clientSocket.getInputStream());
      BufferedReader in = new BufferedReader(new InputStreamReader(fromClient));

      return in.readLine();

    } catch (IOException ex) {            
      System.err.println(ex.toString());
    } finally {
      try {
        clientSocket.close();
        fromClient.close();
        toClient.close();
      } catch (IOException ex) {
        System.err.println(ex.toString());
      }
    }

    return null;
  }
}
