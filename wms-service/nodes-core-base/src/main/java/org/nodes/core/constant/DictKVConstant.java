package org.nodes.core.constant;


/**
 * 字典key、value常量，需要配合DictCodeConstant使用
 *
 * @author nodesc
 */
public interface DictKVConstant {

	/**
	 * 库区类型(zone_type):不合格品区
	 */
	Integer ZONE_TYPE_NONCONFORMING = 50;
	/**
	 * 库区类型(zone_type):虚拟库区
	 */
	Integer ZONE_TYPE_VIRTUAL = 70;
	/**
	 * 库区类型(zone_type):备货区
	 */
	Integer ZONE_TYPE_CHOICE = 71;
	/**
	 * 库区类型(zone_type):翻包区
	 */
	Integer ZONE_TYPE_PACK = 72;
	/**
	 * 库区类型(zone_type):入库暂存区
	 */
	Integer ZONE_TYPE_STAGE = 73;
	/**
	 * 库区类型(zone_type):出库集货区
	 */
	Integer ZONE_TYPE_PICK_TO = 74;
	/**
	 * 库区类型(zone_type):拣货区
	 */
	Integer ZONE_TYPE_PICK = 75;
	/**
	 * 库区类型(zone_type):入库质检区
	 */
	Integer ZONE_TYPE_INSTOCK_QC = 76;
	/**
	 * 库区类型(zone_type):存储区
	 */
	Integer ZONE_TYPE_STORAGE = 77;
	/**
	 * 库区类型(zone_type):自动化存储区
	 */
	Integer ZONE_TYPE_AGV_STORAGE = 80;
	/**
	 * 库区类型(zone_type):自动化拣货区
	 */
	Integer ZONE_TYPE_AGV_PICK = 81;
	/**
	 * 库区类型(zone_type):自动化备货区
	 */
	Integer ZONE_TYPE_AGV_CHOICE = 82;
	/**
	 * 库区类型(zone_type):自动化临时区
	 */
	Integer ZONE_TYPE_AGV_TEMPORARY = 83;

	/**
	 * 库位使用状态(loc_flag)：正常 可以上架库存
	 */
	Integer LOC_FLAG_NORMAL = 1;
	/**
	 * 库位使用状态(loc_flag)：破损 不能上架库存到该库位
	 */
	Integer LOC_FLAG_DAMAGED = 10;
	/**
	 * 库位使用状态(loc_flag)：冻结 可以上架库存，但库存状态为冻结
	 */
	Integer LOC_FLAG_FORZEN = 20;
	/**
	 * 库位使用状态(loc_flag)：系统业务冻结, 不能上架库存到该库位
	 */
	Integer LOC_FLAG_SYSTEM_FORZEN = 40;
}
