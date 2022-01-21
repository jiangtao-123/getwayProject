package cn.dpi.edge.gateway.service;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import cn.dpi.edge.gateway.api.ICodec;
import cn.dpi.edge.gateway.api.ILog;
import cn.dpi.edge.gateway.api.ITransport;
import cn.dpi.edge.gateway.api.JSONSerializable;
import cn.dpi.edge.gateway.modbus.RTUCodec;
import cn.dpi.edge.gateway.modbus.RTUConfig;
import cn.dpi.edge.gateway.modbus.RTUTransport;
import cn.dpi.edge.gateway.modbus.TCPCodec;
import cn.dpi.edge.gateway.modbus.TCPConfig;
import cn.dpi.edge.gateway.modbus.TCPTransport;

public class ModbusConfig implements JSONSerializable, DataConvert {

	public String id;
	public byte slaveId;
	public ModbusTCPConfig tcp;
	public ModbusSerialConfig serial;
	public ModbusArea[] areas;

	public static void main(String[] args) throws JSONException {
		ModbusConfig c = new ModbusConfig();
		c.id = "1";
		c.tcp = new ModbusTCPConfig();
		c.tcp.address = "127.0.0.1:8008";
		c.areas = new ModbusArea[2];
		ModbusArea area = new ModbusArea();
		area.id = "1";
		area.area = 1;
		area.start = 0;
		area.amount = 8;
		c.areas[0] = area;
		area = new ModbusArea();
		area.id = "2";
		area.area = 1;
		area.start = 8;
		area.amount = 8;
		c.areas[1] = area;
		System.out.println(c.toJson());
	}

	public JSONObject toJson() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("id", id);
		j.put("slaveId", slaveId);
		if (tcp != null) {
			j.put("tcp", tcp.toJson());
		} else if (serial != null) {
			j.put("serial", serial.toJson());
		}
		if (areas != null) {
			JSONArray array = new JSONArray();
			for (int i = 0; i < areas.length; i++) {
				ModbusArea area = areas[i];
				if (area != null) {
					array.put(areas[i].toJson());
				}
			}
			j.put("areas", array);
		}
		return j;
	}

	public void parse(JSONObject json) throws JSONException {
		this.id = json.getString("id");
		this.slaveId = (byte) (json.getInt("slaveId") & 0xFF);
		if (json.has("tcp")) {
			JSONObject _tcp = json.getJSONObject("tcp");
			this.tcp = new ModbusTCPConfig();
			this.tcp.parse(_tcp);
		} else if (json.has("serial")) {
			JSONObject _serial = json.getJSONObject("serial");
			this.serial = new ModbusSerialConfig();
			this.serial.parse(_serial);
		}
		JSONArray array = json.getJSONArray("areas");
		if (array != null) {
			this.areas = new ModbusArea[array.length()];
			for (int i = 0; i < array.length(); i++) {
				areas[i] = new ModbusArea();
				areas[i].parse(array.getJSONObject(i));
			}
		}
	}

	public JSONObject toData() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("id", id);
		if (areas != null) {
			JSONArray array = new JSONArray();
			for (int i = 0; i < areas.length; i++) {
				ModbusArea area = areas[i];
				if (area != null) {
					array.put(areas[i].toData());
				}
			}
			j.put("areas", array);
		}
		return j;
	}

	public ICodec modbusCodec() {
		if (tcp != null) {
			return TCPCodec.newCodec();
		} else if (serial != null) {
			return RTUCodec.newCodec();
		}
		throw new RuntimeException("modbus config error");
	}

	public ITransport modbusTransport(ILog log) {
		if (tcp != null) {
			String address = this.tcp.address;
			TCPConfig config = new TCPConfig();
			config.Address = address;
			ITransport transport = TCPTransport.newTCPTransport(config, log);
			return transport;
		} else if (serial != null) {
			RTUConfig config = new RTUConfig();
			config.Address = this.serial.address;
			config.BaudRate = this.serial.baudrate;
			ITransport transport = RTUTransport.newRTUTTransport(config, log);
			return transport;
		}
		throw new RuntimeException("modbus config error");
	}
}
