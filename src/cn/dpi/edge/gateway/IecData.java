package cn.dpi.edge.gateway;

import org.joshvm.util.ArrayList;

public class IecData {
	private byte slaveId;// 设备ID；
	private Integer type;// 读取类型；1:线圈 2:离散输入;3:保持寄存器;4:输入寄存器；
	private DataVo data;// 读取数据；
	private boolean change;
	private String index;

	public DataVo getData() {
		return data;
	}

	public void setData(DataVo data) {
		this.data = data;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public byte getSlaveId() {
		return slaveId;
	}

	public void setSlaveId(byte slaveId) {
		this.slaveId = slaveId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public boolean getChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

}
