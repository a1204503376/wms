
package org.nodes.wms.core.strategy.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.nodes.wms.core.strategy.dto.RelenishmentDTO;
import org.nodes.wms.core.strategy.entity.Relenishment;
import org.nodes.wms.core.strategy.vo.RelenishmentVO;
import org.springblade.core.mp.base.BaseService;

/**
 * 补货策略 服务类
 *
 * @author liangmei
 * @since 2019-12-09
 */
public interface IRelenishmentService extends BaseService<Relenishment> {

	/**
	 * 新增补货策略
	 *
	 * @param relenishmentDTO
	 * @return
	 */
	boolean save(RelenishmentDTO relenishmentDTO);

	/**
	 * 修改补货策略
	 *
	 * @param relenishmentDTO
	 * @return
	 */

	boolean updateById(RelenishmentDTO relenishmentDTO);

	/**
	 * 新增或修改补货策略
	 *
	 * @param relenishmentDTO
	 * @return
	 */

	boolean saveOrUpdate(RelenishmentDTO relenishmentDTO);

	/**
	 * 查询补货策略详情
	 *
	 * @param wrapper 查询条件
	 * @return 补货策略详情
	 */
	RelenishmentVO getOne(Wrapper<Relenishment> wrapper);

}
