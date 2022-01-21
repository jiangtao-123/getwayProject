package cn.dpi.edge.gateway.bean;

/**
 * 异常代码
 * 
 * @author aimin
 *
 */
public class ModbusExceptionCode {
	/**
	 * 代码码
	 */
	private final byte code;

	/**
	 * 获取代码
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
	 *            代码
	 */
	private ModbusExceptionCode(byte code) {
		this.code = code;
	}

	public final static byte _IllegalFunction = 1;
	public final static byte _IllegalDataAddress = 2;
	public final static byte _IllegalDataValue = 3;
	public final static byte _ServerDeviceFailure = 4;
	public final static byte _Acknowledge = 5;
	public final static byte _ServerDeviceBusy = 6;
	public final static byte _MemoryParityError = 8;
	public final static byte _GatewayPathUnavailable = 10;
	public final static byte _GatewayTargetDeviceFailedToRespond = 11;

	public final static ModbusExceptionCode NULL = new ModbusExceptionCode((byte)0);
	public final static ModbusExceptionCode IllegalFunction = new ModbusExceptionCode(_IllegalFunction);
	public final static ModbusExceptionCode IllegalDataAddress = new ModbusExceptionCode(_IllegalDataAddress);
	public final static ModbusExceptionCode IllegalDataValue = new ModbusExceptionCode(_IllegalDataValue);
	public final static ModbusExceptionCode ServerDeviceFailure = new ModbusExceptionCode(_ServerDeviceFailure);
	public final static ModbusExceptionCode Acknowledge = new ModbusExceptionCode(_Acknowledge);
	public final static ModbusExceptionCode ServerDeviceBusy = new ModbusExceptionCode(_ServerDeviceBusy);
	public final static ModbusExceptionCode MemoryParityError = new ModbusExceptionCode(_MemoryParityError);
	public final static ModbusExceptionCode GatewayPathUnavailable = new ModbusExceptionCode(_GatewayPathUnavailable);
	public final static ModbusExceptionCode GatewayTargetDeviceFailedToRespond = new ModbusExceptionCode(
			_GatewayTargetDeviceFailedToRespond);

	/**
	 * 封装
	 * 
	 * @param code
	 * @return
	 */
	public static ModbusExceptionCode from(byte code) {
		ModbusExceptionCode temp = null;
		switch (code) {
		case _IllegalFunction:
			temp = IllegalFunction;
			break;
		case _IllegalDataAddress:
			temp = IllegalDataAddress;
			break;
		case _IllegalDataValue:
			temp = IllegalDataValue;
			break;
		case _ServerDeviceFailure:
			temp = ServerDeviceFailure;
			break;
		case _Acknowledge:
			temp = Acknowledge;
			break;
		case _ServerDeviceBusy:
			temp = ServerDeviceBusy;
			break;
		case _MemoryParityError:
			temp = MemoryParityError;
			break;
		case _GatewayPathUnavailable:
			temp = GatewayPathUnavailable;
			break;
		}
		return temp;
	}

	public String toString() {
		String temp = null;
		switch (code) {
		case _IllegalFunction:
			temp = "IllegalFunction";
			break;
		case _IllegalDataAddress:
			temp = "IllegalDataAddress";
			break;
		case _IllegalDataValue:
			temp = "IllegalDataValue";
			break;
		case _ServerDeviceFailure:
			temp = "ServerDeviceFailure";
			break;
		case _Acknowledge:
			temp = "Acknowledge";
			break;
		case _ServerDeviceBusy:
			temp = "ServerDeviceBusy";
			break;
		case _MemoryParityError:
			temp = "MemoryParityError";
			break;
		case _GatewayPathUnavailable:
			temp = "GatewayPathUnavailable";
			break;
		}
		return temp;
	}
}
