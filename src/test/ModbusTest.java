package test;

import cn.dpi.edge.gateway.api.ICodec;
import cn.dpi.edge.gateway.api.IModbus;
import cn.dpi.edge.gateway.api.ITransport;
import cn.dpi.edge.gateway.component.log.ConsoleLog;
import cn.dpi.edge.gateway.modbus.Modbus;
import cn.dpi.edge.gateway.modbus.RTUCodec;
import cn.dpi.edge.gateway.modbus.RTUConfig;
import cn.dpi.edge.gateway.modbus.RTUTransport;
import cn.dpi.edge.gateway.modbus.TCPCodec;
import cn.dpi.edge.gateway.modbus.TCPConfig;
import cn.dpi.edge.gateway.modbus.TCPTransport;
import cn.dpi.edge.gateway.utils.modbusUtil;
import cn.dpi.edge.gateway.utils.stringUtil;
import cn.dpi.edge.gateway.utils.threadUtil;

public class ModbusTest {

	static ConsoleLog log = new ConsoleLog();

	private static ITransport serialTest() {
		RTUConfig config = new RTUConfig();
		config.Address = "COM0";
		config.BaudRate = 9600;
		ITransport transport = RTUTransport.newRTUTTransport(config, log);
		return transport;
	}

	private static ITransport tcpTest() {
		String address = "192.168.6.254:8008";
		address = "127.0.0.1:502";
		TCPConfig  config = new TCPConfig();
		config.Address = address;
		ITransport transport = TCPTransport.newTCPTransport(config, log);
		return transport;
	}

	public static void main(String[] args) {
		byte slaveID = 1;
		ICodec codec;
		ITransport transport;
		 transport = tcpTest();
		 System.out.println(">>>>>>>>");
		 codec = TCPCodec.newCodec();
//		transport = serialTest();
//		codec = RTUCodec.newCodec();
		try {
			transport.connect();
			IModbus modbus = Modbus.newModbus(slaveID, transport, codec);
			System.out.println("read Coils>>");
			ReadCoils(modbus);//1do线圈
			System.out.println("read Inputs>>");
			 ReadDiscreteInputs(modbus);//
			 System.out.println("read HoldingRegisters>>");
			 ReadHoldingRegisters(modbus);//do遥信，遥测
			 System.out.println("read InputRegisters>>");
			 ReadInputRegisters(modbus);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			threadUtil.close(transport);
		}
	}

	private static void ReadCoils(IModbus modbus) throws Exception {
		int start = 0, quantity = 8;//读取8个数据；
		boolean[] coils = new boolean[quantity];
		byte[] data = modbus.ReadCoils(start, quantity);
		modbusUtil.readCoils(coils, data);
		for (int i = 0; i < coils.length; i++) {
			log.info(i + "---" + (coils[i]));
		}
	}
/**
 * 
 * @param modbus
 * @throws Exception
 */
	private static void ReadDiscreteInputs(IModbus modbus) throws Exception {
		int start = 0, quantity = 2;
		boolean[] coils = new boolean[quantity];
		byte[] data = modbus.ReadDiscreteInputs(start, quantity);
		modbusUtil.readCoils(coils, data);
		for (int i = 0; i < coils.length; i++) {
			log.info(i + "---" + (coils[i]));
		}
	}

	private static void ReadHoldingRegisters(IModbus modbus) throws Exception {
		int start = 0, quantity = 10;
		byte[] data = modbus.ReadHoldingRegisters(start, quantity);
		boolean[] coils = new boolean[quantity];
		modbusUtil.readCoils(coils, data);
		for (int i = 0; i < coils.length; i++) {
			log.info(i + "---" + (coils[i]));
		}
		System.out.println(stringUtil.format("{}", data));
	}

	private static void ReadInputRegisters(IModbus modbus) throws Exception {
		int start = 0, quantity = 8;
		byte[] data = modbus.ReadInputRegisters(start, quantity);
		boolean[] coils = new boolean[quantity];
		for (int i = 0; i < coils.length; i++) {
			log.info(i + "---" + (coils[i]));
		}
		System.out.println(stringUtil.format("{}", data));
	}
}
