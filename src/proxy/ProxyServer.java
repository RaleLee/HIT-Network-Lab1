/**
 * 
 */
package proxy;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Raymo
 *
 */
public class ProxyServer {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO 自动生成的方法存根
    try {
      ServerSocket serverSocket = new ServerSocket(10240);
      while (true) {
        try {
          Socket socket = serverSocket.accept();
          Thread proxy = new Thread(new Proxy(socket));
          proxy.start();
          System.out.println(Thread.activeCount());
        } catch (Exception e) {
          serverSocket.close();
        }
      }
    } catch (Exception e) {
      // TODO 自动生成的 catch 块
      e.printStackTrace();
      System.out.println("Proxy failed!");
    } finally {

    }
  }

}
