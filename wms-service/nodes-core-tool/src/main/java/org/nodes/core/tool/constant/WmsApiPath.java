package org.nodes.core.tool.constant;

/**
 * WMS系统API路径常量类
 */
public class WmsApiPath {

	/**
	 * 根路径
	 */
	public final static String WMS_ROOT_URL = "/wms/";

	/**
	 * 状态
	 */
	public final static String STATE_URL = WmsApiPath.WMS_ROOT_URL+"state";

	/**
	 * 列显隐
	 */
	public final static String NODES_CURD_COLUMN_URL = WmsApiPath.WMS_ROOT_URL+"nodesCurdColumn";

	/**
	 * 系统字典
	 */
	public static final String DICTIONARY_URL = WmsApiPath.WMS_ROOT_URL + "dictionary";

	/**
	 * 物品
	 */
	public static final String SKU_URL = WmsApiPath.WMS_ROOT_URL + "basedata/sku";

	/**
	 * 库房
	 */
	public static final String WAREHOUSE_URL = WmsApiPath.WMS_ROOT_URL +"/warehouse/warehouse";

	/**
	 * 手持前缀
	 */
	public static final String WMS_PDA_API="/api/ApiPDA";

	/**
	 * 天宜定制：调度系统api
	 */
	public static final String SCHEDULING_SYSTEM_API = WmsApiPath.WMS_ROOT_URL + "scheduling";
}
