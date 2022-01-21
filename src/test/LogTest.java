package test;

import cn.dpi.edge.gateway.component.log.Config;
import cn.dpi.edge.gateway.component.log.FileLog;
import cn.dpi.edge.gateway.utils.threadUtil;

public class LogTest {
	public static void main(String[] args) {
		Config config = new Config();
		config.FilePath = "/Phone";
		config.Debug = false;
		FileLog l = FileLog.newFileLog(config);
		System.out.println(l == null);
		while (true) {
			l.info("infoxxx");
			l.debug("debugxxx");
			threadUtil.sleep(1000);
		}
		// l.close();
	}
}