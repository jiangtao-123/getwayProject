package cn.dpi.edge.gateway;

public class DataVo {
	private short shortData;
	private int intData;
	private float floatData;
	private byte[] byteData;
	private int dataType;// 数据类型；0:byte[],1：int16,2：int32,3：float32

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

	public short getShortData() {
		return shortData;
	}

	public void setShortData(short shortData) {
		this.shortData = shortData;
	}

	public int getIntData() {
		return intData;
	}

	public void setIntData(int intData) {
		this.intData = intData;
	}

	public float getFloatData() {
		return floatData;
	}

	public void setFloatData(float floatData) {
		this.floatData = floatData;
	}

}
