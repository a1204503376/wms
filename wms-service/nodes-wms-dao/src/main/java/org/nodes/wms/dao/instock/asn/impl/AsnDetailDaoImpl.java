package org.nodes.wms.dao.instock.asn.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.asn.AsnDetailDao;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.mapper.AsnDetailMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  Asn单明细 Dao 实现类
 */
@Repository
@RequiredArgsConstructor
public class AsnDetailDaoImpl
	extends BaseServiceImpl<AsnDetailMapper, AsnDetail>
	implements AsnDetailDao {

	@Override
	public Boolean deleteAsnDetailByAsnBillId(List<Long> asnBillIdList) {
		LambdaQueryWrapper<AsnDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.in(AsnDetail::getAsnBillId,asnBillIdList);
		return super.baseMapper.delete(lambdaQueryWrapper) > 0;
	}

	@Override
	public List<Long> selectAsnDetailIdListByAsnBillId(List<Long> asnBillIdList) {
		return super.baseMapper.selectAsnDetailId(asnBillIdList);
	}

	@Override
	public Boolean insertAsnDetail(List<AsnDetail> asnDetailList) {
		return super.saveBatch(asnDetailList);
	}

	@Override
	public List<AsnDetail> getAsnDetailByAsnBillId(Long asnBillId) {
		return super.baseMapper.selectAsnDetailByAsnBillId(asnBillId);
	}
}
