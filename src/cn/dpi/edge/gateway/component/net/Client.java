package cn.dpi.edge.gateway.component.net;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import cn.dpi.edge.gateway.api.AutoCloseable;
import cn.dpi.edge.gateway.component.api.ClientBase;
import cn.dpi.edge.gateway.utils.threadUtil;

public class Client extends ClientBase implements AutoCloseable {

	private SocketConnection connection;
	private Config config;

	private Client() {
	}

	public static Client newClient(Config config) throws IOException {
		Client client = new Client();
		client.config = config;
		client.check();
		return client;
	}

	protected void check() throws IOException {
		if (this.connection == null || this.input == null || this.output == null) {
			this.close();
			String host = "socket://" + this.config.Address;
			System.out.println("start>>>>>>>>>connection");
			this.connection = (SocketConnection) Connector.open(host);
			this.config.setConnection(connection);
			this.input = connection.openInputStream();
			this.output = connection.openOutputStream();
		}
	}

	public void close() {
		super.close();
		threadUtil.close(connection);
	}

	protected String encoding() {
		return this.config.Encoding;
	}
}
