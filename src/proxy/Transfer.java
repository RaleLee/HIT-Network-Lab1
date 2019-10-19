/**
 * 
 */
package proxy;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Raymo
 *
 */
public class Transfer implements Runnable {

  private InputStream ins;
  private OutputStream ous;

  public Transfer(InputStream ins, OutputStream ous) {
    this.ins = ins;
    this.ous = ous;
  }

  /*
   * （非 Javadoc）
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run() {
    // TODO 自动生成的方法存根
    int length;
    byte bytes[] = new byte[1024];
    while (true) {
      try {
        if ((length = ins.read(bytes)) > 0) {
          ous.write(bytes, 0, length); // 把服务器返回的数据写回客户机
          ous.flush();
        } else if (length < 0)
          break;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
