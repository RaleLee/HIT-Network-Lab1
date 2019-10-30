package proxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.Timer;

public class Client {
  private final int port;
  private ServerSocket serverSocket;
  static Timer timer;

  public Client(int port) {
    this.port = port;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    System.out.println("代理服务器正在运行：127.0.0.1:" + port);
    while (true) {
      Socket client = null;
      try {
        client = serverSocket.accept();
      } catch (IOException e) {
        e.printStackTrace();
      }
      Connect connect = new Connect(client);
      connect.start();
      timer = new Timer(10000, new DelayActionListener(connect));
      timer.start();
    }
  }

  public static void main(String[] args) {
    try {
      int port = 12345;
      @SuppressWarnings("unused")
      Cache cache = new Cache(new Receive());
      Client client = new Client(port);
      client.run();
    } catch (ArrayIndexOutOfBoundsException ex) {
    } catch (NumberFormatException ex) {
    }
  }
}

//关于计时器的设置
class DelayActionListener implements ActionListener {
  private Connect connect;

  public DelayActionListener(Connect connect) {
    this.connect = connect;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Client.timer.stop();
    System.out.println("请求超时！自动将其关闭！");
    connect.stopTask();
  }
}
