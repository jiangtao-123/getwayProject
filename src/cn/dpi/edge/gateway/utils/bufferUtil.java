package cn.dpi.edge.gateway.utils;

import cn.dpi.edge.gateway.bean.Buffer;

public class bufferUtil {

	public static void main(String[] args) {
		 byte[] temp = new byte[20];
		 Buffer buffer = new Buffer(temp);
		 buffer.write((byte) 1);
		 buffer.write((short) 2);
		 buffer.write(0xFFFFFFFF);
		 buffer.write(0xFFFFFFFFL);
		 System.out.println(stringUtil.format("{}",temp));
		 buffer.setIndex(0);
		 System.out.println(buffer.readByte());
		 System.out.println(buffer.readShort());
		 System.out.println(buffer.readInt());
		 System.out.println(buffer.readLong());
	}
}
