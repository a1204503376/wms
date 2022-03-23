
package org.nodes.wms.core.warehouse.service;

import org.nodes.wms.core.warehouse.entity.WorkArea;
import org.springblade.core.mp.base.BaseService;

/**
 * 工作区服务类
 *
 * @author liangmei
 * @since 2019-12-09
 */
public interface IWorkAreaService extends BaseService<WorkArea> {
	/**
	 * 保存工作区
	 * @param workArea
	 * @return
	 */
	boolean save(WorkArea workArea);

	/**
	 * 修改工作区
	 * @param workArea
	 * @return
	 */
	boolean updateById(WorkArea workArea);
}
