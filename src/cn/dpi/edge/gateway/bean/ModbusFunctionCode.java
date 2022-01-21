package cn.dpi.edge.gateway.bean;

/**
 * modbus功能码
 * 
 * @author aimin
 *
 */
public class ModbusFunctionCode {
	/**
	 * 功能码
	 */
	private final byte code;

	/**
	 * 获取功能码
	 * 
	 * @return
	 */
	public byte getCode() {
		return this.code;
	}

	/**
	 * 构造
	 * 
	 * @param code
	 *            功能码
	 */
	private ModbusFunctionCode(byte code) {
		this.code = code;
	}

	// 1bit
	/**
	 * 连续线圈读取
	 */
	public final static byte _ReadCoils = 1;
	/**
	 * 连续离散输入读取
	 */
	public final static byte _ReadDiscreteInputs = 2;
	/**
	 * 单个线圈写入
	 */
	public final static byte _WriteCoil = 5;
	/**
	 * 连续线圈写入
	 */
	public final static byte _WriteCoils = 15;
	// 16bit
	/**
	 * 连续输入寄存器读取
	 */
	public final static byte _ReadInputRegisters = 4;
	/**
	 * 连续保持寄存器读取
	 */
	public final static byte _ReadHoldingRegisters = 3;
	/**
	 * 单个保持寄存器写入
	 */
	public final static byte _WriteRegister = 6;
	/**
	 * 连续保持寄存器写入
	 */
	public final static byte _WriteRegisters = 16;

	// 1 bit
	/**
	 * 连续线圈读取
	 */
	public final static ModbusFunctionCode ReadCoils = new ModbusFunctionCode(_ReadCoils);
	/**
	 * 连续离散输入读取
	 */
	public final static ModbusFunctionCode ReadDiscreteInputs = new ModbusFunctionCode(_ReadDiscreteInputs);
	/**
	 * 单个线圈写入
	 */
	public final static ModbusFunctionCode WriteCoil = new ModbusFunctionCode(_WriteCoil);
	/**
	 * 连续线圈写入
	 */
	public final static ModbusFunctionCode WriteCoils = new ModbusFunctionCode(_WriteCoils);

	// 16bit
	/**
	 * 连续输入寄存器读取
	 */
	public final static ModbusFunctionCode ReadInputRegisters = new ModbusFunctionCode(_ReadInputRegisters);
	/**
	 * 连续保持寄存器读取
	 */
	public final static ModbusFunctionCode ReadHoldingRegisters = new ModbusFunctionCode(_ReadHoldingRegisters);
	/**
	 * 单保持寄存器写入
	 */
	public final static ModbusFunctionCode WriteRegister = new ModbusFunctionCode(_WriteRegister);
	/**
	 * 连续保持寄存器写入
	 */
	public final static ModbusFunctionCode WriteRegisters = new ModbusFunctionCode(_WriteRegisters);

	/**
	 * 封装 功能码
	 * 
	 * @param code
	 * @return
	 */
	public static ModbusFunctionCode from(byte code) {
		ModbusFunctionCode temp = null;
		switch (code) {
		case _ReadCoils:
			temp = ReadCoils;
			break;
		case _ReadDiscreteInputs:
			temp = ReadDiscreteInputs;
			break;
		case _WriteCoil:
			temp = WriteCoil;
			break;
		case _WriteCoils:
			temp = WriteCoils;
			break;
		case _ReadInputRegisters:
			temp = ReadInputRegisters;
			break;
		case _ReadHoldingRegisters:
			temp = ReadHoldingRegisters;
			break;
		case _WriteRegister:
			temp = WriteRegister;
			break;
		case _WriteRegisters:
			temp = WriteRegisters;
			break;
		}
		return temp;
	}

	public String toString() {
		String temp = null;
		switch (code) {
		case _ReadCoils:
			temp = "连续线圈读取";
			break;
		case _ReadDiscreteInputs:
			temp = "连续离散输入读取";
			break;
		case _WriteCoil:
			temp = "单个线圈写入";
			break;
		case _WriteCoils:
			temp = "连续线圈写入";
			break;
		case _ReadInputRegisters:
			temp = "连续输入寄存器读取";
			break;
		case _ReadHoldingRegisters:
			temp = "连续保持寄存器读取";
			break;
		case _WriteRegister:
			temp = "单个保持寄存器写入";
			break;
		case _WriteRegisters:
			temp = "连续保持寄存器写入";
			break;
		}
		return temp;
	}
}
