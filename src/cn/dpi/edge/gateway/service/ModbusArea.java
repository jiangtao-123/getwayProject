package cn.dpi.edge.gateway.service;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import cn.dpi.edge.gateway.api.JSONSerializable;
import cn.dpi.edge.gateway.utils.stringUtil;

public class ModbusArea implements JSONSerializable, DataConvert {
	// "id":"string-唯一标识",
	// "area": "int-1:线圈 2:离散输入;3:保持寄存器;4:输入寄存器 1|2:按位取值 3|4:按双字节取值",
	// "start": "int-起始地址 从0开始",
	// "amount": "int-读取数量 area为1或者2时1~200； 其他1~125"
	public String id;
	public int area;
	public int start;
	public int amount;
	public byte[] data;

	public JSONObject toJson() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("id", id);
		j.put("area", area);
		j.put("start", start);
		j.put("amount", amount);
		return j;
	}

	public void parse(JSONObject json) throws JSONException {
		this.id = json.getString("id");
		this.area = json.getInt("area");
		this.start = json.getInt("start");
		this.amount = json.getInt("amount");
	}

	public JSONObject toData() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("id", id);
		j.put("data", stringUtil.format("{}", this.data));
		return j;
	}
}
