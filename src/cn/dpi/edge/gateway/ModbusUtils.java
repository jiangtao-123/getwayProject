package cn.dpi.edge.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.bouncycastle.util.Arrays;
import org.joshvm.util.ArrayList;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import cn.dpi.edge.gateway.api.ICodec;
import cn.dpi.edge.gateway.api.ILog;
import cn.dpi.edge.gateway.api.IModbus;
import cn.dpi.edge.gateway.api.ITransport;
import cn.dpi.edge.gateway.component.log.Config;
import cn.dpi.edge.gateway.component.log.FileLog;
import cn.dpi.edge.gateway.modbus.Modbus;
import cn.dpi.edge.gateway.service.APPConfig;
import cn.dpi.edge.gateway.service.ModbusArea;
import cn.dpi.edge.gateway.service.ModbusConfig;
import cn.dpi.edge.gateway.utils.HexUtils;
import cn.dpi.edge.gateway.utils.streamUtil;
import cn.dpi.edge.gateway.utils.threadUtil;
import cn.dpi.edge.gateway.utils.file.FileConstant;
import cn.dpi.edge.gateway.utils.file.FileUtils;

/**
 * tcp连接获取modbus硬件数据；
 * 
 * @author jiangtao
 *
 */
public class ModbusUtils {
	private APPConfig aPPConfig;// 日志文件配置；
	private ArrayList modbusConfigList;// modbus配置文件
	private ModbusConfig modbusConfig;// modbus配置文件；
	private ILog log;
	private Hashtable dataTable = new Hashtable();
	private static ModbusUtils singleton;
	private boolean read = false;
	private boolean write = false;

	private boolean isRead() {
		return read;
	}

	private void setRead(boolean read) {
		this.read = read;
	}

	private boolean isWrite() {
		return write;
	}

	private void setWrite(boolean write) {
		this.write = write;
	}

	private Hashtable getDataTable() {
		return dataTable;
	}

	private void setDataTable(Hashtable iecTable) {
		this.dataTable = iecTable;
	}

	private ModbusUtils() {
	}

	public static ModbusUtils getInstance() {
		if (singleton == null) {
			synchronized (ModbusUtils.class) {
				singleton = new ModbusUtils();
			}

		}
		return singleton;
	}

	public static void main(String[] args) throws JSONException {
		ModbusUtils.getInstance().start(true);
	}

