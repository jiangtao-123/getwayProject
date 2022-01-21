package cn.dpi.edge.gateway.api;

import cn.dpi.edge.gateway.bean.States;

public interface ITransport extends AutoCloseable{
	// 连接
	void connect() throws Exception;

	// 发送数据
	byte[] send(byte[] data) throws Exception;

	// State 状态
	States state() throws Exception;
}
