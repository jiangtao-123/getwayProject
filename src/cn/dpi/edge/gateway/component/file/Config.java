package cn.dpi.edge.gateway.component.file;

import java.io.IOException;

import javax.microedition.io.file.FileConnection;

public class Config {

	public String FilePath;
	public String Encoding = "UTF-8";
	public boolean Readable = true;
	public boolean Writable = true;

	public void setConnection(FileConnection connection) throws IllegalArgumentException, IOException {
		if (connection == null) {
			throw new NullPointerException("connection is null");
		}

		if (!connection.exists()) {
			connection.create();
			connection.setReadable(Readable);
			connection.setWritable(Writable);
		}
	}
}
