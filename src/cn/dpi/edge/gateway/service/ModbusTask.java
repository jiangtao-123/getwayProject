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
				for (int i = 0; i < this.config.areas.size(); i++) {
					ModbusArea area = (ModbusArea) this.config.areas.get(i);
					System.out.println("area.area"+":"+area.area+"area.start:"+area.start+"area.amount:"+area.amount);
					// 1:线圈 2:离散输入;3:保持寄存器;4:输入寄存器
					switch (area.area) {
					case 1:
						// 线圈
//						System.out.println("data>>>>");
						data = modbus.ReadCoils(area.start, area.amount);
//						System.out.println("read:"+area.area+">>>>>>>"+data.length);
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
				String s = this.config.toData().toString();
				log.info("modebus-data-hh" + s);
				System.out.println("MODBUSDATA-ERROR--"+s);
			} catch (Throwable e) {
				e.printStackTrace();
				System.out.println("MODBUSDATA-ERROR");
				log.debug(e.getMessage());
				threadUtil.close(transport);
				transport = null;
			}
			threadUtil.sleep(5000);
		} while (true);
	}

}
