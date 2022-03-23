package org.nodes.wms.statistics.mapper;

import com.baomidou.mybatisplus.core.mapper.Mapper;
import org.nodes.wms.statistics.vo.SkuOutstockSummaryVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 统计查询 Mapper 接口
 *
 * @author pengwei
 * @since 2020-07-03
 */
@Primary
public interface StatisticsMapper extends Mapper {

	List<SkuOutstockSummaryVO> selectSkuOutstockSummary();
}
