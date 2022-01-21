package cn.dpi.edge.gateway.component.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.dpi.edge.gateway.api.AutoCloseable;
import cn.dpi.edge.gateway.bean.ModbusException;
import cn.dpi.edge.gateway.utils.streamUtil;
import cn.dpi.edge.gateway.utils.threadUtil;

public abstract class ClientBase implements AutoCloseable {
	protected InputStream input;
	protected OutputStream output;
	protected static final char LF = '\n';

	protected abstract void check() throws IOException;

	protected abstract String encoding();

	public int read(byte[] data, int offset, int length) throws IOException {
		this.check();
		return this.input.read(data, offset, length);
	}

	public int read(byte[] data) throws IOException {
		return this.read(data, 0, data.length);
	}

	public String readLine() throws IOException {
		return streamUtil.readLine(input);
	}

	public int readAtLeast(byte[] data, int start, int min) throws IOException {
		if ((data.length - start) < min) {
			throw ModbusException.responseError("data size small");
		}
		int size = -1;
		for (int i = 0; i < min; i++) {
			int val = this.input.read();
			if (val < 0) {
				break;
			}
			data[start + i] = (byte) (val & 0xFF);
			size++;
		}
		return size < 0 ? size : size + 1;
	}

	public void write(byte[] data, int offset, int length) throws IOException {
		this.check();
		this.output.write(data, offset, length);
	}

	public void write(byte[] data) throws IOException {
		this.write(data, 0, data.length);
	}

	public void write(String data) throws IOException {
		byte[] temp = data.getBytes(this.encoding());
		this.write(temp);
	}

	public void writeLine(String line) throws IOException {
		if (line == null) {
			line = "";
		}
		line = line + LF;
		byte[] temp = line.getBytes(this.encoding());
		this.write(temp);
	}

	public void flush() throws IOException {
		this.output.flush();
	}

	public void close() {
		threadUtil.close(input);
		threadUtil.close(output);
		this.input = null;
		this.output = null;
	}
}
