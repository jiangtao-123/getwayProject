package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class TCPTest2 {

	// IP
	private static String ip = "127.0.0.1";
	// 端口
	private static String port = "8009";

	private static StreamConnection streamConnection;
	private static InputStream inputStream;
	private static OutputStream outputStream;

	public static void main(String[] args) {

		System.out.println("    ============   Start Socket Demo...");
		String host = "socket://" + ip + ":" + port;
		try {

			// 建立连接
			streamConnection = (StreamConnection) Connector.open(host);

			inputStream = streamConnection.openInputStream();
			outputStream = streamConnection.openOutputStream();

			// 发送请求
			String request = "GET / HTTP/1.0\n\n";
			byte[] reqBytes = request.getBytes();
			sendData(reqBytes, 0, reqBytes.length);

			byte[] buffer = new byte[256];
			// 读数据
			int len = inputStream.read(buffer);
			for (int i = 0; i < len; i++) {
				System.out.print(Integer.toHexString(buffer[i]&0xFF));

				System.out.print("-");
			}
			System.out.println("");
			System.out.println("end");
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				closeStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 发送数据
	 * 
	 * @param buffer
	 * @param off
	 * @param len
	 * @throws IOException
	 */
	private static void sendData(byte[] buffer, int off, int len) throws IOException {

		if (outputStream != null) {

			outputStream.write(buffer, off, len);
		}
	}

	/**
	 * 关闭串口连接
	 * <p>
	 * 如果是阻塞读数据的时候，可以由外部进行关闭
	 * 
	 * @throws IOException
	 */
	private static void closeStream() throws IOException {

		if (inputStream != null) {

			inputStream.close();
		}

		if (outputStream != null) {

			outputStream.close();
		}

		if (streamConnection != null) {

			streamConnection.close();
		}
	}
}
