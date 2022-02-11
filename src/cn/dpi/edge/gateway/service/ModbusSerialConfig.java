package cn.dpi.edge.gateway.service;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import cn.dpi.edge.gateway.api.JSONSerializable;

public class ModbusSerialConfig implements JSONSerializable {

	public String address;
	public int baudrate;
	public int dataBits;
	public int stopBits;
	public String parity;
	public String blocking;
	public String autocts;

	public JSONObject toJson() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("address", address);
		j.put("baudrate", baudrate);
		j.put("dataBits", dataBits);
		j.put("stopBits", stopBits);
		j.put("parity", parity);
		j.put("blocking", blocking);
		j.put("autocts", autocts);
		return j;
	}

	public void parse(JSONObject j) throws JSONException {
		if (j.has("address")) {

			this.address = j.getString("address");
		}
		if (j.has("baudrate")) {

			this.baudrate = j.getInt("baudrate");
		}
		if (j.has("dataBits")) {

			this.dataBits = j.getInt("dataBits");
		}
		if (j.has("stopBits")) {

			this.stopBits = j.getInt("stopBits");
		}
		if (j.has("parity")) {

			this.parity = j.getString("parity");
		}
		if (j.has("blocking")) {

			this.blocking = j.getString("blocking");
		}
		if (j.has("autocts")) {

			this.autocts = j.getString("autocts");
		}
	}

}
