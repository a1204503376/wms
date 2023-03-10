package org.nodes.wms.dao.instock.asn.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.asn.AsnDetailDao;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailForDetailResponse;
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
	public boolean deleteAsnDetailByAsnBillId(List<Long> asnBillIdList) {
		LambdaQueryWrapper<AsnDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.in(AsnDetail::getAsnBillId,asnBillIdList);
		return super.baseMapper.delete(lambdaQueryWrapper) > 0;
	}

	@Override
	public boolean saveOrUpdateAsnDetail(List<AsnDetail> asnDetailList) {
		return super.saveOrUpdateBatch(asnDetailList);
	}

	@Override
	public List<AsnDetail> getAsnDetailByAsnBillId(Long asnBillId) {
		return super.baseMapper.selectAsnDetailByAsnBillId(asnBillId);
	}

	@Override
	public Page<AsnDetailForDetailResponse> getAsnDetailForDetailByAsnBillId(IPage<?> page, Long asnBillId) {
		return super.baseMapper.selectAsnDetailForDetailByAsnBillId(page, asnBillId);
	}

	@Override
    public void deleteByIds(List<Long> removeIdList) {
        super.removeByIds(removeIdList);
    }
}
