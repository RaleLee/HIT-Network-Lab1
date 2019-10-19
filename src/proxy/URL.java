/**
 * 
 */
package proxy;

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
  private String port;

  public URL(String url) {
    int pos = url.indexOf("//");
    if (pos != -1) {
      this.http = url.substring(0, pos - 1);
    } else {
      this.http = "http"; // default http request
    }
    String tmp = url.substring(pos + 2);
    pos = tmp.indexOf('/');
    if (pos != -1) {
      this.res = tmp.substring(pos);
      tmp = tmp.substring(0, pos);
    }
    pos = tmp.indexOf(':');
    if (pos != -1) {
      this.port = tmp.substring(pos + 1);
      tmp = tmp.substring(0, pos);
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
  public String getPort() {
    return port;
  }

}
