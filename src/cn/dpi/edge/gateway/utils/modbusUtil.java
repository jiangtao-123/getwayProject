package cn.dpi.edge.gateway.utils;

public class modbusUtil {

	public static void readCoils(boolean[] coils, byte[] data) {
		for (int i = 0; i < coils.length; i++) {
			int p = i / 8;
			int r = i % 8;
			byte val = data[p];
			coils[i] = ((val >> r) & 0x1) == 1;
		}
	}
}
