package org.nodes.wms.dao.count.impl;

import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.count.CountDetailDao;
import org.nodes.wms.dao.count.dto.input.PdaStockCountDetailBySkuSpecRequest;
import org.nodes.wms.dao.count.dto.output.PdaStockCountDetailBySkuSpecResponse;
import org.nodes.wms.dao.count.entity.CountDetail;
import org.nodes.wms.dao.count.mapper.CountDetailMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 盘点单明细
 */
@Repository
public class CountDetailDaoImpl
	extends BaseServiceImpl<CountDetailMapper, CountDetail>
	implements CountDetailDao {

	@Override
	public boolean deleteByIds(List<Long> ids) {
		AssertUtil.notEmpty(ids, "参数为空");
		return baseMapper.deleteByCountDetailId(ids) > 0;
	}

	@Override
	public List<CountDetail> selectByCountBillId(Long countBillId) {
		AssertUtil.notNull(countBillId, "盘点单ID为空");
		return super.lambdaQuery()
			.eq(CountDetail::getCountBillId, countBillId)
			.select(
				CountDetail::getCountDetailId,
				CountDetail::getCountDetailState,
				CountDetail::getLocId,
				CountDetail::getLocCode,
				CountDetail::getBoxCode,
				CountDetail::getUserName)
			.groupBy(CountDetail::getLocCode)
			.list();
	}

	@Override
	public CountDetail selectCountDetailByCode(String locCode, String boxCode) {
		AssertUtil.notNull(locCode, "根据库位编码和箱码获取盘点单明细时，库位编码为空");
		AssertUtil.notNull(boxCode, "根据库位编码和箱码获取盘点单明细时，箱码为空");
		return super.lambdaQuery()
			.eq(CountDetail::getLocCode, locCode)
			.eq(CountDetail::getBoxCode, boxCode)
			.one();
	}

	@Override
	public List<PdaStockCountDetailBySkuSpecResponse> getStockCountDetailBySkuSpec(PdaStockCountDetailBySkuSpecRequest request) {
		AssertUtil.notNull(request.getSkuLot2(), "查询能创建盘点单的明细失败，规格型号不能为空");
		return super.baseMapper.getStockCountDetailBySkuSpec(request);
	}
}
