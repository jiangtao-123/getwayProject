package cn.dpi.edge.gateway.api;

import cn.dpi.edge.gateway.bean.ModbusPDU;

public interface ICodec {
	// 编码
	byte[] encode(ModbusPDU pdu, byte slaveID);

	/**
	 * 数据验证
	 * @param aduRequest 请求数据包
	 * @param aduResponse 响应数据包
	 */
	void verify(byte[] aduRequest, byte[] aduResponse);

	/**
	 * 解码
	 * @param adu 数据包
	 * @return
	 */
	ModbusPDU decode(byte[] adu);
}
