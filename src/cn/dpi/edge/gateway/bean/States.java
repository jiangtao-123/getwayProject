package cn.dpi.edge.gateway.bean;

/**
 * 状态
 * 
 * @author aimin
 *
 */
public class States {
	private final int state;

	/**
	 * 获取状态
	 * 
	 * @return
	 */
	public int getState() {
		return this.state;
	}

	private States(int state) {
		this.state = state;
	}

	/**
	 * 初始化
	 */
	private final static int _Normal = 0;
	/**
	 * 启动中
	 */
	private final static int _Starting = 1;
	/**
	 * 已启动
	 */
	private final static int _Started = 2;
	/**
	 * 停止中
	 */
	private final static int _Stoping = 3;
	/**
	 * 已经停止
	 */
	private final static int _Stoped = 4;

	/**
	 * 初始化
	 */
	public final static States Normal = new States(_Normal);
	/**
	 * 启动中
	 */
	public final static States Starting = new States(_Starting);
	/**
	 * 已启动
	 */
	public final static States Started = new States(_Started);
	/**
	 * 停止中
	 */
	public final static States Stoping = new States(_Stoping);
	/**
	 * 已经停止
	 */
	public final static States Stoped = new States(_Stoped);

	/**
	 * 封装 状态
	 * 
	 * @param codestate
	 * @return
	 */
	public static States from(int state) {
		States temp = null;
		switch (state) {
		case _Normal:
			temp = Normal;
			break;
		case _Starting:
			temp = Starting;
			break;
		case _Started:
			temp = Started;
			break;
		case _Stoping:
			temp = Stoping;
			break;
		case _Stoped:
			temp = Stoped;
			break;
		}
		return temp;
	}

	public String toString() {
		String temp = null;
		switch (this.state) {
		case _Normal:
			temp = "Normal";
			break;
		case _Starting:
			temp = "Starting";
			break;
		case _Started:
			temp = "Started";
			break;
		case _Stoping:
			temp = "Stoping";
			break;
		case _Stoped:
			temp = "Stoped";
			break;
		}
		return temp;
	}
}
