package cn.dpi.edge.gateway.modbus;

import cn.dpi.edge.gateway.component.serial.Config;

public class RTUConfig extends Config {
	/**
	 * 连接超时 ms
	 */
	public int Timeout = 10000;
	/**
	 * 收发超时 ms
	 */
	public int SoTimeout = 5000;
}
