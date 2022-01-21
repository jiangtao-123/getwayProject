package cn.dpi.edge.gateway.component.log;

import java.util.Date;

import cn.dpi.edge.gateway.api.ILog;
import cn.dpi.edge.gateway.utils.dateUtil;

public class ConsoleLog implements ILog {

	public void info(String message) {
		System.out.println(dateUtil.formatDateTime(new Date(), true) + " INFO " + message);
	}

	public void debug(String message) {
		System.err.println(dateUtil.formatDateTime(new Date(), true) + " DEBUG " + message);
	}
}
