package cn.dpi.edge.gateway.api;

import cn.dpi.edge.gateway.DataValue;

public interface IModbus {

	/**
	 * 线圈读取
	 * 
	 * @param address
	 *            从0开始
	 * @param quantity
	 *            读取位数量
	 * @return
	 * @throws Exception
	 */
	byte[] ReadCoils(int address, int quantity) throws Exception;
	/**
	 * 离散读取
	 * 
	 * @param address
	 *            从0开始
	 * @param quantity
	 *            读取位数量
	 * @return
	 * @throws Exception
	 */
	byte[] ReadDiscreteInputs(int address, int quantity) throws Exception;
	/**
	 * 保持寄存器读取
	 * 
	 * @param address
	 *            从0开始
	 * @param quantity
	 *            读取位数量
	 * @return
	 * @throws Exception
	 */
	byte[] ReadHoldingRegisters(int address, int quantity) throws Exception;
	/**
	 * 输入寄存器读取
	 * 
	 * @param address
	 *            从0开始
	 * @param quantity
	 *            读取位数量
	 * @return
	 * @throws Exception
	 */
	byte[] ReadInputRegisters(int address, int quantity) throws Exception;
	/**
	 * 
	 * @param start
	 * @param amount
	 * @return
	 * @throws Exception 
	 */
	DataValue ReadHoldingRegistersDataValue(int start, int amount) throws Exception;
}
