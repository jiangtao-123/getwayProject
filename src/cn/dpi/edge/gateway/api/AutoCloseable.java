package cn.dpi.edge.gateway.api;

public interface AutoCloseable {
	/**
	 * 关闭
	 */
	void close() throws Exception;
}
