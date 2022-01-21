package cn.dpi.edge.gateway.component.serial;

import java.io.IOException;

import javax.microedition.io.CommConnection;

public class Config {
	public String Address;
	public int BaudRate;
	public String Encoding = "UTF-8";
	public String DataBits;
	public String StopBits;
	public String Parity;
	public String Blocking;
	public String Autocts;

	public String build() {
		StringBuffer sb = new StringBuffer();
		sb.append("comm:" + this.Address);
		sb.append(";baudrate=" + this.BaudRate);
		if (DataBits != null && DataBits.length() > 0) {
			sb.append(";bitsperchar=" + this.DataBits);
		}
		if (StopBits != null && StopBits.length() > 0) {
			sb.append(";stopbits=" + this.StopBits);
		}
		if (Parity != null && Parity.length() > 0) {
			sb.append(";parity=" + this.Parity);
		}
		if (Blocking != null && Blocking.length() > 0) {
			sb.append(";blocking=" + this.Blocking);
		}
		if (Autocts != null && Autocts.length() > 0) {
			sb.append(";autorts=" + this.Autocts);
		}
		return sb.toString();
	}

	public void setConnection(CommConnection connection) throws IllegalArgumentException, IOException {
		if (connection == null) {
			throw new NullPointerException("connection is null");
		}
	}
}
