package proxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Receive {
  public byte[] receive;
  private int receiveSize;

  public Receive() {
    receive = new byte[100000];
  }

  // 从服务器输出流构造HTTP文件
  public void CreateHTTP(InputStream inputStream) {
    int totalBytesRead = 0;
    byte[] buffer = new byte[65535];
    try {
      int bytesRead = inputStream.read(buffer);
      while (bytesRead != -1) {
        for (int i = 0; i < bytesRead && totalBytesRead + i < 100000; i++) {
          receive[i + totalBytesRead] = buffer[i];
        }
        totalBytesRead += bytesRead;
        bytesRead = inputStream.read(buffer);
      }
    } catch (IOException ex) {
    }
    receiveSize = totalBytesRead;
  }

  public byte[] getReceive() {
    return Arrays.copyOf(receive, receiveSize);
  }
}
