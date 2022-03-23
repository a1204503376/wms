
package org.nodes.wms.core.basedata.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.vo.IdleSkuInfoVO;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物品 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-09
 */
@Primary
public interface SkuMapper extends BaseMapper<Sku> {
	/**
	 * 查询未出库的物品数量
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 未出库的物品
	 */
	List<IdleSkuInfoVO> selectUnOutstockCount(LocalDateTime begin, LocalDateTime end);
}
