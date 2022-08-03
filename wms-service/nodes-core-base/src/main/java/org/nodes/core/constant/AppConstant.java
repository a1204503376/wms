package org.nodes.core.constant;

public interface AppConstant {

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

	Integer Loading = 10;//正在装车
}
