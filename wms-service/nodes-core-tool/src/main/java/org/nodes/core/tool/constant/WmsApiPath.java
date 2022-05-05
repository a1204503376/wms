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
}
