package cn.dpi.edge.gateway.modbus;

import cn.dpi.edge.gateway.api.ICodec;
import cn.dpi.edge.gateway.bean.ModbusException;
import cn.dpi.edge.gateway.bean.ModbusFunctionCode;
import cn.dpi.edge.gateway.bean.ModbusPDU;
import cn.dpi.edge.gateway.component.crc.CRC16;
import cn.dpi.edge.gateway.utils.arrayUtil;
import cn.dpi.edge.gateway.utils.stringUtil;

public class RTUCodec extends ModbusCodec {

	public final static int rtuMinSize = 4;
	public final static int rtuMaxSize = 256;
	public final static int rtuExceptionSize = 5;

	public static ICodec newCodec() {
		return new RTUCodec();
	}

	public byte[] encode(ModbusPDU pdu, byte slaveID) {
		int length = pdu.Data.length + 4;

		if (length > rtuMaxSize) {
			throw ModbusException.responseError(
					"modbus: length of data '" + length + "' must not be bigger than '" + rtuMaxSize + "'");
		}
		byte[] adu = new byte[length];
		int index = 0;
		// Unit identifier
		adu[index++] = slaveID;
		// function code
		adu[index++] = pdu.FunctionCode.getCode();
		// PDU
		for (int i = 0; i < pdu.Data.length; i++) {
			adu[index++] = pdu.Data[i];
		}
		// Append crc
		CRC16 crc = CRC16.NewCRC16();
		crc.Push(adu, 0, length - 2);
		short checksum = crc.Value();
		adu[length - 1] = (byte) (checksum & 0xFF);
		adu[length - 2] = (byte) ((checksum >> 8) & 0xFF);
		return adu;

	}

	public void verify(byte[] aduRequest, byte[] aduResponse) {
		int length = aduResponse.length;
		// Minimum size (including address, function and CRC)
		if (length < rtuMinSize) {
			throw ModbusException.responseError(
					"modbus: response length '" + length + "' does not meet minimum '" + rtuMinSize + "'");
		}
		// Slave address must match
		if (aduResponse[0] != aduRequest[0]) {
			throw ModbusException.responseError("modbus: response slave id '" + aduResponse[0]
					+ "' does not match request '" + aduRequest[0] + "'");
		}

	}

	public ModbusPDU decode(byte[] adu) {
		// get length
		int length = adu.length;
		// Calculate checksum
		CRC16 crc = CRC16.NewCRC16();
		crc.Push(adu, 0, length - 2);
		short checksum = (short) (((adu[length - 1] & 0xFF) << 8) | (adu[length - 2] & 0xFF));
		short calcCRC = crc.Value();
		if (checksum != calcCRC) {
			throw ModbusException
					.responseError("modbus: response crc '" + checksum + "' does not match expected '" + calcCRC + "'");
		}
		// Function code & data
		ModbusPDU pdu = new ModbusPDU();
		pdu.FunctionCode = ModbusFunctionCode.from(adu[1]);
		pdu.Data = new byte[length - 4];
		arrayUtil.copy(adu, 2, pdu.Data, 0,pdu.Data.length);
		return pdu;
	}

}
