package org.nodes.wms.biz.basics.systemParam;

import org.nodes.core.base.entity.Param;

import java.math.BigDecimal;

public interface SystemParamBiz {
	/**
	 * 获取列的最大载重
	 * @return
	 */
	BigDecimal findMaxLoadWeightOfColumn();

	/**
	 * 参数管理-新增或修改
	 * @param param Param对象
	 * @return 是否成功
	 */
	public Boolean saveOrUpdate(Param param);

	/**
	 * 根据参数的key获取参数
	 *
	 * @param key 键
	 * @return Param
	 */
	public Param selectByKey(String key);

	/**
	 * 移除参数
	 * @param ids 参数id集合
	 * @return 是否成功
	 */
	public Boolean delete(String ids);
}
