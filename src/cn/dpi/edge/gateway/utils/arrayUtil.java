package cn.dpi.edge.gateway.utils;

public class arrayUtil {
	public static void copy(byte[] src, int srcStart, byte[] des, int desStart, int length) {
		for (int i = 0; i < length; i++) {
			des[desStart + i] = src[srcStart + i];
		}
	}

	public static void copy(byte[] src, int srcStart, byte[] des, int desStart) {
		int length = des.length - desStart;
		copy(src, srcStart, des, desStart, length);
	}

	public static byte[] clone(byte[] src, int srcStart, int length) {
		byte[] temp = new byte[length];
		copy(src, srcStart, temp, 0, length);
		return temp;
	}

	public static byte[] clone(byte[] src, int srcStart) {
		int length = src.length - srcStart;
		return clone(src, srcStart, length);
	}
}
