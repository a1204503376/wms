package org.nodes.wms.core.log.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 业务动作枚举
 * @author: pengwei
 * @create: 2020-11-14 16:07
 **/
@Getter
@AllArgsConstructor
public enum ActionEnum {

	CREATE(0, "创建"),
	DELETE(1, "删除"),
	UPDATE(2, "更新"),
	CLOSE(3, "关闭"),
	CANCEL(4, "取消"),
	RECEIVE_BASE(10, "按件收货"),
	RECEIVE_LPN(11, "按托收货"),
	RECEIVE_BOX(12, "按箱收货"),
	RECEIVE_ORDER(13, "按单收货"),

	PUTAWAY_LPN(20, "整托上架"),
	PUTAWAY_BOX(21, "按箱上架"),

	PICK_BASE(30, "按件拣货"),
	PICK_LPN(31, "按托拣货"),
	PICK_BOX(32, "按箱拣货"),
	PICK_MULTI(33, "二次分拣"),

	PLAN_BILL(40, "按单分配"),
	PLAN_WELLEN(41, "波次分配"),
	PLAN_ROLLBACK(42, "取消分配"),
	PLAN_DETAIL(43, "明细分配"),

	MOVE_SKU(50, "物品移动"),
	MOVE_BOX(51, "按箱移动"),
	MOVE_LPN(52, "按托移动"),

	STOCK_PACK_PICK(60, "库内打包拣货"),
	STOCK_PACK_TASK(61, "库内打包任务创建"),
	STOCK_SHARE_LPN(65, "拼托"),

	REPLENISH_CREATE(70, "补货创建"),
	REPLENISH_SUBMIT(71, "补货提交"),

	COUNT_STATIC(80, "静态盘点"),
	COUNT_RANDOM(81, "随机盘点"),
	COUNT_DIFF_CREATE(82, "生成差异报告"),

	LOADING_EXE(90, "执行装车"),
	LOCK_STOCK(100, "库存锁定"),
	LOCK_LOT(101, "批次锁定"),
	UNLOCK_STOCK(102, "库存解锁"),
	UNLOCK_LOT(103,"批次解锁"),
	COMPLATED_OUTSTOCK(110, "完成出库"),
	;

	Integer index;
	String name;
}
