package cn.dpi.edge.gateway.component.log;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import cn.dpi.edge.gateway.api.JSONSerializable;

public class Config extends cn.dpi.edge.gateway.component.file.Config implements JSONSerializable {

	public boolean Debug = false;

	public JSONObject toJson() throws JSONException {
		return null;
	}

	public void parse(JSONObject json) throws JSONException {
		if (json.has("path")) {
			this.FilePath = json.getString("path");
		}
		if (json.has("debug")) {
			this.Debug = json.getBoolean("debug");
		}
	}

}
