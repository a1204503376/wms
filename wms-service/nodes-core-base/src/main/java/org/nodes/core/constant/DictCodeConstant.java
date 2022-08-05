package org.nodes.core.constant;

/**
 * 字典常量
 *
 * @author zhuangqian
 */
public interface DictCodeConstant {

	String SEX_CODE = "sex";

	String NOTICE_CODE = "notice";

	String MENU_CATEGORY_CODE = "menu_category";

	String BUTTON_FUNC_CODE = "button_func";

	String YES_NO_CODE = "yes_no";

	String FLOW_CATEGORY_CODE = "flow_category";

	/**
	 * 企业类型
	 */
	String ENTERPRISE_TYPE = "enterprise_type";
	/**
	 * 模板类别
	 */
	String SPT_TYPE = "spt_type";
	/**
	 * 欣天新-标签类型
	 */
	String XDC_LABEL_TYPE = "xdc_label_type";
	/**
	 * 尾箱打包状态
	 */
	String STOCK_PACK_STATE = "stock_pack_state";
	/**
	 * 上架策略类型
	 */
	String SSI_PROC_TYPE = "ssi_proc_type";
	/**
	 * 周转方式
	 */
	String TURNOVER_TYPE = "turnover_type";
	/**
	 * 周转类型
	 */
	String TURNOVER_ITEM = "turnover_item";
	/**
	 * 库位类型
	 */
	String LOC_TYPE = "loc_type";
	/**
	 * 库位种类
	 */
	String LOC_CATEGORY = "loc_category";
	/**
	 * 库位处理
	 */
	String LOC_HANDLING = "loc_handling";
	/**
	 * 使用状态
	 */
	String LOC_FLAG = "loc_flag";
	/**
	 * 库位ABC分类
	 */
	String LOC_ABC = "abc";

	/**
	 * 库内移动单据类型
	 */
	String TRANSFER_BILL_TYPE = "transfer_bill_type";

	/**
	 * 物品包装层级
	 */
	String SKU_LEVEL = "sku_level";
	/**
	 * 出库方式
	 */
	String OUTSTORE_TYPE = "outstore_type";
	/**
	 * 发货方式
	 */
	String SO_TRANSPORT_CODE = "so_transport_code";
	/**
	 * 库区类型
	 */
	String ZONE_TYPE = "zone_type";
	/**
	 * 库区类型:不合格品区
	 */
	Integer ZONE_TYPE_UNACCEPTED_PRODUCT_AREA = 50;
	/**
	 * 库区类型:虚拟库区
	 */
	Integer ZONE_TYPE_VIRTUAL_AREA = 70;

