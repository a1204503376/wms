
package org.nodes.wms.core.count.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.stock.core.vo.CountSkuQtyVO;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.mapper.CountDetailMapper1;
import org.nodes.wms.core.count.service.ICountDetailService;
import org.nodes.wms.core.count.vo.CountDetailVO;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 盘点单明细表 服务实现类
 *
 * @author NodeX
 * @since 2020-01-02
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class CountDetailServiceImpl<M extends CountDetailMapper1, T extends CountDetail>
	extends BaseServiceImpl<CountDetailMapper1, CountDetail>
	implements ICountDetailService {

	@Override
	public IPage<CountDetailVO> selectCountDetailPage(IPage<CountDetailVO> page, CountDetailVO countDetail) {
		return page.setRecords(baseMapper.selectCountDetailPage(page, countDetail));
	}

	@Override
	public List<CountDetail> selectCountDetailList(CountDetail countDetail) {
		return baseMapper.selectList(Condition.getQueryWrapper(countDetail));
	}

	@Override
	public boolean removeByIdsNew(String ids) {

		return baseMapper.removeByIdsNew(Func.toLong(ids));
	}

	@Override
	public List<CountDetail> list(Long countBillId) {
		CountDetail countDetail = new CountDetail();
		countDetail.setCountBillId(countBillId);
		return super.list(Condition.getQueryWrapper(countDetail));
	}

	@Override
	public List<CountDetail> selectOccupyCountDetailByLocCodes(List<Long> locIds) {
		return baseMapper.selectOccupyCountDetailByLocCodes(locIds);
	}
	@Override
	public List<CountDetail> selectOccupyCountDetailBySkuCodes(List<Long> skuIds) {
		return baseMapper.selectOccupyCountDetailBySkuCodes(skuIds);
	}
	@Override
	public List<CountSkuQtyVO> querySkuQty(Map<String, String> map, List<Long> skuIds) {
		return baseMapper.querySkuQty(map,skuIds);
	}
}
