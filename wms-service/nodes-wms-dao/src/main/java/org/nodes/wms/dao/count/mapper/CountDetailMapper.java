package org.nodes.wms.dao.count.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.count.dto.input.PdaStockCountDetailBySkuSpecRequest;
import org.nodes.wms.dao.count.dto.output.PdaStockCountDetailBySkuSpecResponse;
import org.nodes.wms.dao.count.entity.CountDetail;

import java.util.Collection;
import java.util.List;

/**
 * 盘点单明细表 Mapper 接口
 */
public interface CountDetailMapper extends BaseMapper<CountDetail> {

	int deleteByCountDetailId(@Param("countDetailIdList") Collection<Long> countDetailIdList);

	/**
	 * 根据规格型号查询能创建盘点单的明细
	 *
	 * @param request 包含规格型号
	 * @return 能创建盘点单的明细
	 */
	List<PdaStockCountDetailBySkuSpecResponse> getStockCountDetailBySkuSpec(@Param("param") PdaStockCountDetailBySkuSpecRequest request);
}
