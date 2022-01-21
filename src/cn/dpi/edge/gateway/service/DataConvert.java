package cn.dpi.edge.gateway.service;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public interface DataConvert {
	JSONObject toData() throws JSONException;
}
