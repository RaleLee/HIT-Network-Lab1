/**
 * 
 */
package proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * To represent a url, including http, port, id
 * 
 * @author Raymo
 *
 */
public class URL {

  private String ip;
  private String http;
  private String res;
  private String host;
  private int port = 0;

  public URL(String url) {
    int pos = url.indexOf("//");
    String tmp;
    if (pos != -1) {
      this.http = url.substring(0, pos - 1);
      tmp = url.substring(pos + 2);
    } else {
      this.http = "https"; // default http request
      tmp = url.substring(pos + 1);
    }

    pos = tmp.indexOf('/');
    if (pos != -1) {
      this.res = tmp.substring(pos);
      tmp = tmp.substring(0, pos);
    }
    pos = tmp.indexOf(':');
    if (pos != -1) {
      this.port = Integer.valueOf(tmp.substring(pos + 1));
      tmp = tmp.substring(0, pos);
    } else {
      port = 80;
    }
    this.host = tmp;

  }

  /**
   * @return ip
   */
  public String getIp() {
    return ip;
  }

  /**
   * @return http
   */
  public String getHttp() {
    return http;
  }

  /**
   * @return res
   */
  public String getRes() {
    return res;
  }

  /**
   * @return host
   */
  public String getHost() {
    return host;
  }

  /**
   * @return port
   */
  public int getPort() {
    return port;
  }

  public String getIP() {
    java.security.Security.setProperty("networkaddress.cache.ttl", "30");
    try {
      this.ip = InetAddress.getByName(this.host).getHostAddress();
    } catch (UnknownHostException e) {
      return "";
    }
    return this.ip;
  }
}
