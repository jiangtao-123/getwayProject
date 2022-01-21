package cn.dpi.edge.gateway.utils;

import java.io.IOException;
import java.io.InputStream;

public class streamUtil {
	public static String readLine(InputStream staream) throws IOException {
		int c;
		StringBuffer stringBuffer = new StringBuffer();
		do {
			c = staream.read();
			if (c < 0) {
				break;
			}
			if (c == '\r') {
				continue;
			}
			if (c == '\n') {
				break;
			}
			stringBuffer.append((char) c);
		} while (true);
		return stringBuffer.toString();

	}

	public static byte[] readToEnd(InputStream stream) throws IOException {
		return readToEnd(stream, 256);
	}

	public static byte[] readToEnd(InputStream stream, int cache) throws IOException {
		int total = 0;
		byte[] data = new byte[cache];
		do {
			int temp = stream.read(data, total, cache);
			if (temp < 0) {
				return arrayUtil.clone(data, 0, total);
			}
			total += temp;
			if (temp < cache) {
				return arrayUtil.clone(data, 0, total);
			}
			byte[] t = new byte[data.length + cache];
			arrayUtil.copy(data, 0, t, 0);
			data = t;
		} while (true);
	}

}
