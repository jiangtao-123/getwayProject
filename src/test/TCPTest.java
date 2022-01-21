package test;

import java.io.IOException;

import cn.dpi.edge.gateway.component.net.Client;
import cn.dpi.edge.gateway.component.net.Config;
import cn.dpi.edge.gateway.utils.stringUtil;
import cn.dpi.edge.gateway.utils.threadUtil;

public class TCPTest {

	public static void main(String[] args) {
		Client client = null;
		try {
			Config config = new Config();
			config.Address = "127.0.0.1:8009";
			// 建立连接
			client = Client.newClient(config);
			// while (true) {
			// 发送请求
			String request = "1234";
			byte[] reqBytes = request.getBytes();
			System.out.println(stringUtil.format("tcp: SEND {}", reqBytes));
			client.write(reqBytes);
			byte[] buffer = new byte[50];
			int len = client.read(buffer);
			System.out.println(stringUtil.format("tcp: receive {}", buffer, 0, len));

			// }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			threadUtil.close(client);
		}
	}
}
