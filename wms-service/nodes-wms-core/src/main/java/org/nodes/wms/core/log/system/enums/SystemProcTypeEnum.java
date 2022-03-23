package org.nodes.wms.core.log.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 系统日志业务种类
 * @create 20200306
 */
@Getter
@AllArgsConstructor
public enum SystemProcTypeEnum {
	ASN("入库", 0),
	SO("出库", 10),
	MOVE("移库", 30),
	PICK_PLAN("拣货计划", 40),
	PICK("拣货", 50),
	TURN("播包", 60),
	COUNT("盘点", 80),
	LOADING("装车", 90),
	STOCK_PACK("尾箱打包", 100),
	TASK("任务", 110),
	ERP_ASN("ERP-入库单", 200),
	ERP_SO("ERP-出库单", 210),
	ERP_BILL_TYPE("ERP-单据类型", 220),
	ERP_PUB_ENTERPRISE("ERP-企业接口", 230),
	ERP_SKU("ERP-物品", 240),
	ERP_SKU_PACKAGE("ERP-物品包装", 250),
	ERP_SKU_TYPE("ERP-物品分类", 260),
	ERP_SKU_UM("ERP-物品单位", 270),
	ERP_WAREHOUSE("ERP-仓库", 280),
	ACCOUNT_SUBJECT("会计科目", 290),
	BARCODE("企业条码关联", 300),
	BILL_TYPE("单据类型", 310),
	ENTERPRISE("企业", 320),
	LABELCOLUMN("字段标签", 330),
	LABEL("标签", 340),
	LABEL_ENTERPRISE("企业标签关联", 350),
	OWNER("企业标签关联", 360),
	SKU("物品", 370),
	SKU_INC("物品供应商", 380),
	SKU_INCSTOCK("物品入库设置", 390),
	SKU_LOT("批属性", 400),
	SKU_LOT_VAL("批属性验证", 410),
	SKU_OUTSTOCK("出库设置", 420),
	SKU_PACKAGE("包装", 430),
	SKU_PACKAGE_DETAIL("包装明细", 440),
	SKU_TYPE("物品分类", 450),
	SKU_UM("物品单位", 460),
	LOCK_STOCK("库存锁定", 470),
	UNLOCK_STOCK("库存解锁", 471),
	LOCK_LOT("批次锁定", 475),
	UNLOCK_LOT("批次解锁",476),
	COMPLATED_OUTSTOCK("完成出库", 480),
	REL("补货", 500),
	;

	String name;
	int index;

}
