package cn.dpi.edge.gateway;

import java.io.InputStream;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import cn.dpi.edge.gateway.api.ILog;
import cn.dpi.edge.gateway.component.log.Config;
import cn.dpi.edge.gateway.component.log.FileLog;
import cn.dpi.edge.gateway.service.APPConfig;
import cn.dpi.edge.gateway.service.ModbusConfig;
import cn.dpi.edge.gateway.service.ModbusTask;
import cn.dpi.edge.gateway.utils.streamUtil;
import cn.dpi.edge.gateway.utils.threadUtil;

public class EdgeStudio {

	public static void main(String[] args) throws Exception {
		InputStream stream;
		byte[] data;
		String json;
		ILog log;// = new ConsoleLog();
		System.out.println("read app config");
		stream = EdgeStudio.class.getResourceAsStream("/app.json");
		data = streamUtil.readToEnd(stream);
		threadUtil.close(stream);
		json = new String(data, "UTF-8");
		APPConfig appConfig = new APPConfig();
		appConfig.parse(new JSONObject(json));
		System.out.println("log path=" + appConfig.log.FilePath);
		Config logConfig = new Config();
		logConfig.FilePath = appConfig.log.FilePath;
		log = FileLog.newFileLog(logConfig);
		System.out.println("read app config end");
		// 读取modbus配置文件
		stream = EdgeStudio.class.getResourceAsStream("/MudbusConfig.json");
		data = streamUtil.readToEnd(stream);
		threadUtil.close(stream);
		json = new String(data, "UTF-8");
		JSONArray array = new JSONArray(json);
		for (int i = 0; i < array.length(); i++) {
			ModbusConfig c = new ModbusConfig();
			c.parse(array.getJSONObject(i));
			System.out.println(c.toJson().toString());
			// 根据配置启动读取线程

			String name = "thread-modbus-channel-" + c.id;
			Thread thread = new Thread(new ModbusTask(c, log), name);
			thread.start();
		}
		
		Thread.currentThread().join();
	}

}
