package cn.dpi.edge.gateway.component.net;

import java.io.IOException;

import javax.microedition.io.SocketConnection;

public class Config {
	public String Address;
	public String Encoding = "UTF-8";
	public int Delay = 1;
	public int Linger = 1;
	public int Keepalive = 1;
	public int ReceiveBufferLength;
	public int SendBufferLength;

	public void setConnection(SocketConnection connection) throws IllegalArgumentException, IOException {
		if (connection == null) {
			throw new NullPointerException("connection is null");
		}
		if (Delay > 0) {
			connection.setSocketOption(SocketConnection.DELAY, Delay);
		}
		if (Linger > 0) {
			connection.setSocketOption(SocketConnection.LINGER, Linger);
		}
		if (Keepalive > 0) {
			connection.setSocketOption(SocketConnection.KEEPALIVE, Keepalive);
		}
		if (ReceiveBufferLength > 0) {
			connection.setSocketOption(SocketConnection.RCVBUF, ReceiveBufferLength);
		}
		if (SendBufferLength > 0) {
			connection.setSocketOption(SocketConnection.SNDBUF, SendBufferLength);
		}
	}
}
