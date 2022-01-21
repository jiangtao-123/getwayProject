package cn.dpi.edge.gateway.modbus;

import cn.dpi.edge.gateway.api.ICodec;

public abstract class ModbusCodec implements ICodec {
	public final static int MinTransactionID = 0;
	public final static int MaxTransactionID = 0xFFFF;

	private int transactionID = MinTransactionID;

	public synchronized short newTransactionID() {
		this.transactionID++;
		if (this.transactionID >= MaxTransactionID) {
			this.transactionID = MinTransactionID;
		}
		return (short) (this.transactionID & 0xFFFF);
	}
}
