package test;

import com.robot.channel.proxy.client.Client;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class ClientTest {
    public static void main(String[] args) {
        Client client = new Client();
        boolean flag = client.connect("127.0.0.1", 19000);
        int i = 0;
        while (flag) {
            try {
                Channel c = client.getChannel();
                String data = "dataabc" + i++;
                ByteBuf buf = c.alloc().buffer(data.length());
                buf.writeBytes(buf);
                c.writeAndFlush(buf);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
