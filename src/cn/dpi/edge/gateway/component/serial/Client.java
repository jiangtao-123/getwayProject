package cn.dpi.edge.gateway.component.serial;

import java.io.IOException;

import javax.microedition.io.CommConnection;
import javax.microedition.io.Connector;

import cn.dpi.edge.gateway.api.AutoCloseable;
import cn.dpi.edge.gateway.component.api.ClientBase;
import cn.dpi.edge.gateway.utils.stringUtil;
import cn.dpi.edge.gateway.utils.threadUtil;

public class Client extends ClientBase implements AutoCloseable {

	public static void main(String args[]) throws Exception {
		Config config = new Config();
		config.Address = "COM0";
		config.BaudRate = 9600;
		Client client = Client.newClient(config);
		byte[] data = new byte[255];
		while (true) {
			client.write(new byte[] { 00, 11, 22, 33 });
			int size = client.read(data);
			System.out.println("size=" + size);
			if (size > 0) {
				System.out.println(stringUtil.format("receive --  {}", data, 0, size));
			}
		}
	}

	private CommConnection connection;
	private Config config;

	public static Client newClient(Config config) throws IOException {
		Client client = new Client();
		client.config = config;
		client.check();
		return client;
	}

	protected void check() throws IOException {
		if (connection == null || input == null || output == null) {
			this.close();
			String host = this.config.build();
			System.out.println("host=" + host);

			this.connection = (CommConnection) Connector.open(host);
			this.config.setConnection(connection);
			input = connection.openInputStream();
			output = connection.openOutputStream();
		}
	}

	protected String encoding() {
		return this.config.Encoding;
	}

	public void close() {
		super.close();
		threadUtil.close(connection);
	}

}
