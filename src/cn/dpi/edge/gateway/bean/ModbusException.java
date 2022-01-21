package cn.dpi.edge.gateway.bean;

public class ModbusException extends RuntimeException {

	public ModbusFunctionCode FunctionCode;
	public ModbusExceptionCode ExceptionCode;

	public ModbusException() {
	}

	public ModbusException(String message) {
		super(message);
	}

	public static ModbusException responseError(ModbusPDU response) {
		ModbusFunctionCode functionCode = response.FunctionCode;
		ModbusExceptionCode modbusCode = ModbusExceptionCode.NULL;
		if (response.Data != null && response.Data.length > 0) {
			modbusCode = ModbusExceptionCode.from(response.Data[0]);
		}
		String message = functionCode.toString() + "-" + modbusCode.toString();
		ModbusException ex = new ModbusException(message);
		ex.FunctionCode = functionCode;
		ex.ExceptionCode = modbusCode;
		return ex;
	}

	public static ModbusException responseError(String message) {
		ModbusException ex = new ModbusException(message);
		return ex;
	}
}
