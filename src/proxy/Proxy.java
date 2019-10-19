/**
 * 
 */
package proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Raymo
 *
 */
public class Proxy implements Runnable {

  private Socket socket;

  public Proxy(Socket socket) {
    this.socket = socket;
  }

  /*
   * （非 Javadoc）
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run() {
    // TODO 自动生成的方法存根
    InputStream c2p = null;
    InputStream s2p = null;
    OutputStream p2c = null;
    OutputStream p2s = null;

    Socket proxy = null;
    String firstLine = "";
//    String host = "";
    try {
      c2p = this.socket.getInputStream();
      p2c = this.socket.getOutputStream();

//      String ip = InetAddress.getLocalHost().getHostAddress();

      // parser the first line
      while (true) {
        int c = c2p.read();
        if (c == -1)
          break;
        if (c == '\n' || c == '\r')
          break;
        firstLine += (char) c;
      }

      String urls = extractUrl(firstLine);
      URL url = new URL(urls);

      proxy = new Socket(url.getHost(), 80);

      if (proxy != null) {
        p2s = proxy.getOutputStream();
        s2p = proxy.getInputStream();
        p2s.write(firstLine.getBytes());
        pipe(c2p, s2p, p2s, p2c);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        this.socket.close();
        c2p.close();
        p2c.close();
      } catch (IOException e) {
        // TODO 自动生成的 catch 块
        e.printStackTrace();
      }
      try {
        proxy.close();
        p2s.close();
        s2p.close();
      } catch (IOException e) {
        // TODO 自动生成的 catch 块
        e.printStackTrace();
      }
    }
  }

  public String extractUrl(String firstLine) {
    String[] words = firstLine.split(" ");
    String url = "";
    int len = words.length;
    for (int i = 0; i < len; i++) {
      if (words[i].startsWith("http://")) {
        url = words[i];
        break;
      }
    }
    return url;
  }

  public void pipe(InputStream c2p, InputStream s2p, OutputStream p2s, OutputStream p2c) {
    Transfer c2s = new Transfer(c2p, p2s);
    Transfer s2c = new Transfer(s2p, p2c);
    Thread cts = new Thread(c2s);
    Thread stc = new Thread(s2c);
    cts.start();
    stc.start();
    try {
      cts.join();
      stc.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
