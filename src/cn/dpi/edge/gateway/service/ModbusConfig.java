package cn.dpi.edge.gateway.service;

import org.joshvm.util.ArrayList;
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
	public ArrayList areas;
	public boolean bigEndian;// 是否是大端
	public int dataType;// 哪种数据类型；1：int16,2：int32,3：float32
	ArrayList modbusConfigList = new ArrayList();

	public static void main(String[] args) throws JSONException {
		ModbusConfig c = new ModbusConfig();
		c.id = "1";
		c.tcp = new ModbusTCPConfig();
		c.tcp.address = "127.0.0.1:502";
		c.areas = new ArrayList();
		ModbusArea area = new ModbusArea();
		area.id = "1";
		area.area = 1;
		area.start = 0;
		area.amount = 8;
		c.areas.add(area);
		area = new ModbusArea();
		area.id = "2";
		area.area = 1;
		area.start = 8;
		area.amount = 8;
		c.areas.add(area);
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
			for (int i = 0; i < areas.size(); i++) {
				ModbusArea area = (ModbusArea) areas.get(i);
				if (area != null) {
					array.put(((ModbusArea) areas.get(i)).toJson());
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
			this.areas = new ArrayList();
			for (int i = 0; i < array.length(); i++) {
				ModbusArea modbusArea = new ModbusArea();
				modbusArea.parse(array.getJSONObject(i));
				this.areas.add(modbusArea);
			}
		}
	}

	public JSONObject toData() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("id", id);
		if (areas != null) {
			JSONArray array = new JSONArray();
			for (int i = 0; i < areas.size(); i++) {
				ModbusArea area = (ModbusArea) areas.get(i);
				if (area != null) {
					array.put(((ModbusArea) areas.get(i)).toData());
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
			// System.out.println("tcp<<<<<<<123");
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

	public static ArrayList parse(JSONArray jsonArray) throws JSONException {
		// TODO Auto-generated method stub
		ArrayList list = new ArrayList();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ModbusConfig modbusConfig = new ModbusConfig();
				JSONObject json = jsonArray.getJSONObject(i);
				modbusConfig.id = json.getString("id");
				// if (json.has("bigEndian")) {
				// modbusConfig.bigEndian=json.getBoolean("bigEndian");
				// }else{//默认是大端
				// modbusConfig.bigEndian=true;
				//
				// }
				// if (json.has("dataType")) {
				// modbusConfig.dataType=json.getInt("dataType");
				// }
				modbusConfig.slaveId = (byte) (json.getInt("id"));

				if (json.has("tcp")) {
					JSONObject _tcp = json.getJSONObject("tcp");
					modbusConfig.tcp = new ModbusTCPConfig();
					modbusConfig.tcp.parse(_tcp);
				} else if (json.has("serial")) {
					JSONObject _serial = json.getJSONObject("serial");
					modbusConfig.serial = new ModbusSerialConfig();
					modbusConfig.serial.parse(_serial);
				}
				JSONArray array = json.getJSONArray("areas");
				if (array != null) {
					modbusConfig.areas = new ArrayList();
					for (int ii = 0; ii < array.length(); ii++) {
						ModbusArea modbusArea = new ModbusArea();
						modbusArea.parse(array.getJSONObject(ii));
						modbusConfig.areas.add(modbusArea);
					}
				}

				list.add(modbusConfig);
			}
		}
		return list;
	}
}
