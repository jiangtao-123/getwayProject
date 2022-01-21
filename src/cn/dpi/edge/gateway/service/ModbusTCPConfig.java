package cn.dpi.edge.gateway.service;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import cn.dpi.edge.gateway.api.JSONSerializable;

public class ModbusTCPConfig implements JSONSerializable {

	public String address;

	public JSONObject toJson() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("address", address);
		return j;
	}

	public void parse(JSONObject json) throws JSONException {
		this.address = json.getString("address");
	}

}
