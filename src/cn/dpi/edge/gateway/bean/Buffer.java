package cn.dpi.edge.gateway.bean;

public class Buffer {
	private int index = 0;
	private final byte[] buffer;

	public Buffer(int size) {
		this.buffer = new byte[size];
	}

	public Buffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public Buffer setIndex(int index) {
		this.index = index;
		return this;
	}

	public Buffer skip(int count) {
		this.index += count;
		return this;
	}

	public Buffer write(byte value) {
		this.buffer[index++] = value;
		return this;
	}

	public Buffer write(short value) {
		for (int i = 2; i > 0; i--) {
			this.buffer[index++] = (byte) ((value >> (8 * (i - 1))) & 0xFF);
		}
		return this;
	}

	public Buffer write(int value) {
		for (int i = 4; i > 0; i--) {
			this.buffer[index++] = (byte) ((value >> (8 * (i - 1))) & 0xFF);
		}
		return this;
	}

	public Buffer write(long value) {
		System.out.println("xxxxxxxxxx="+value);
		for (int i = 8; i > 0; i--) {
			this.buffer[index++] = (byte) ((value >> (8 * (i - 1))) & 0xFF);
		}
		return this;
	}

	public byte readByte() {
		byte value = this.buffer[index++];
		return value;
	}

	public short readShort() {
		short value = 0;
		for (int i = 2; i > 0; i--) {
			value = (short) (value | ((this.buffer[index++] & 0xFF) << ((i - 1) * 8)));
		}
		return value;
	}

	public int readInt() {
		int value = 0;
		for (int i = 4; i > 0; i--) {
			value = (int) (value | ((this.buffer[index++] & 0xFF) << ((i - 1) * 8)));
		}
		return value;
	}

	public long readLong() {
		long value = 0;
		for (int i = 8; i > 0; i--) {
			value = (long) (value | ((this.buffer[index++] & 0xFFL) << ((i - 1) * 8)));
		}
		return value;
	}
}
