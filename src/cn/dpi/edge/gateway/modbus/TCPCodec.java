package cn.dpi.edge.gateway.modbus;

import cn.dpi.edge.gateway.api.ICodec;
import cn.dpi.edge.gateway.bean.ModbusFunctionCode;
import cn.dpi.edge.gateway.bean.ModbusPDU;
import cn.dpi.edge.gateway.utils.arrayUtil;

public class TCPCodec extends ModbusCodec {

	public final static int tcpProtocolIdentifier = 0x0000;
	public final static int tcpHeaderSize = 7;
	public final static int tcpMaxLength = 260;
	public final static int tcpMaxBufferLength = tcpMaxLength - tcpHeaderSize + 1;

	public static ICodec newCodec() {
		return new TCPCodec();
	}

	public byte[] encode(ModbusPDU pdu, byte slaveID) {
		int size = tcpHeaderSize + 1 + pdu.Data.length;
		byte[] adu = new byte[size];
		short transactionID = this.newTransactionID();
		int index = 0;
		// Transaction identifier
		adu[index++] = (byte) (transactionID >> 8);
		adu[index++] = (byte) (transactionID);
		// Protocol identifier
		adu[index++] = (byte) (tcpProtocolIdentifier >> 8);
		adu[index++] = (byte) (tcpProtocolIdentifier);
		// Length = sizeof(SlaveId) + sizeof(FunctionCode) + Data
		int length = 1 + 1 + pdu.Data.length;
		adu[index++] = (byte) (length >> 8);
		adu[index++] = (byte) (length);
		// Unit identifier
		adu[index++] = slaveID;
		// PDU
		adu[index++] = pdu.FunctionCode.getCode();
		for (int i = 0; i < pdu.Data.length; i++) {
			adu[index++] = pdu.Data[i];
		}
		return adu;
	}

	public void verify(byte[] aduRequest, byte[] aduResponse) {
		// Transaction id
		int req_index = 0;
		int res_index = 0;
		int responseVal = (aduResponse[res_index++] << 8) | (aduResponse[res_index++]);
		int requestVal = (aduRequest[req_index++] << 8) | (aduRequest[req_index++]);
		if (responseVal != requestVal) {
			throw new RuntimeException("modbus: response transaction id '" + responseVal + "' does not match request '"
					+ requestVal + "'");
		}
		// Protocol id
		responseVal = (aduResponse[res_index++] << 8) | (aduResponse[res_index++]);
		requestVal = (aduRequest[req_index++] << 8) | (aduRequest[req_index++]);
		if (responseVal != requestVal) {
			throw new RuntimeException(
					"modbus: response protocol id '" + responseVal + "' does not match request '" + requestVal + "'");
		}
		// Unit id (1 byte)
		responseVal = aduResponse[6];
		requestVal = aduRequest[6];
		if (responseVal != requestVal) {
			throw new RuntimeException(
					"modbus: response unit id '" + responseVal + "' does not match request '" + requestVal + "'");
		}
	}

	public ModbusPDU decode(byte[] adu) {
		// Read length value in the header
		int length = adu[4] << 8 | adu[5];
		int pduLength = adu.length - tcpHeaderSize;
		if (pduLength <= 0 || pduLength != (length - 1)) {
			throw new RuntimeException("modbus: length in response '" + (length - 1)
					+ "' does not match pdu data length '" + pduLength + "'");
		}
		ModbusPDU pdu = new ModbusPDU();
		// The first byte after header is function code
		pdu.FunctionCode = ModbusFunctionCode.from(adu[tcpHeaderSize]);
		pdu.Data = arrayUtil.clone(adu, tcpHeaderSize + 1);
		return pdu;
	}

}
