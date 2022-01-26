package cn.dpi.edge.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

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
 * 获取modbus硬件数据；
 * @author jiangtao
 *
 */
public class ModbusUtils {
	private  APPConfig  aPPConfig;//缓存文件配置；
	private ArrayList modbusConfigList;//modbus配置文件
	private  ModbusConfig modbusConfig;//modbus配置文件；
	private  ILog log;
	private  Hashtable dataTable=new Hashtable();
	private static ModbusUtils singleton;
	private boolean read=false;
	private boolean write=false;
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public boolean isWrite() {
		return write;
	}
	public void setWrite(boolean write) {
		this.write = write;
	}
	public  Hashtable getDataTable() {
		return dataTable;
	}
	public  void setDataTable(Hashtable iecTable) {
		this.dataTable = iecTable;
	}
	private ModbusUtils(){}
	public static ModbusUtils getInstance(){
		if (singleton==null) {
			synchronized (ModbusUtils.class) {
				singleton=new ModbusUtils();
			}
			
		}
		return singleton;
	}
	public static void main(String[] args) throws JSONException {
		//初始化配置文件；
		ModbusUtils.getInstance().InitConfig();
		do {
			//读取数据；
			ModbusUtils.getInstance().getModbusData();
			threadUtil.sleep(5000);//休眠5S；
		} while (true);
	}
	/**
	 * iec端获取modbus数据；
	 * @return hashTable;
	 */
	public synchronized Hashtable getDataHashtable(){
		ModbusUtils.getInstance().setRead(true);//加读锁；
		if (ModbusUtils.getInstance().write) {
			return null;
		}else {
			ModbusUtils.getInstance().setRead(false);//释放读锁；
			return dataTable;
			
		}
	
	}
	private  void InitConfig() {
		try {
		//获取缓存配置信息；
			setAppConfig();
//			System.out.println("cg>>>>>");
			//获取modbus配置信息
			setModbusConfig();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	private  void setModbusConfig() throws IOException, JSONException {
		// TODO Auto-generated method stub
		InputStream stream;
		byte[] data;
		String json;
		stream = EdgeStudio.class.getResourceAsStream("/MudbusConfig.json");
		data = streamUtil.readToEnd(stream);

		threadUtil.close(stream);
		json = new String(data, "UTF-8");
//		System.out.println("><><><>>>>>"+json);
//		modbusConfig=new ModbusConfig();
		modbusConfigList=	ModbusConfig.parse(new JSONArray(json));
		
	}
	private  void setAppConfig()throws IOException, JSONException {
		// TODO Auto-generated method stub
		InputStream stream;
		byte[] data;
		String json;
//		ILog log;// = new ConsoleLog();
		System.out.println("read app config");
		stream = EdgeStudio.class.getResourceAsStream("/app.json");
		data = streamUtil.readToEnd(stream);
		threadUtil.close(stream);
		json = new String(data, "UTF-8");
		 aPPConfig = new APPConfig();
		 aPPConfig.parse(new JSONObject(json));
		Config logConfig = new Config();
		logConfig.FilePath = aPPConfig.log.FilePath;
		System.out.println("FilePath"+logConfig.FilePath+"<><><>>KKKKK");
		log = FileLog.newFileLog(logConfig);
		System.out.println("read app config end");
		
	}
	/**
	 * 使用modbus协议获取硬件数据；
	 */
	public  synchronized void getModbusData() {
		if (ModbusUtils.getInstance().read) {
		return;
//			Thread.sleep(millis);
		}
		//写加锁
ModbusUtils.getInstance().setWrite(true);
if (modbusConfigList!=null&&modbusConfigList.size()>0) {
	for (int i = 0; i < modbusConfigList.size(); i++) {
		modbusConfig=(ModbusConfig) modbusConfigList.get(i);
		ITransport transport = null;
		try {
			if (transport == null) {
				transport = modbusConfig.modbusTransport(log);
			}
			ICodec codec = modbusConfig.modbusCodec();
			transport.connect();
			IModbus modbus = Modbus.newModbus(modbusConfig.slaveId, transport, codec);
			byte[] data = null;
			DataValue dataValue=null;
			ArrayList list=new ArrayList();
			for (int i1 = 0; i1 < modbusConfig.areas.size(); i1++) {
				IecData iecData=new IecData();
				ModbusArea area = (ModbusArea) modbusConfig.areas.get(i1);
				System.out.println("area.area"+":"+area.area+"area.start:"+area.start+"area.amount:"+area.amount+">>>>>>");
				// 1:线圈 2:离散输入;3:保持寄存器;4:输入寄存器
				switch (area.area) {
				case 1:
					// 线圈
					data = modbus.ReadCoils(area.start, area.amount);
					System.out.println(HexUtils.bytes2Hex(data)+"><>PPPPPPP1");
					break;
				case 2:
					// 离散输入
					data = modbus.ReadDiscreteInputs(area.start, area.amount);
					System.out.println(HexUtils.bytes2Hex(data)+"><>PPPPPPP2");
//					System.out.println("hhhh");
					break;
				case 3:
					// 保持寄存器
					dataValue=new DataValue();
					data = modbus.ReadHoldingRegisters(area.start, area.amount);
					System.out.println(HexUtils.bytes2Hex(data)+"><>PPPPPPP3");
					dataValue= modbus.ReadHoldingRegistersDataValue(area.start, area.amount);
					break;
				case 4:
					// 输入寄存器
					data = modbus.ReadInputRegisters(area.start, area.amount);
					System.out.println(HexUtils.bytes2Hex(data)+"><>PPPPPPP4");
					break;
				}
				
				area.data = data;
				iecData.setType(new Integer(area.area));
//				System.err.println("<><>"+data.length);
//				iecData.setData(new Integer(byteArrayToInt(data)));
//				System.out.println(byteArrayToInt(data)+">>>>>");
//				iecData.setData(HexUtils.bytes2Hex(data));//转换成16进制；
//				if (area.area==3) {
//					System.out.println(HexUtils.bytes2Hex(data)+"><>PPPPPPP3");
//				}
//				System.out.println(HexUtils.bytes2Hex(data)+"><>PPPPPPP");
				iecData.setSlaveId(modbusConfig.slaveId);
//				iecData.setChange(false);
				list.add(iecData);
			}
			// 输出
			String s = modbusConfig.toData().toString();
			//封装iec数据；
			setIecData(modbusConfig.slaveId,list);
//			System.out.println("end>>><"+modbusConfig.slaveId);
			//写入缓存文件；
//			log.info("modebus-data-yyy:" + s);
//			System.out.println("MODBUSDATA-DATA:"+s);
			//关闭流；
			threadUtil.close(transport);
			transport = null;
			modbusConfig=null;
			//释放写锁；
			
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("MODBUSDATA-ERROR");
			log.debug(e.getMessage());
			threadUtil.close(transport);
			transport = null;
			modbusConfig=null;
			ModbusUtils.getInstance().setWrite(false);
		}
//		threadUtil.sleep(5000);
//		iecTable.put(key,list);
		threadUtil.sleep(5000);
	}
	}
//写入缓存文件；
writeFile();

ModbusUtils.getInstance().setWrite(false);//释放写锁；
	}
	private void writeFile() {
		// TODO Auto-generated method stub
		try {
			
		String str=parseToJson(ModbusUtils.getInstance().dataTable);
//		System.out.println("KKKKKKKKKKK:"+str);
		FileUtils.creatFile(FileConstant.fileURI,FileConstant.fileName,/*System.currentTimeMillis()+*/str);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private String parseToJson(Hashtable iecTable) throws JSONException {
		JSONArray jsonArray=new JSONArray();
		if (null!=iecTable&&iecTable.size()>0) {
			Enumeration en = iecTable.keys();
//			System.out.println();
			JSONObject jsonObject=new JSONObject();
			jsonArray.put(jsonObject);
			
		while (en.hasMoreElements()) {
			Object key = (Object) en.nextElement();
			JSONArray data=new JSONArray();
			jsonObject.put(key+"", data);
			ArrayList list=(ArrayList)(iecTable.get(key));
			for (int i = 0; i < list.size(); i++) {
				JSONObject j=new JSONObject();
				IecData iecData=(IecData) list.get(i);
				j.put("slaveId", iecData.getSlaveId());
				j.put("type", iecData.getType());
//				j.put("data", iecData.getData()==null?"null":iecData.getData());
//				j.put("change", iecData.isChange());
				data.put(j);
			}
			
			
		}
		}
		return jsonArray.toString();
	}
	/**
	 * 封装iec数据；
	 * @param data
	 */
	private   void setIecData(byte slaveId,ArrayList list) {
		// TODO Auto-generated method stub
		if(this.checkData(((ArrayList)	ModbusUtils.getInstance().getDataTable().get(slaveId+"")),list)){
		ModbusUtils.getInstance().getDataTable().put(slaveId+"", list);}
//		int d=byteArrayToInt(data);
		
	}
	/**
	 * 比对两个list中对象属性值是否有改变
	 * @param oldList上一次数据；
	 * @param newList最近一次数据；
	 * @return
	 */
	 private  boolean checkData(ArrayList oldList, ArrayList newList) {
		// TODO Auto-generated method stub
		 boolean flag = false;//默认没有变化；
		 if (newList != null && oldList != null){
	           if (newList.size() != oldList.size()){
	               flag = true;
	           }else{
	        	   
	        	   }
	           }
		 
		 
		 return flag;
		
	}
	/**
     * byte[]转int
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }
    /**
     * int到byte[] 由高位到低位
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

}
