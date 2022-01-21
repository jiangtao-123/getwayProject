package cn.dpi.edge.gateway.bean;

/**
 * pdu
 * 
 * @author aimin
 *
 */
public class ModbusPDU {

	/**
	 * 功能码
	 */
	public ModbusFunctionCode FunctionCode;
	/**
	 * 数据
	 */
	public byte[] Data;

	public ModbusPDU() {
	}

	public ModbusPDU(ModbusFunctionCode functionCode, byte[] data) {
		this.FunctionCode = functionCode;
		this.Data = data;
	}

	public static ModbusPDU newModbusPDU(ModbusFunctionCode functionCode, byte[] data) {
		return new ModbusPDU(functionCode, data);
	}
}
