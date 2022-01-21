package cn.dpi.edge.gateway.component.file;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import cn.dpi.edge.gateway.api.AutoCloseable;
import cn.dpi.edge.gateway.component.api.ClientBase;
import cn.dpi.edge.gateway.utils.threadUtil;

public class Client extends ClientBase implements AutoCloseable {
	private FileConnection connection;
	private Config config;

	protected Client() {
	}

	public static Client newClient(Config config) throws IOException {
		Client client = new Client();
		client.config = config;
		client.check();
		return client;
	}

	protected void check() throws IOException {
		if (connection == null || input == null || output == null) {
			this.close();
			String host = "file://" + this.config.FilePath;
			this.connection = (FileConnection) Connector.open(host);
			this.config.setConnection(connection);
			input = connection.openInputStream();
			output = connection.openOutputStream(this.connection.fileSize());
		}
	}

	public void close() {
		super.close();
		threadUtil.close(connection);
		connection = null;
	}

	protected String encoding() {
		return this.config.Encoding;
	}
}
