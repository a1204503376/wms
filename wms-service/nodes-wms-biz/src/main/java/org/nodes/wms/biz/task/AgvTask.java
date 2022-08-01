package org.nodes.wms.biz.task;

/**
 * 天宜定制：agv调度任务
 *
 * @author caiyun
 */
public class AgvTask {

	public void putwayToSchedule() {
		// 已经发送的任务不能再次发送
		// 调用上架策略生成目标库位，并把目标库位保存到任务表中
		// 发送成功之后，冻结目标库位和冻结原库位的库存
	}

	public void moveStockToSchedule() {

	}

	public void pickToSchedule() {

	}
}
