package cn.dpi.edge.gateway.modbus;

import cn.dpi.edge.gateway.api.ICodec;
import cn.dpi.edge.gateway.api.IModbus;
import cn.dpi.edge.gateway.api.ITransport;
import cn.dpi.edge.gateway.bean.ModbusException;
import cn.dpi.edge.gateway.bean.ModbusFunctionCode;
import cn.dpi.edge.gateway.bean.ModbusPDU;
import cn.dpi.edge.gateway.utils.arrayUtil;

public class Modbus implements IModbus {
	private byte slaveID;
	private ITransport transport;
	private ICodec codec;

	public static Modbus newModbus(byte slaveID, ITransport transport, ICodec codec) {
		Modbus item = new Modbus();
		item.slaveID = slaveID;
		item.transport = transport;
		item.codec = codec;
		return item;
	}

	/**
	 * send sends request and checks possible exception in the response.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public ModbusPDU send(ModbusPDU request) throws Exception {
		ModbusPDU pdu = null;
		byte[] aduRequest = this.codec.encode(request, this.slaveID);
		byte[] aduResponse = this.transport.send(aduRequest);
		this.codec.verify(aduRequest, aduResponse);
		pdu = this.codec.decode(aduResponse);
		// Check correct function code returned (exception)
		if (pdu.FunctionCode != request.FunctionCode) {
			throw ModbusException.responseError(pdu);
		}
		if (pdu.Data == null || pdu.Data.length == 0) {
			// Empty response
			throw new RuntimeException("modbus: response data is empty");
		}
		return pdu;
	}

	// dataBlock creates a sequence of uint16 data.
	private static byte[] dataBlock(int address, int quantity) {
		byte[] data = new byte[4];
		data[0] = (byte) ((address >> 8) & 0xFF);
		data[1] = (byte) (address & 0xFF);
		data[2] = (byte) ((quantity >> 8) & 0xFF);
		data[3] = (byte) (quantity & 0xFF);
		return data;
	}

	public byte[] ReadCoils(int address, int quantity) throws Exception {
		if (quantity < 1 || quantity > 2000) {
			throw ModbusException.responseError("modbus: quantity '" + quantity + "' must be between '1' and '2000'");
		}

		byte[] temp = dataBlock(address, quantity);
		ModbusPDU request = ModbusPDU.newModbusPDU(ModbusFunctionCode.ReadCoils, temp);

		ModbusPDU response = this.send(request);
		int count = (int) response.Data[0];
		int length = response.Data.length - 1;
		if (count != length) {
			throw ModbusException
					.responseError("modbus: response data size '" + length + "' does not match count '" + count + "'");
		}
		byte[] data = new byte[length];
		arrayUtil.copy(response.Data, 1, data, 0);
		return data;
	}

	public byte[] ReadDiscreteInputs(int address, int quantity) throws Exception {
		if (quantity < 1 || quantity > 2000) {
			throw ModbusException.responseError("modbus: quantity '" + quantity + "' must be between '1' and '2000'");
		}

		byte[] temp = dataBlock(address, quantity);
		ModbusPDU request = ModbusPDU.newModbusPDU(ModbusFunctionCode.ReadDiscreteInputs, temp);

		ModbusPDU response = this.send(request);
		int count = (int) response.Data[0];
		int length = response.Data.length - 1;
		if (count != length) {
			throw ModbusException
					.responseError("modbus: response data size '" + length + "' does not match count '" + count + "'");
		}
		byte[] data = new byte[length];
		arrayUtil.copy(response.Data, 1, data, 0);
		return data;
	}

	public byte[] ReadHoldingRegisters(int address, int quantity) throws Exception {
		if (quantity < 1 || quantity > 125) {
			throw ModbusException.responseError("modbus: quantity '" + quantity + "' must be between '1' and '125'");
		}
		byte[] temp = dataBlock(address, quantity);
		ModbusPDU request = ModbusPDU.newModbusPDU(ModbusFunctionCode.ReadHoldingRegisters, temp);
		ModbusPDU response = this.send(request);
		int count = (int) response.Data[0];
		int length = response.Data.length - 1;
		if (count != length) {
			throw ModbusException
					.responseError("modbus: response data size '" + length + "' does not match count '" + count + "'");
		}
		byte[] data = new byte[length];
		arrayUtil.copy(response.Data, 1, data, 0);
		return data;
	}

	public byte[] ReadInputRegisters(int address, int quantity) throws Exception {
		if (quantity < 1 || quantity > 125) {
			throw ModbusException.responseError("modbus: quantity '" + quantity + "' must be between '1' and '125'");
		}

		byte[] temp = dataBlock(address, quantity);
		ModbusPDU request = ModbusPDU.newModbusPDU(ModbusFunctionCode.ReadInputRegisters, temp);

		ModbusPDU response = this.send(request);
		int count = (int) response.Data[0];
		int length = response.Data.length - 1;
		if (count != length) {
			throw ModbusException
					.responseError("modbus: response data size '" + length + "' does not match count '" + count + "'");
		}
		byte[] data = new byte[length];
		arrayUtil.copy(response.Data, 1, data, 0);
		return data;
	}
}