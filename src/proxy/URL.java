package proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class URL {
  private String host;
  private int port;
  private List<String> headers;
  private String scheme;
  private String path;
  private String version;
  private String IP;

  public URL() {
    headers = new ArrayList<String>();
    host = "";
    port = 80;
    scheme = "GET";
    path = "/";
    version = "HTTP/1.1";
  }

  // 获取dns信息，从域名解析得到ip地址，并将dns缓存
  public String getIP() {
    java.security.Security.setProperty("networkaddress.cache.ttl", "30");
    try {
      this.IP = InetAddress.getByName(this.host).getHostAddress();
    } catch (UnknownHostException e) {
    }
    return this.IP;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getScheme() {
    return scheme;
  }

  public String getHeadId() {
    return scheme + getUrl() + version;
  }

  public String getUrl() {
    return path.startsWith("http://") ? path : host + path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getHeadersAsString() {
    StringBuilder sb = new StringBuilder();
    for (String header : headers) {
      sb.append(header).append("\r\n");
    }
    sb.append("\r\n");
    return sb.toString();
  }

  // 构造HTTP头
  private void CreateHeader(String header) {
    if (header.startsWith("GET") || header.startsWith("POST")) {
      CreateScheme(header);
    }
    if (header.startsWith("Host:")) {
      CreateHost(header);
    }
    if (!(header.startsWith("Connection:") || header.startsWith("Proxy-Connection:"))) {
      headers.add(header);
    } else {
      headers.add("Connection: close");
    }
  }

  private void CreateHost(String header) {
    String[] components = header.split(" ");
    String hostName = components[1];
    if (hostName.indexOf(":") > 0) {
      String[] hostComponents = hostName.split(":");
      host = hostComponents[0];
      port = Integer.parseInt(hostComponents[1]);
    } else {
      host = hostName;
    }
  }

  // 从输入流构造请求
  public void CreateRequest(InputStream inputStream) {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    try {
      String header = bufferedReader.readLine();
      while (header != null && header.length() != 0) {
        CreateHeader(header);
        header = bufferedReader.readLine();
      }
    } catch (IOException ex) {
    }
  }

  private void CreateScheme(String header) {
    String[] components = header.split(" ");
    scheme = components[0];
    path = components[1];
    version = components[2];
  }
}
