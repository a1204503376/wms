package org.nodes.core.constant;

/**
 * 库位常量类
 **/
public class LocationConstant {

	/**
	 * 入库暂存区，属于入库暂存区
	 */
	public static final String LOC_STAGE = "STAGE";

	/**
	 * 入库质检区,属于入库质检区
	 */
	public static final String LOC_QC = "QC";

	/**
	 * 出库集货区，属于出库集货区
	 */
	public static final String LOC_PICKTO = "PICKTO";

	/**
	 * 打包区
	 */
	public static final String LOC_PACK = "PACK";

	/**
	 * 未知库位，属于虚拟库区类型
	 */
	public static final String LOC_UNKNOWN = "UNKNOWN";

	/**
	 * 库内中间库位，属于虚拟库区类型
	 */
	public static final String LOC_INTRANSIT = "INTRANSIT";
	/**
	 * 正常 可以上架库存
	 */
	public static final Integer LOC_FLAG_NORMAL = 1;
	/**
	 * 破损 不能上架库存到该库位
	 */
	public static final Integer LOC_FLAG_DAMAGED = 10;
	/**
	 * 冻结 可以上架库存，但库存状态为冻结
	 */
	public static final Integer LOC_FLAG_FORZEN = 20;
	/**
	 * 系统业务冻结, 不能上架库存到该库位
	 */
	public static final Integer LOC_FLAG_SYSTEM_FORZEN = 40;

	public static String[] getLocTypes() {
		return new String[] {
				LOC_STAGE,
				LOC_QC,
				LOC_PICKTO,
				LOC_UNKNOWN,
				LOC_INTRANSIT
		};
	}
}
