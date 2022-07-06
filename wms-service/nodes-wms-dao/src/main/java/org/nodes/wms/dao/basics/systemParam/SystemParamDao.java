package org.nodes.wms.dao.basics.systemParam;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.service.IParamService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


public interface SystemParamDao {

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
