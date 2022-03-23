package org.nodes.wms.core.log.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 数据项目类型枚举
 * @create 20200310
 */
@Getter
@AllArgsConstructor
public enum DataTypeEnum {
	WellenNo(10, "波次编码"),
	TaskNo(20, "任务编码"),
	SO_BILL_NO(30, "出库单编码"),
	AsnBillNo(40, "入库单编码"),
	CountBillNo(50, "盘点编码"),
	LoddingBillNo(60, "装车单编码"),
	StockPackGroupNo(70, "尾箱打包分组编码"),
	TransferBillId(80, "库内移动表头ID"),
	BillTypeNo(90, "单据类型编码"),
	EnterpriseNo(100, "企业编码"),
	SkuNo(110, "物品编码"),
	SkuPackageNo(120, "物品包装编码"),
	SkuTypeNo(130, "物品分类编码"),
	SkuUmNo(140, "物品单位编码"),
	WareHouseNo(150, "仓库编码"),
	AccountSubjectNo(160, "会计科目编码"),
	BarCodeNo(170, "企业条码关联"),
	LabelColumnNo(180, "字段标签编码"),
	LabelNo(190, "标签编码"),
	LabelEnterpriseNo(200, "企业标签关联编码"),
	OwnerNo(210, "货主编码"),
	SkuIncNo(220, "物品供应商编码"),
	SkuIncStockNo(230, "物品入库编码"),
	SkuLotNo(240, "批属性"),
	SkuLotValNo(250, "批属性验证"),
	SkuOutstockNo(260, "出库设置编码"),
	SkuPackageDetailNo(270, "物品包装明细编码"),
	StockId(280, "库存ID"),
	STOCK_DETAIL_ID(281, "库存明细ID"),
	LotId(290, "批次ID"),
	RelBillNo(300, "补货编码"),
	;


	private int index;
	private String name;
}
