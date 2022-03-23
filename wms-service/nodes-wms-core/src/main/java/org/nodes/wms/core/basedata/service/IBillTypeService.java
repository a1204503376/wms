package org.nodes.wms.core.basedata.service;

import org.nodes.wms.core.basedata.entity.BillType;
import org.springblade.core.mp.base.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 单据类型 服务类
 *
 * @author NodeX
 * @since 2019-12-24
 */
public interface IBillTypeService extends BaseService<BillType> {

	/**
	 *
	 * @param ids 传入主键集合
	 * @return 是否成功删除单据类型
	 */
	boolean removeByIds(List<Long> ids);

	/**
	 *
	 * @param billType 传入单据类型
	 * @return 是否成功保存单据类型
	 */
	boolean saveOrUpdate(BillType billType);
}
