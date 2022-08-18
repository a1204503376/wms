package org.nodes.wms.dao.count.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.count.CountDetailDao;
import org.nodes.wms.dao.count.dto.input.PdaStockCountDetailBySkuSpecRequest;
import org.nodes.wms.dao.count.dto.output.PdaStockCountDetailBySkuSpecResponse;
import org.nodes.wms.dao.count.entity.CountDetail;
import org.nodes.wms.dao.count.enums.CountDetailStateEnum;
import org.nodes.wms.dao.count.mapper.CountDetailMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 盘点单明细
 *
 * @author nodesc
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
			.eq(CountDetail::getCountDetailState, CountDetailStateEnum.NOT_COUNTED)
			.groupBy(CountDetail::getLocCode)
			.select(
				CountDetail::getCountDetailId,
				CountDetail::getCountDetailState,
				CountDetail::getLocId,
				CountDetail::getLocCode,
				CountDetail::getBoxCode,
				CountDetail::getUserName)
			.list();
	}

	@Override
	public void updateCountDetailStateByCountDetailId(Long countDetailId, CountDetailStateEnum countDetailStateEnum) {
		UpdateWrapper<CountDetail> updateWrapper = Wrappers.update();
		updateWrapper
			.lambda()
			.set(CountDetail::getCountDetailState, countDetailStateEnum.getCode())
			.eq(CountDetail::getCountDetailId, countDetailId);
		if (!super.update(updateWrapper)) {
			throw new ServiceException("盘点单明细修改失败,请再次重试");
		}
	}

	@Override
	public Boolean getCountDetailStateByCountBillId(Long countBillId) {
		return super.lambdaQuery()
			.eq(CountDetail::getCountBillId, countBillId)
			.eq(CountDetail::getCountDetailState, CountDetailStateEnum.NOT_COUNTED)
			.count() > 0;
	}


	@Override
	public CountDetail selectCountDetailByCode(String locCode, String boxCode, String skuCode) {
		AssertUtil.notNull(locCode, "根据库位编码和箱码获取盘点单明细时，库位编码为空");
		AssertUtil.notNull(boxCode, "根据库位编码和箱码获取盘点单明细时，箱码为空");
		return super.lambdaQuery()
			.eq(CountDetail::getLocCode, locCode)
			.eq(CountDetail::getBoxCode, boxCode)
			.eq(CountDetail::getSkuCode, skuCode)
			.last("limit 1")
			.one();
	}

	@Override
	public List<PdaStockCountDetailBySkuSpecResponse> getStockCountDetailBySkuSpec(PdaStockCountDetailBySkuSpecRequest request) {
		AssertUtil.notNull(request.getSkuLot2(), "查询能创建盘点单的明细失败，规格型号不能为空");
		return super.baseMapper.getStockCountDetailBySkuSpec(request);
	}
}
