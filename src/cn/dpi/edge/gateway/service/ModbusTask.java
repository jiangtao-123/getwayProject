package cn.dpi.edge.gateway.service;

import cn.dpi.edge.gateway.api.ICodec;
import cn.dpi.edge.gateway.api.ILog;
import cn.dpi.edge.gateway.api.IModbus;
import cn.dpi.edge.gateway.api.ITransport;
import cn.dpi.edge.gateway.modbus.Modbus;
import cn.dpi.edge.gateway.utils.threadUtil;

public class ModbusTask implements Runnable {
	private final ModbusConfig config;
	private final ILog log;

	public ModbusTask(ModbusConfig config, ILog log) {
		this.config = config;
		this.log = log;
	}

	public void run() {
		ITransport transport = null;
		do {
			try {
				if (transport == null) {
					transport = this.config.modbusTransport(log);
				}
				ICodec codec = this.config.modbusCodec();
				transport.connect();
				IModbus modbus = Modbus.newModbus(this.config.slaveId, transport, codec);
				byte[] data = null;
				for (int i = 0; i < this.config.areas.length; i++) {
					ModbusArea area = this.config.areas[i];
					// 1:线圈 2:离散输入;3:保持寄存器;4:输入寄存器
					switch (area.area) {
					case 1:
						// 线圈
						data = modbus.ReadCoils(area.start, area.amount);
						break;
					case 2:
						// 离散输入
						data = modbus.ReadDiscreteInputs(area.start, area.amount);
						break;
					case 3:
						// 保持寄存器
						data = modbus.ReadHoldingRegisters(area.start, area.amount);
						break;
					case 4:
						// 输入寄存器
						data = modbus.ReadInputRegisters(area.start, area.amount);
						break;
					}
					area.data = data;
				}
				// 输出
				log.info("MODBUSDATA-" + this.config.toData().toString());
			} catch (Throwable e) {
				log.debug(e.getMessage());
				threadUtil.close(transport);
				transport = null;
			}
			threadUtil.sleep(5000);
		} while (true);
	}

}
