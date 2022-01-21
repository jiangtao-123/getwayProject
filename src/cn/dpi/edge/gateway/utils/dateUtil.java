package cn.dpi.edge.gateway.utils;

import java.util.Calendar;
import java.util.Date;

public class dateUtil {
	public static void main(String[] args) {
		String s = formatDateTime(new Date(), true);
		System.out.println(s);
	}

	public static String formatDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return stringUtil.format("{}-{}-{}", new Object[] { pading(year, 4), pading(month), pading(day) });
	}

	public static String formatDateTime(Date date) {
		return formatDateTime(date, false);
	}

	public static String formatDateTime(Date date, boolean hasMillsecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int a_p_am = calendar.get(Calendar.AM_PM);
		if (a_p_am == 1 && hour < 12) {
			hour += 12;
		}
		if (hasMillsecond) {
			int millsecond = calendar.get(Calendar.MILLISECOND);
			return stringUtil.format("{}-{}-{} {}:{}:{} {}", new Object[] { pading(year, 4), pading(month), pading(day),
					pading(hour), pading(minute), pading(second), pading(millsecond, 3) });
		} else {
			return stringUtil.format("{}-{}-{} {}:{}:{}", new Object[] { pading(year, 4), pading(month), pading(day),
					pading(hour), pading(minute), pading(second) });
		}
	}

	private static String pading(int value) {
		return pading(value, 2);
	}

	private static String pading(int value, int len) {
		String src = Integer.toString(value);
		return stringUtil.padingLeft(src, '0', len);
	}

	public static String formatyyyyMMdd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return stringUtil.format("{}{}{}", new Object[] { pading(year, 4), pading(month), pading(day) });
	}
}
