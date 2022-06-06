package org.nodes.wms.dao.basics.sku.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 批属性验证生成掩码枚举类
 * @author: pengwei
 * @create: 2020-12-18 15:49
 **/
@Getter
@AllArgsConstructor
public enum SkuLotMaskEnum {

	FIXED_VALUE(1, "固定值"),
	BILL_CLIENT_AREA(2, "单据.客户大区"),
	QUALIFIED(3, "合格"),
	BILL_CLIENT_NAME(4, "单据.客户名称"),
	BILL_CLIENT_CODE(5, "单据.客户编码"),
	BILL_SUPPLIER_CODE(6, "单据.供应商编码"),
	BILL_SUPPLIER_NAME(7, "单据.供应商名称"),
	SYSTEM_TIME(8, "系统时间"),
	BILL_ARRIVE_TIME(9, "单据下发时间"),
	INSTOCK_TYPE(10, "入库方式"),
	SUPPLIER_ADDRESS(11, "供应商地址"),
	SUPPLIER_CONTACT(12, "供应商联系人"),
	SUPPLIER_CONTACT_PHONE(13, "供应商联系电话"),
	DEPT_CODE(14, "部门编码"),
	OWNER_CODE(15, "货主编码"),
	UM_CODE(16, "计量单位编码"),
	UM_NAME(17, "计量单位名称"),
	BILL_TYPE_CODE(18, "单据种类编码"),
	WH_CODE(19, "库房编码"),
	SCHEDULED_ARRIVAL_TIME(20, "预计收货时间"),
	PACKAGE_NAME(21, "包装名称"),
	USER_DEFINED(22, "用户自定义"),
	;

	Integer index;
	String name;
}
