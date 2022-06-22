
package org.nodes.wms.core.strategy.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.dto.InstockDTO;
import org.nodes.wms.core.strategy.entity.Instock;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.nodes.wms.core.strategy.vo.InstockVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 上架策略 服务类
 *
 * @author liangmei
 * @since 2019-12-09
 */
public interface IInstockService extends BaseService<Instock> {

	/**
	 * 新增上架策略
	 *
	 * @param instockDTO
	 * @return
	 */
	boolean save(InstockDTO instockDTO);

	/**
	 * 修改上架策略
	 *
	 * @param instockDTO
	 * @return
	 */

	boolean updateById(InstockDTO instockDTO);

	/**
	 * 新增或修改上架策略
	 *
	 * @param instockDTO
	 * @return
	 */

	boolean saveOrUpdate(InstockDTO instockDTO);

	/**
	 * 查询上架策略详情
	 *
	 * @param wrapper 查询条件
	 * @return 上架策略详情
	 */
	InstockVO getOne(Wrapper<Instock> wrapper);

	/**
	 * 执行上架策略
	 *
	 * @param stockList  库存列表
	 * @param billTypeCd 单据类型编码
	 * @return 执行上架策略结果
	 */
	InstockExecuteVO execute(List<Stock> stockList, String billTypeCd);
}
