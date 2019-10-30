package proxy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Connect extends Thread {
  private Socket client;
  private List<String> BanList = new ArrayList<String>();
  private List<String> FishList = new ArrayList<String>();
  private List<String> UserList = new ArrayList<String>();
  private String finalUrl = "http://nga.178.com";
  private String Request = "HTTP/1.1 302 Moved Temporarily\r\n";

  // 判断URL是否过滤
  public boolean NonUrlBan(String url) {
    for (int i = 0; i < BanList.size(); i++) {
      if (url.contains(BanList.get(i)) || url.matches(BanList.get(i))) {
        return false;
      }
    }
    return true;
  }

  // 钓鱼响应报文首部
  public String getFirst() {
    String first = new String();
    first = "Location: " + getFinalUrl() + "\r\n\r\n";
    return first;
  }

  // 判断是否钓鱼
  public boolean NonFish(String url) {
    for (int i = 0; i < this.FishList.size(); i++) {
      if (url.contains(this.FishList.get(i)) || url.matches(this.FishList.get(i))) {
        return false;
      }
    }
    return true;
  }

  // 判断是否用户过滤
  public boolean NonUserBan(String ip) {
    try {
      InetAddress address = InetAddress.getLocalHost();
      for (int i = 0; i < UserList.size(); i++) {
        if (ip.contains(UserList.get(i)) || address.equals(UserList.get(i))) {
          return false;
        }
      }
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return true;
  }

  public String getFinalUrl() {
    return finalUrl;
  }

  public String getRequest() {
    return Request;
  }

  public Connect(Socket client) {
    this.client = client;
    BanList.add("jwes.hit.edu.cn");
    // UserList.add("127.0.0.1");
    FishList.add("lib.hit.edu.cn");
  }

  @Override
  public void run() {
    URL url = new URL();
    Receive rb = new Receive();
    Request request = new Request(url, rb);
    try {
      request.url.CreateRequest(client.getInputStream());
    } catch (IOException e1) {
    }
    InetAddress addr = client.getInetAddress();
    if (this.NonUserBan(addr.getHostAddress())) {
      // 判断是否过滤该url
      if (this.NonUrlBan(url.getUrl()) && url.getUrl().length() < 100) {
        // 判断是否钓鱼
        if (this.NonFish(url.getUrl())) {
          System.out.println("收到链接：" + request.url.getUrl());

          Receive receive = null;
          try {
            receive = CacheReceive(request);
          } catch (IOException e) {
          }
          try {
            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
            outputStream.write(receive.getReceive());
          } catch (IOException e1) {
          }
        } else {
          url.setPath(this.getFinalUrl());
          request = new Request(url, rb);
          System.out.println("*********该链接已被钓鱼到：" + request.url.getUrl());

          OutputStream OutputOfClient;
          try {
            OutputOfClient = client.getOutputStream();
            OutputOfClient.write(this.getRequest().getBytes());
            OutputOfClient.write(this.getFirst().getBytes());
            OutputOfClient.flush();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } else {
        System.out.println("该URL：" + url.getUrl() + "禁止访问！");

      }
    } else {
      System.out.println("该IP：" + addr.getHostAddress() + "被屏蔽！");
    }
    try {
      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Receive CacheReceive(Request request) throws IOException {
    Receive receive = null;
    // System.out.println("New here?");
    if (Cache.isCached(request)) {
      System.out.println("Cached!");
      if (Cache.isNew(request)) {
        receive = Cache.get(request);
        System.out.println("从Cache中取出：" + request.url.getUrl());
      } else {
        receive = request.CreateReceive();
        Cache.add(request, receive);
        System.out.println("Cache中存在，但不是最新，已经获取最新：" + request.url.getUrl());
      }
    } else {
      receive = request.CreateReceive();
      Cache.add(request, receive);
      System.out.println("Cache中没有此记录！已生成Cache文件：" + Cache.hashStr(request));
    }
    return receive;
  }

  public void stopTask() {
    try {
      if (client != null) {
        client.close();
      }
    } catch (IOException e) {
    }
  }
}
