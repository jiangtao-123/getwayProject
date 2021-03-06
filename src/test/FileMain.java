package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import cn.dpi.edge.gateway.EdgeStudio;
import cn.dpi.edge.gateway.api.ILog;
import cn.dpi.edge.gateway.component.file.Client;
import cn.dpi.edge.gateway.component.file.Config;
import cn.dpi.edge.gateway.utils.streamUtil;
import cn.dpi.edge.gateway.utils.threadUtil;

public class FileMain {

	public static void mainx(String[] args) throws IOException {

		FileConnection a = (FileConnection) Connector.open("file:///Phone/am.txt");
		if (!a.exists()) {
			a.create();
			a.setReadable(true);
			a.setWritable(true);

		}
		byte[] d = "hello world\n".getBytes();

		OutputStream out = a.openOutputStream(a.fileSize());

		out.write(d);
		d = "hello world2 \n".getBytes();
		out.write(d);
		d = "hello world3 \n".getBytes();
		out.write(d, 0, d.length);

		byte[] e = new byte[512];

		InputStream in = a.openInputStream();

		int n = in.read(e);

		System.out.println(new String(e, 0, n));

		out.close();
		in.close();
		a.close();
	}

	public static void mainxxxxx(String[] args) throws IOException {
		Config config = new Config();
		config.FilePath = "/Phone/19800106.log";
		Client client = Client.newClient(config);
		while (true) {
			String line = client.readLine();
			System.out.println("line = " + line);
			
		}
	}

	public static void main(String[] args) throws IOException {
		byte[] data;
		String json;
		ILog log;// = new ConsoleLog();
		System.out.println("read app config");
		InputStream stream = EdgeStudio.class.getResourceAsStream("/MudbusConfig.json");
		data = streamUtil.readToEnd(stream);
		threadUtil.close(stream);
		json = new String(data, "UTF-8");
		System.out.println(json);
	}
}