	/**
	 * 是否线程启动；
	 * 
	 * @param thread
	 */
	public void start(boolean isthread) {
		if (isthread) {
			Thread thread = new Thread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					// 初始化配置文件；
					ModbusUtils.getInstance().InitConfig();
					do {
						// 读取数据；
						ModbusUtils.getInstance().getModbusData();
						threadUtil.sleep(500);// 休眠5S；
					} while (true);
				}
			});
			thread.start();
		} else {

			// 初始化配置文件；
			ModbusUtils.getInstance().InitConfig();
			do {
				// 读取数据；
				ModbusUtils.getInstance().getModbusData();
				threadUtil.sleep(500);// 休眠5S；
			} while (true);
		}
	}

	/**
	 * iec端获取modbus数据；
	 * 
	 * @return hashTable;
	 */
	public synchronized Hashtable getDataHashtable() {
		ModbusUtils.getInstance().setRead(true);// 加读锁；
		if (ModbusUtils.getInstance().write) {
			return null;
		} else {
			ModbusUtils.getInstance().setRead(false);// 释放读锁；
			// 删除本次缓存文件；
			// FileUtils.deleteFile(FileConstant.fileURI,FileConstant.fileName);
			return dataTable;

		}

	}

	private void InitConfig() {
		try {
			// 获取日志配置信息；
			setAppConfig();
			// 获取modbus配置信息
			setModbusConfig();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setModbusConfig() throws IOException, JSONException {
		// TODO Auto-generated method stub
		InputStream stream;
		byte[] data;
		String json;
		stream = EdgeStudio.class.getResourceAsStream("/MudbusConfig.json");
		data = streamUtil.readToEnd(stream);

		threadUtil.close(stream);
		json = new String(data, "UTF-8");
//		System.out.println("<<>"+json);
		modbusConfigList = ModbusConfig.parse(new JSONArray(json));

	}

	private void setAppConfig() throws IOException, JSONException {
		// TODO Auto-generated method stub
		InputStream stream;
		byte[] data;
		String json;
		System.out.println("read app config");
		stream = EdgeStudio.class.getResourceAsStream("/app.json");
		data = streamUtil.readToEnd(stream);
		threadUtil.close(stream);
		json = new String(data, "UTF-8");
		aPPConfig = new APPConfig();
		aPPConfig.parse(new JSONObject(json));
		Config logConfig = new Config();
		logConfig.FilePath = aPPConfig.log.FilePath;
		log = FileLog.newFileLog(logConfig);
		System.out.println("read app config end");

	}

	/**
	 * 使用modbus协议获取硬件数据；
	 */
	public synchronized void getModbusData() {
		if (ModbusUtils.getInstance().read) {
			return;
//			 Thread.sleep(500);
			
		}
		// 写加锁
		ModbusUtils.getInstance().setWrite(true);
		if (modbusConfigList != null && modbusConfigList.size() > 0) {
			for (int i = 0; i < modbusConfigList.size(); i++) {
				modbusConfig = (ModbusConfig) modbusConfigList.get(i);
				ITransport transport = null;
				try {
					if (transport == null) {
						transport = modbusConfig.modbusTransport(log);
					}
					ICodec codec = modbusConfig.modbusCodec();
					transport.connect();
					IModbus modbus = null;//Modbus.newModbus(modbusConfig.slaveId, transport, codec);
					byte[] data = null;
					DataValue dataValue = null;
					ArrayList list = new ArrayList();
					for (int i1 = 0; i1 < modbusConfig.areas.size(); i1++) {
						IecData iecData = new IecData();
						DataVo d = new DataVo();
						ModbusArea area = (ModbusArea) modbusConfig.areas.get(i1);
						 modbus = Modbus.newModbus(area.slaveId, transport, codec);
						// 1:线圈 2:离散输入;3:保持寄存器;4:输入寄存器
						switch (area.area) {
						case 1:
							// 线圈
							data = modbus.ReadCoils(area.start, area.amount);
//							System.out.println(HexUtils.hexToInt(HexUtils.bytes2Hex(data)) + "><>PPPPPPP1");
							break;
						case 2:
							// 离散输入
							data = modbus.ReadDiscreteInputs(area.start, area.amount);
							break;
						case 3:
							// 保持寄存器
							dataValue = new DataValue();
							data = modbus.ReadHoldingRegisters(area.start, area.amount);
							break;
						case 4:
							// 输入寄存器
							data = modbus.ReadInputRegisters(area.start, area.amount);
//							System.out.println(HexUtils.bytes2Hex(data) + "><>PPPPPPP4");
							break;
						}
						d = HexUtils.getModbusData(data, area.bigEndian, area.dataType, d);
						iecData.setData(d);
						iecData.setSlaveId(modbusConfig.slaveId);
						iecData.setIndex(modbusConfig.id);
						list.add(iecData);
					}
					// 输出
					// 封装iec数据；
					setIecData(modbusConfig.slaveId, list);
					threadUtil.close(transport);
					transport = null;
					modbusConfig = null;
				} catch (Throwable e) {
					e.printStackTrace();
					log.debug(e.getMessage());
					threadUtil.close(transport);
					transport = null;
					modbusConfig = null;
					ModbusUtils.getInstance().setWrite(false);
				}
				threadUtil.sleep(500);
			}
		}
		// 写入缓存文件；
		writeFile();
		ModbusUtils.getInstance().setWrite(false);// 释放写锁；
	}

	private void writeFile() {
		// TODO Auto-generated method stub
		try {

			String str = parseToJson(ModbusUtils.getInstance().dataTable);
			FileUtils.creatFile(FileConstant.baseFileURI, FileConstant.tcpFileName+getDate(new Date())+".txt",
					/* System.currentTimeMillis()+ */str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getDate(Date date) {
		// TODO Auto-generated method stub
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);

		StringBuffer buffer = new StringBuffer();		

		buffer.append(ca.get(Calendar.YEAR)+"-");

		buffer.append((ca.get(Calendar.MONTH)+1)+"-");

		buffer.append(ca.get(Calendar.DAY_OF_MONTH));
		//追加时分秒；
//
//		buffer.append(ca.get(Calendar.HOUR_OF_DAY)+":");
//
//		buffer.append(ca.get(Calendar.MINUTE)+":");
//
//		buffer.append(ca.get(Calendar.SECOND));

		return buffer.toString();
		
	}

	private String parseToJson(Hashtable iecTable) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		if (null != iecTable && iecTable.size() > 0) {
			Enumeration en = iecTable.keys();
			// System.out.println();
			JSONObject jsonObject = new JSONObject();
			jsonArray.put(jsonObject);
			while (en.hasMoreElements()) {
				Object key = (Object) en.nextElement();
				JSONArray data = new JSONArray();
				jsonObject.put(key + "", data);
				ArrayList list = (ArrayList) (iecTable.get(key));
				for (int i = 0; i < list.size(); i++) {
					JSONObject j = new JSONObject();
					IecData iecData = (IecData) list.get(i);
					j.put("slaveId", iecData.getSlaveId());
					j.put("type", iecData.getType());
					j.put("change", iecData.getChange());
					j.put("index", iecData.getIndex());
					if (null != iecData.getData()) {
						JSONObject d = new JSONObject();
						d.put("byteData", HexUtils.bytes2Hex(iecData.getData().getByteData()));
						d.put("intData", iecData.getData().getIntData());
						d.put("shortData", iecData.getData().getShortData());
						d.put("floatData", iecData.getData().getFloatData() + "");
						j.put("data", d);
					}

					data.put(j);
				}

			}
		}
		return jsonArray.toString();
	}

	/**
	 * 封装iec数据；
	 * 
	 * @param data
	 */
	private void setIecData(byte slaveId, ArrayList list) {
		// TODO Auto-generated method stub
		if (this.checkData(((ArrayList) ModbusUtils.getInstance().getDataTable().get(slaveId + "")), list)) {
			ModbusUtils.getInstance().getDataTable().put(slaveId + "", list);
		}
	}

	/**
	 * 比对两个list中对象属性值是否有改变
	 * 
	 * @param oldList上一次数据；
	 * @param newList最近一次数据；
	 * @return true有变化；
	 */
	private boolean checkData(ArrayList oldList, ArrayList newList) {
		// TODO Auto-generated method stub
		boolean flag = false;// 默认没有变化；
		if ((newList != null && newList.size() > 0) && (oldList != null && oldList.size() > 0)) {
			if (newList.size() != oldList.size()) {
				flag = true;
			} else {// 比对两个list中对应对象属性值是否有变化以index匹配对象；
				for (int i = 0; i < newList.size(); i++) {
					IecData nd = (IecData) newList.get(i);
					IecData od = null;
					for (int j = 0; j < oldList.size(); j++) {
						if (((IecData) oldList.get(j)).getIndex() == nd.getIndex()) {
							od = (IecData) oldList.get(j);
						}
					}
					if (chechIecDataChage(nd, od)) {
						nd.setChange(true);// 设置本条数据已经改变；
						flag = true;
					}
				}
			}
		} else {// 处理初始数据为空时，不做判定，直接存入；
			flag = true;

		}

		return flag;

	}

	/**
	 * 比较两次采集的数据是否有变化
	 * 
	 * @param nd
	 * @param od
	 * @return TRUE 有变化；
	 */
	private boolean chechIecDataChage(IecData nd, IecData od) {
		if (nd.getSlaveId() != od.getSlaveId()) {
			return true;
		} else if (nd.getType() != od.getType()) {
			return true;
		} else if (compareData(nd.getData(), od.getData())) {
			return true;
		}
		return false;
	}

	/**
	 * 比较采集的数据和上次是否有变化；
	 * 
	 * @param nd
	 * @param od
	 * @return true有变化
	 */

	private boolean compareData(DataVo nd, DataVo od) {
		boolean flag = false;
		if (null != nd && null != od) {
			if (nd.getDataType() != od.getDataType()) {
				flag = true;
			}
			// 比较从设备获取到的数据；
			switch (nd.getDataType()) {
			case 0:
				flag = bytesCompare(nd.getByteData(), od.getByteData());
				break;
			case 1:
				flag = (nd.getShortData() != od.getShortData());
				break;
			case 2:
				flag = (nd.getFloatData() != od.getFloatData());
				break;
			default:
				break;
			}
		} else {
			flag = true;
		}
		return flag;
	}

	/**
	 * 数组内容比较；
	 * 
	 * @param nd
	 * @param od
	 * @return true 有变化；
	 */

	private boolean bytesCompare(byte[] nd, byte[] od) {

		boolean flag = false;
		if ((nd != null && nd.length > 0) && (od != null && od.length > 0)) {
			if (nd.length != od.length) {
				flag = true;
				return flag;
			}
			for (int i = 0; i < nd.length; i++) {
				if (nd[i] != od[i]) {
					flag = true;
					break;
				}
			}
		} else {
			flag = true;
		}
		return flag;
	}

}
