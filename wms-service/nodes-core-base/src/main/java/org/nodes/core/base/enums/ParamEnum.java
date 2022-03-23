package org.nodes.core.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 系统参数枚举类
 * @since 2020-09-04
 */
@Getter
@AllArgsConstructor
public enum ParamEnum {

	/**
	 * 收货：是否允许超收
	 */
	ASN_IS_OVER("account:isOver"),
	/**
	 * 批属性开放数量
	 */
	SKU_LOT_COUNT("account:lotCount"),
	/**
	 * 系统：手持部署的URL
	 */
	SYSTEM_PDA_URL("system.pda.url"),
	/**
	 * 任务：自动分配：开关
	 */
	TASK_AUTO_ENABLE("account:autoTask"),
	/**
	 * 任务：自动分配：上限
	 */
	TASK_AUTO_UPPER_LIMIT("account:taskCount"),
	/**
	 * 任务：上架任务：模式(1:任务上架, 2:自动上架)
	 */
	TASK_PUTAWAY_MODE("asn:putawayMode"),
	/**
	 * 箱号规则
	 */
	LPN_BOX_RULE("lpn:boxRule"),
	/**
	 * 用户默认密码
	 */
	USER_DEFAULT_PASSWORD("account.initPassword"),
	/**
	 * 出库-拣货分配-提示
	 */
	OUTSTOCK_PICKPLAN_PROMPT("outstock.pickplan.prompt"),
	/**
	 * 盘点-盘盈入库-单据类型编码
	 */
	COUNT_PROFIT_TYPECD("count.profit.typeCd"),
	/**
	 * 盘点-盘亏出库-单据类型编码
	 */
	COUNT_LOSS_TYPECD("count.loss.typeCd"),
	/**
	 * 策略-分配策略-切换模式
	 */
	STRATEGY_ALLOC_MODE("strategy.alloc.mode"),
	/**
	 * 盘点模式：
	 * 0:盲盘, 1:明盘
	 */
	COUNT_MODE("count.mode"),
	/**
	 * 手持-库存查询-箱码关键字
	 */
	PDA_STOCK_QUERY_BOXCODE("pda.stock.keywords"),
	/**
	 * 租户-定时任务-触发间隔（默认：整点）
	 */
	TENANT_TASK_INTERVAL("tenant.task.interval"),
	/**
	 * 清除日志-定时任务-触发间隔
	 */
	CLEAR_LOG_TASK_INTERVAL("clearlog.task.interval"),
	/**
	 * 出库单拣货分配模式：
	 * 0:不自动分配, 1:自动分配，缺货不生成, 2:自动分配，缺货的情况也分配
	 */
	OUTSTOCK_PICKPLAN_MODE("outstock.pickplan.mode"),

	;
	final String key;
}
