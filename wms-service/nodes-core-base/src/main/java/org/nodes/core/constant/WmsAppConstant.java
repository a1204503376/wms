package org.nodes.core.constant;

/**
 * 应用程序常量
 *
 * @author nodesc
 */
public interface WmsAppConstant {

	String BASE_PACKAGES = "org.springblade";

	/**
	 * 系统全局默认 false 值
	 */
	Integer FALSE_DEFAULT = 0;

	/**
	 * 系统全局默认 true 值
	 */
	Integer TRUE_DEFAULT = 1;

	/**
	 * 系统全局默认true的字符值
	 */
	String TRUE_DEFAULT_STRING = "1";

	/**
	 * 系统全局默认false的字符值
	 */
	String FALSE_DEFAULT_STRING = "0";

	/**
	 * 单据类型：销售退回
	 */
	String BILL_TYPE_SALE_RETURN = "SR";

	/**
	 * 单据类型：归还入库
	 */
	String BILL_TYPE_RETURN = "RR";

	/**
	 * 单据类型：借用出库
	 */
	String BILL_TYPE_LEND = "BO";

	/**
	 * app name
	 */
	String APPLICATION_NAME = org.springblade.core.launch.constant.AppConstant.APPLICATION_NAME_PREFIX + "api";

	/**
	 * sword 系统名
	 */
	String SWORD_NAME = "sword";

	/**
	 * saber 系统名
	 */
	String SABER_NAME = "saber";

	/**
	 * 顶级父节点id
	 */
	Long TOP_PARENT_ID = 0L;

	/**
	 * 顶级父节点名称
	 */
	String TOP_PARENT_NAME = "顶级";

	/**
	 * 未封存状态值
	 */
	Integer NOT_SEALED_ID = 0;

	/**
	 * 默认密码
	 */
	String DEFAULT_PASSWORD = "123456";

	/**
	 * 数据权限类型
	 */
	Integer DATA_SCOPE_CATEGORY = 1;

	/**
	 * 接口权限类型
	 */
	Integer API_SCOPE_CATEGORY = 2;

	/**
	 * 正在装车
	 */
	Integer Loading = 10;

	/**
	 * 批属性：批属性必填
	 */
	Integer SKU_LOT_MUST = 1;

	/**
	 * 批属性：批属性的字段名称前缀
	 */
	String SKU_LOT_FIELD_PREFIX = "skuLot";

	/**
	 * 批属性：批属性的总个数
	 */
	int SKU_LOT_TOTAL_NUM = 30;

	/**
	 * 入库暂存区，属于入库暂存区
	 */
	String LOC_STAGE = "STAGE";

	/**
	 * 入库质检区,属于入库质检区
	 */
	String LOC_QC = "QC";

	/**
	 * 出库集货区，属于出库集货区
	 */
	String LOC_PICKTO = "PICKTO";

	/**
	 * 打包区
	 */
	String LOC_PACK = "PACK";

	/**
	 * 未知库位，属于虚拟库区类型
	 */
	String LOC_UNKNOWN = "UNKNOWN";

	/**
	 * 库内中间库位，属于虚拟库区类型
	 */
	String LOC_INTRANSIT = "INTRANSIT";

	/**
	 * 系统创建的默认库位
	 */
	String[] LOC_BY_SYSTEM_CREATED =
		new String[]{LOC_STAGE, LOC_QC, LOC_PICKTO, LOC_UNKNOWN, LOC_INTRANSIT};

	/**
	 * 虚拟库区
	 */
	String ZONE_VIRTUAL = "VIRTUAL";

	/**
	 * 系统自动创建的默认库区
	 */
	String[] ZONE_BY_SYSTEM_CREATED =
		new String[]{LOC_STAGE, LOC_QC, LOC_PICKTO, ZONE_VIRTUAL};

	/**
	 * D箱人工拣货区(天宜定制)
	 */
	String ZONE_CODE_D_PICK_AREA = "WH1-D";
	/**
	 * agv发货接驳区(天宜定制)
	 */
	String ZONE_CODE_AGV_SHIPMENT_CONNECTION_AREA = "WH1-AGV-PICKTO";
	/**
	 * agv拣货区(天宜定制)
	 */
	String ZONE_CODE_AGV = "WH1-AGV";
	/**
	 * D箱编码(天宜定制)
	 */
	String BOX_TYPE_D = "D";
	/**
	 * BC箱是否通用键值
	 */
	String BC_ON_AGV_PICK = "bc_on_agv_pick";
}