	/**
	 * 库区类型:备货区
	 */
	Integer ZONE_TYPE_CHOICE_AREA = 71;
	/**
	 * 库区类型:翻包区
	 */
	Integer ZONE_TYPE_FLAP_BAG_AREA = 72;
	/**
	 * 库区类型:拣货区
	 */
	Integer ZONE_TYPE_PICKING_AREA = 75;
	/**
	 * 库区类型:质检区
	 */
	Integer ZONE_TYPE_QC_AREA = 76;
	/**
	 * 库区类型:储存区
	 */
	Integer ZONE_TYPE_STORAGE_AREA = 77;
	/**
	 * 库区类型:自动化存储区
	 */
	Integer ZONE_TYPE_AUTOMATION_STORAGE_AREA = 78;
	/**
	 * 库区类型:自动化拣货区
	 */
	Integer ZONE_TYPE_AUTOMATION_PICKING_AREA = 79;
	/**
	 * 库区类型:自动化备货区
	 */
	Integer ZONE_TYPE_AUTOMATION_CHOICE_AREA = 80;
	/**
	 * 库区类型:自动化临时区
	 */
	Integer ZONE_TYPE_AUTOMATION_TEMPORARY_AREA = 81;
	/**
	 * 库区类型:出库集货区
	 */
	Integer ZONE_TYPE_OF_PICK_TO = 110;
	/**
	 * 库区类型:入库暂存区
	 */
	Integer ZONE_TYPE_OF_STAGE = 120;
	/**
	 * 库区类型:入库质检区
	 */
	Integer ZONE_TYPE_OF_INSTOCK_QC = 130;
	/**
	 * 入库方式
	 */
	String INSTORE_TYPE = "instore_type";
	/**
	 * 批属性验证
	 */
	String SKU_LOT_VAL = "sku_lot_val";
	/**
	 * 清点状态
	 */
	String ACL_STATE = "acl_state";
	/**
	 * 盘点单状态
	 */
	String STOCK_COUNT_STATE = "stockcount_state";
	/**
	 * 盘点标签
	 */
	String STOCK_COUNT_TYPE = "stockcount_type";
	/**
	 * 分配策略执行代码
	 */
	String OUTSTOCK_FUNCTION = "outstock_function";
	/**
	 * 移动类型
	 */
	String TRANSFER_TYPE = "transfer_type";
	/**
	 * 上架策略计算代码
	 */
	String INSTOCK_FUNCTION = "instock_function";
	/**
	 * 存货类型
	 */
	String INVENTORY_TYPE = "inventory_type";
	/**
	 * 库存状态
	 */
	String STOCK_STATUS = "stock_status";
	/**
	 * HTTP 请求方式
	 */
	String HTTP_METHOD = "http_method";

	/**
	 * 保质期类型
	 */
	String QUALITY_TYPE = "quality_type";
	/**
	 * 发运状态
	 */
	String SHIP_STATE = "ship_state";
	/**
	 * 地址类型
	 */
	String ADDRESS_TYPE = "address_type";
	/**
	 * 数据项目
	 */
	String DATA_ITEM = "data_item";
	/**
	 * 入库单状态
	 */
	String ASN_BILL_STATE = "asn_bill_state";
	/**
	 * 同步状态
	 */
	String SYNC_STATE = "sync_state";
	/**
	 * 创建类型
	 */
	String CREATE_TYPE = "create_type";
	/**
	 * 收货明细接收状态
	 */
	String DETAIL_STATUS = "detail_status";
	/**
	 * 是否为序列号管理
	 */
	String IS_SN = "is_sn";
	/**
	 * 出库明细状态
	 */
	String OUTSTOCK_STATUS = "outstock_status";
	/**
	 * 出库单状态
	 */
	String SO_BILL_STATE = "so_bill_state";
	/**
	 * 过账状态
	 */
	String POST_STATE = "post_state";
	/**
	 * 批属性运算符
	 */
	String SKU_LOT_OPERATION = "sku_lot_operation";
	/**
	 * 订单扩展字段运算符
	 */
	String BILL_UDF_OPERATION = "bill_udf_operation";
	/**
	 * 调拨单状态
	 */
	String ALLOT_BILL_STATE = "allot_bill_state";
	/**
	 * 月台分类
	 */
	String PLATFORM_TYPE = "platform_type";

	String TASK_PROC_TYPE = "task_proc_type";

	String TASK_TYPE = "task_type";
	/**
	 * 库位状态
	 */
	String LOC_STATUS = "loc_status";
	/**
	 * 任务策略推送方式
	 */
	String TASK_PUSH_WAY = "task_push_way";
	/**
	 * 任务策略计算方式
	 */
	String TASK_COUNT_WAY = "task_count_way";
	/**
	 * 波次策略订单字段
	 */
	String ST_WELLEN_FIELD = "st_wellen_field";
	/**
	 * 波次策略操作符
	 */
	String ST_WELLEN_OPERATION = "st_wellen_operation";
	/**
	 * 波次策略订单字段
	 */
	String ST_WELLEN_CRITERIA = "st_wellen_criteria";
	/**
	 * 计费协议执行状态
	 */
	String BILLING_EXECUTE_STATE = "billing_execute_state";
	/**
	 * 计费协议状态
	 */
	String BILLING_AGREEMENT_STATE = "billing_agreement_state";
}
