package cn.dpi.edge.gateway.modbus;

import java.io.IOException;

import cn.dpi.edge.gateway.api.ILog;
import cn.dpi.edge.gateway.api.ITransport;
import cn.dpi.edge.gateway.bean.ModbusException;
import cn.dpi.edge.gateway.bean.States;
import cn.dpi.edge.gateway.component.log.EmptyLog;
import cn.dpi.edge.gateway.component.net.Client;
import cn.dpi.edge.gateway.utils.arrayUtil;
import cn.dpi.edge.gateway.utils.stringUtil;
import cn.dpi.edge.gateway.utils.threadUtil;

public class TCPTransport implements ITransport {
	private final TCPConfig config;
	private volatile States state = States.Normal;
	private ILog log;

	private Client client;

	private TCPTransport(TCPConfig config, ILog log) {
		this.config = config;
		if (log == null) {
			this.log = new EmptyLog();
		} else {
			this.log = log;
		}
	}

	public static ITransport newTCPTransport(TCPConfig config, ILog log) {
		return new TCPTransport(config, log);
	}

	public void close() throws Exception {
		if (this.state == States.Stoping || this.state == States.Stoped) {
			return;
		}
		synchronized (this) {
			if (this.state == States.Stoping || this.state == States.Stoped) {
				return;
			}
			this.state = States.Starting;
			threadUtil.close(client);
			this.state = States.Stoped;
		}
	}

	public void connect() throws Exception {
//		System.out.println("connecttcp>>>>>");
		if (this.state == States.Starting || this.state == States.Started) {
			return;
		}
		synchronized (this) {
			if (this.state == States.Starting || this.state == States.Started) {
				return;
			}
			this.state = States.Starting;
			try {
				this.check();
				this.state = States.Started;
			} catch (Exception e) {
				e.printStackTrace();
				this.state = States.Stoped;
			}
		}
	}

	private void check() throws IOException {
		if (this.client == null) {
			this.client = Client.newClient(config);
		}
	}

	public byte[] send(byte[] data) throws Exception {
		synchronized (this) {
			this.check();
			// Set timer to close when idle
			// Set write and read timeout
			// Send data
			log.debug(stringUtil.format("tcp: sending {}", data));
			this.client.write(data);
			// Read header first
			byte[] rcv = new byte[TCPCodec.tcpMaxLength];
			int size = this.client.read(rcv, 0, TCPCodec.tcpMaxLength);
			if (size <= 0) {
				throw new RuntimeException("read size=" + size);
			}
			log.debug(stringUtil.format("tcp: receive {}", rcv, 0, size));
			// Read length, ignore transaction & protocol id (4 bytes)
			int length = (rcv[4] << 8) | rcv[5];
			if (length <= 0) {
				this.client.read(rcv);
				throw ModbusException.responseError("tcp: length in response header '" + length + "' must not be zero");
			}
			if (length > TCPCodec.tcpMaxBufferLength) {
				this.client.read(rcv);
				throw ModbusException.responseError("tcp: length in response header '" + length
						+ "' must not greater than '" + TCPCodec.tcpMaxBufferLength + "'");
			}
			// Skip unit id
			length = length + TCPCodec.tcpHeaderSize - 1;
			byte[] aduResponse = new byte[length];
			arrayUtil.copy(rcv, 0, aduResponse, 0, length);
			return aduResponse;
		}
	}

	public States state() throws Exception {
		return state;
	}

}
