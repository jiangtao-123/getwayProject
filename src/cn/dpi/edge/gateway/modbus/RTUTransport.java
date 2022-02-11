package cn.dpi.edge.gateway.modbus;

import java.io.IOException;

import cn.dpi.edge.gateway.api.ILog;
import cn.dpi.edge.gateway.api.ITransport;
import cn.dpi.edge.gateway.bean.ModbusFunctionCode;
import cn.dpi.edge.gateway.bean.States;
import cn.dpi.edge.gateway.component.log.EmptyLog;
import cn.dpi.edge.gateway.component.serial.Client;
import cn.dpi.edge.gateway.utils.arrayUtil;
import cn.dpi.edge.gateway.utils.stringUtil;
import cn.dpi.edge.gateway.utils.threadUtil;

public class RTUTransport implements ITransport {

	private final RTUConfig config;
	private volatile States state = States.Normal;
	private ILog log;
	private Client client;

	private RTUTransport(RTUConfig config, ILog log) {
		this.config = config;
		if (log == null) {
			this.log = new EmptyLog();
		} else {
			this.log = log;
		}
	}

	public static ITransport newRTUTTransport(RTUConfig config, ILog log) {
		return new RTUTransport(config, log);
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

	private void check() throws IOException {
		if (this.client == null) {
			this.client = Client.newClient(config);
		}
	}

	public void connect() throws Exception {
//		System.out.println("com>>>>>>connect+rtu");
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

	private int calculateDelay(int chars) {
		int characterDelay, frameDelay; // us
		if (config.BaudRate <= 0 || config.BaudRate > 19200) {
			characterDelay = 750;
			frameDelay = 1750;
		} else {
			characterDelay = 15000000 / config.BaudRate;
			frameDelay = 35000000 / config.BaudRate;
		}
		return characterDelay * chars + frameDelay;
	}

	private int calculateResponseLength(byte[] adu) {
		byte code = adu[1];
		int count = ((adu[4] & 0xFF) << 8) | (adu[5] & 0xFF);
		int size = 0;
		switch (code) {
		case ModbusFunctionCode._ReadCoils:
		case ModbusFunctionCode._ReadDiscreteInputs:
			// 1 bit
			size = 1 + (count - 1) / 8 + 1;
			break;
		case ModbusFunctionCode._ReadInputRegisters:
		case ModbusFunctionCode._ReadHoldingRegisters:
			// 2 B
			size = 1 + count * 2;
			break;
		case ModbusFunctionCode._WriteCoil:
		case ModbusFunctionCode._WriteCoils:
		case ModbusFunctionCode._WriteRegister:
		case ModbusFunctionCode._WriteRegisters:
			size = 4;
			break;
		}
		return RTUCodec.rtuMinSize + size;
	}

	public byte[] send(byte[] data) throws Exception {
		synchronized (this) {
			this.check();
			// Set timer to close when idle
			// Set write and read timeout
			// Send data
			log.debug(stringUtil.format("modbus: sending {}", data));
			this.client.write(data);

			byte function = data[1];
			int functionFail = data[1] & 0x80;
			int bytesToRead = calculateResponseLength(data);
			// int millis = calculateDelay(data.length + bytesToRead);
			// System.out.println("thread sleep millis = " + millis);
			// threadUtil.sleep(millis);

			int n, n1 = 0;
			// Read header first
			byte[] rcv = new byte[RTUCodec.rtuMaxSize];
			// We first read the minimum length and then read either the full
			// package
			// or the error package, depending on the error status (byte 2 of
			// the response)
			n = this.client.readAtLeast(rcv, 0, RTUCodec.rtuMinSize);
			if (n <= 0) {
				throw new RuntimeException("read size=" + n);
			}
			// if the function is correct
			if (rcv[1] == function) {
				// we read the rest of the bytes
				int l = bytesToRead - n;
				if (l > 0) {
					n1 = this.client.read(rcv, n, l);
					n = n + n1;
					if (n < bytesToRead) {
						throw new RuntimeException("modbus need size=" + bytesToRead + "  but only read size=" + n);
					}
				}
			} else if (data[1] == functionFail) {
				// for error we need to read 5 bytes
				if (n < RTUCodec.rtuExceptionSize) {
					n1 = this.client.read(rcv, n, RTUCodec.rtuExceptionSize - n);
				}
				n += n1;
			}
			byte[] aduResponse = new byte[n];
			arrayUtil.copy(rcv, 0, aduResponse, 0, n);
			log.debug(stringUtil.format("modbus: received {}", aduResponse));
			return aduResponse;
		}
	}

	public States state() throws Exception {
		return state;
	}

}
