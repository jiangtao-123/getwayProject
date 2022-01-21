package cn.dpi.edge.gateway.utils;

import org.bouncycastle.util.StringList;
import org.bouncycastle.util.Strings;

public class stringUtil {

	public static String format(String format) {
		return format;
	}

	public static String format(String format, Object arg0) {
		return format(format, new Object[] { arg0 });
	}

	public static String format(String format, Object arg0, Object arg1) {
		return format(format, new Object[] { arg0, arg1 });
	}

	public static String format(String format, Object arg0, Object arg1, Object arg2) {
		return format(format, new Object[] { arg0, arg1, arg2 });
	}

	public static String format(String format, Object arg0, Object arg1, Object arg2, Object arg3) {
		return format(format, new Object[] { arg0, arg1, arg2, arg3 });
	}

	public static StringBuffer toHexString(byte[] args, int offset, int length) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < length; i++) {
			String temp = Integer.toHexString(args[offset + i] & 0xFF);
			if (temp.length() == 1) {
				result.append("0");
			}
			result.append(temp.toUpperCase());
		}
		return result;
	}

	public static StringBuffer toHexString(byte[] args) {
		return toHexString(args, 0, args.length);
	}

	public static String format(String format, byte[] args, int offset, int length) {
		StringBuffer result = toHexString(args, offset, length);
		return replace(format, "{}", result.toString());
	}

	public static String format(String format, byte[] args) {
		return format(format, args, 0, args.length);
	}

	public static String replace(String src, String o, String n, int start) {
		StringBuffer list = new StringBuffer();
		int len = o.length();
		int index = start;
		do {
			int temp = src.indexOf(o, index);
			if (temp < 0) {
				list.append(src.substring(index));
				break;
			}
			String t = src.substring(index, temp);
			list.append(t);
			list.append(n);
			index = temp + len;
		} while (true);
		return list.toString();
	}

	public static String replace(String src, String o, String n) {

		return replace(src, o, n, 0);
	}

	public static String format(String format, Object[] args) {
		StringList list = split(format, "{}");
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			result.append(list.get(i));
			if (args.length > i) {
				Object t = args[i];
				if (t != null) {
					result.append(t.toString());
				}
			} else if (i < list.size() - 1) {
				result.append("{}");
			}
		}
		return result.toString();
	}

	public static StringList split(String src, String regex) {
		StringList list = Strings.newList();
		int len = regex.length();
		int index = 0;
		do {
			int temp = src.indexOf(regex, index);
			if (temp < 0) {
				list.add(src.substring(index));
				break;
			}
			String t = src.substring(index, temp);
			list.add(t);
			index = temp + len;
		} while (true);
		return list;
	}

	public static String padingLeft(String src, char fill, int len) {
		int l = len - src.length();
		String result = src;
		for (int i = 0; i < l; i++) {
			result = fill + result;
		}
		return result;
	}

	public static String padingRight(String src, char fill, int len) {
		int l = len - src.length();
		String result = src;
		for (int i = 0; i < l; i++) {
			result = result + fill;
		}
		return result;
	}
}
