package cn.dpi.edge.gateway.utils;

import cn.dpi.edge.gateway.DataVo;

/**
 * 
 * @author jiangtao
 *
 */
public class HexUtils {
	private static final char[] HEXES = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 * byte数组 转换成 16进制大写字符串
	 */
	public static String bytes2Hex(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		String hex = new String();

		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			hex += (HEXES[(b >> 4) & 0x0F]);
			hex += (HEXES[b & 0x0F]);
		}

		return hex.toUpperCase();
	}

	/**
	 * 16进制字符串 转换为对应的 byte数组
	 */
	public static byte[] hex2Bytes(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		char[] hexChars = hex.toCharArray();
		byte[] bytes = new byte[hexChars.length / 2]; // 如果 hex 中的字符不是偶数个,
														// 则忽略最后一个

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
		}

		return bytes;
	}

	/**
	 * 十六进制字符串转10进制；
	 * 
	 * @param hexStr
	 * @return
	 * @throws Exception
	 */

	public static int hexToInt(String hexStr) throws Exception {
		int bigInteger;
		try {

			bigInteger = Integer.parseInt(hexStr, 16);

			return bigInteger;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * byte[]转int32
	 * 
	 * @param bytes
	 *            bigEndian：true:大端,false:小端
	 * @return
	 */
	public static int byteArrayToInt(byte[] bytes, boolean bigEndian) {
		if (!bigEndian) {
			bytes = revelArr(bytes);
		}
		int value = 0;
		// 由高位到低位
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (bytes[i] & 0x000000FF) << shift;// 往高位游
		}
		return value;
	}

	/**
	 * 字节数组转float32 采用IEEE 754标准 bigEndian：true:大端,false:小端
	 * 
	 * @param bytes
	 * @return
	 */
	public static float bytesToFloat(byte[] bytes, boolean bigEndian) {
		// 获取 字节数组转化成的2进制字符串
		if (!bigEndian) {// 字节高低位转换；
			bytes = revelArr(bytes);
		}
		String BinaryStr = bytes2BinaryStr(bytes);
		// 符号位S
		long s = Long.parseLong(BinaryStr.substring(0, 1));
		// 指数位E
		long e = Long.parseLong(BinaryStr.substring(1, 9), 2);
		// 位数M
		String M = BinaryStr.substring(9);
		float m = 0, a, b;
		for (int i = 0; i < M.length(); i++) {
			a = Float.parseFloat(M.charAt(i) + ""); // (float)Integer.valueOf(M.charAt(i)+"");
			b = (float) pow(2, i + 1);
			m = m + (a / b);
		}
		float f = (float) ((pow(-1, s)) * (1 + m) * (pow(2, (e - 127))));
		return -f;
	}

	/**
	 * 数组高低位转换；
	 * 
	 * @param arr
	 * @return
	 */
	public static byte[] revelArr(byte[] arr) {
		for (int i = 0; i < arr.length / 2; i++) { // 循环数组长度的一半次数即可
			// 两个数组元素互换
			byte temp = arr[i];
			arr[i] = arr[arr.length - i - 1];
			arr[arr.length - i - 1] = temp;
		}
		return arr;
	}

	/**
	 * 将字节数组转换成2进制字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2BinaryStr(byte[] bytes) {
		StringBuffer binaryStr = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String str = Integer.toBinaryString((bytes[i] & 0xFF) + 0x100).substring(1);
			binaryStr.append(str);
		}
		return binaryStr.toString();
	}

	public static double pow(double a, double b) {
		// true if base is greater than 1
		boolean gt1 = (Math.sqrt((a - 1) * (a - 1)) <= 1) ? false : true;
		int oc = -1; // used to alternate math symbol (+,-)
		int iter = 20; // number of iterations
		double p, x, x2, sumX, sumY;
		// is exponent a whole number?
		if ((b - Math.floor(b)) == 0) {
			// return base^exponent
			p = a;
			for (int i = 1; i < b; i++)
				p *= a;
			return p;
		}
		x = (gt1) ? (a / (a - 1))
				: // base is greater than 1
				(a - 1); // base is 1 or less
		sumX = (gt1) ? (1 / x)
				: // base is greater than 1
				x; // base is 1 or less
		for (int i = 2; i < iter; i++) {
			// find x^iteration
			p = x;
			for (int j = 1; j < i; j++)
				p *= x;
			double xTemp = (gt1) ? (1 / (i * p))
					: // base is greater than 1
					(p / i); // base is 1 or less
			sumX = (gt1) ? (sumX + xTemp)
					: // base is greater than 1
					(sumX + (xTemp * oc)); // base is 1 or less
			oc *= -1; // change math symbol (+,-)
		}
		x2 = b * sumX;
		sumY = 1 + x2; // our estimate
		for (int i = 2; i <= iter; i++) {
			// find x2^iteration
			p = x2;
			for (int j = 1; j < i; j++)
				p *= x2;
			// multiply iterations (ex: 3 iterations = 3*2*1)
			int yTemp = 2;
			for (int j = i; j > 2; j--)
				yTemp *= j;
			// add to estimate (ex: 3rd iteration => (x2^3)/(3*2*1) )
			sumY += p / yTemp;
		}
		return sumY; // return our estimate
	}

	/**
	 * bytes数组转为 int16
	 *
	 * @param bytes
	 *            数组
	 * @param start
	 *            起始位
	 * @param isBig
	 *            true:大端模式 false:小端模式
	 * @return 短整型
	 */
	public static short byteArrayToShort(byte[] bytes, int start, boolean isBig) {
		short value = 0;

		if (isBig) {
			value += (bytes[start] & 0x000000FF) << 8;
			value += (bytes[start + 1] & 0x000000FF);
		} else {
			value += (bytes[start] & 0x000000FF);
			value += (bytes[start + 1] & 0x000000FF) << 8;
		}
		return value;
	}

	public static DataVo getModbusData(byte[] data, boolean bigEndian, int dataType, DataVo d) {
		d.setDataType(dataType);
		switch (dataType) {
		case 0:
			d.setByteData(data);
			break;

		case 1:
			d.setShortData(byteArrayToShort(data, 0, bigEndian));
			break;
		case 2:
			d.setIntData(byteArrayToInt(data, bigEndian));
			break;
		default:
			d.setFloatData(bytesToFloat(data, bigEndian));
			break;

		}
		return d;
	}
}
