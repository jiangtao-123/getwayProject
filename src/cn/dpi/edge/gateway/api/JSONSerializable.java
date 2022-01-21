package cn.dpi.edge.gateway.api;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public interface JSONSerializable extends Serializable {

	JSONObject toJson() throws JSONException;

	void parse(JSONObject json) throws JSONException;

}
