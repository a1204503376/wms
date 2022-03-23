package org.nodes.wms.core.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author NodeX
 * @since 2020-02-10
 */
@Primary
public interface SoDetailMapper extends BaseMapper<SoDetail> {
	/**
	 * 拣货计划id查询出库单明细
	 * @param skuId
	 * @param pickPlanId
	 * @return
	 */
	List<SoDetail> selectSodetailsByPickPlanId(Long pickPlanId);
}
