package cn.dpi.edge.gateway.component.log;

import java.io.IOException;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import cn.dpi.edge.gateway.api.AutoCloseable;
import cn.dpi.edge.gateway.api.ILog;
import cn.dpi.edge.gateway.component.api.ClientBase;
import cn.dpi.edge.gateway.utils.dateUtil;

public class FileLog extends ClientBase implements AutoCloseable, ILog {

	private FileConnection connection;
	private Config config;

	protected FileLog() {
	}

	public static FileLog newFileLog(Config config) {
		FileLog item = new FileLog();
		item.config = config;
		return item;
	}

	private void write(String level, String message) {
		try {
			String msg = dateUtil.formatDateTime(new Date(), true) + " " + level + " " + message + LF;
			this.write(msg);
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}
	}

	public void info(String message) {
		this.write("INFO", message);
	}

	public void debug(String message) {
		if (this.config.Debug) {
			this.write("DEBUG", message);
		}
	}

	private String fileName = null;

	protected void check() throws IOException {
		synchronized (this) {
			// 判断当前连接对象是为为当天
			String now = dateUtil.formatyyyyMMdd(new Date()) + ".log";
			if (fileName != now) {
				this.close();
				fileName = now;
			}
			if (connection == null || input == null || output == null) {
				String path = "file://" + this.config.FilePath + "/" + this.fileName;
				// System.out.println(path);
				this.connection = (FileConnection) Connector.open(path);
				this.config.setConnection(connection);
				input = connection.openInputStream();
				output = connection.openOutputStream(this.connection.fileSize());
			}
		}
	}

	protected String encoding() {
		return this.config.Encoding;
	}

}
