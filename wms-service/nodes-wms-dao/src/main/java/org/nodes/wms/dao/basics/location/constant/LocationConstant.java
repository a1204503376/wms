package org.nodes.wms.dao.basics.location.constant;

/**
 * 库位常量类
 **/
public class LocationConstant {

	/**
	 * 入库暂存区
	 */
	public static final String LOC_STAGE = "STAGE";

	/**
	 * 入库质检区
	 */
	public static final String LOC_QC = "QC";

	/**
	 * 出库集货区
	 */
	public static final String LOC_PICKTO = "PICKTO";

	/**
	 * 打包区
	 */
	public static final String LOC_PACK = "PACK";

	/**
	 * 未知库位
	 */
	public static final String LOC_UNKNOWN = "UNKNOWN";

	/**
	 * 库内的在途库存的库位
	 */
	public static final String LOC_INTRANSIT = "INTRANSIT";

	public static String[] getLocTypes(){
		return new String[]{
			LOC_STAGE,
			LOC_QC,
			LOC_PICKTO,
			LOC_PACK,
			LOC_UNKNOWN,
			LOC_INTRANSIT
		};
	}
}
