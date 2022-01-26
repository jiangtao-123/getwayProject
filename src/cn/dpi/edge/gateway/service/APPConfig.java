package cn.dpi.edge.gateway.service;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import cn.dpi.edge.gateway.api.JSONSerializable;
import cn.dpi.edge.gateway.component.log.Config;

public class APPConfig implements JSONSerializable {
	public Config log;

	public JSONObject toJson() throws JSONException {
		JSONObject j=new JSONObject();
		j.put("path", log.FilePath);
		j.put("debug", log.Debug);
		return j;
	}

	public void parse(JSONObject json) throws JSONException {
		if (json.has("log")) {
			this.log = new Config();
			this.log.parse(json.getJSONObject("log"));
		}
	}

}
