package org.nodes.wms.dao.outstock.so.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.SoDetailDao;
import org.nodes.wms.dao.outstock.so.dto.input.SoDetailAndStockRequest;
import org.nodes.wms.dao.outstock.so.dto.output.*;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.mapper.SoDetailMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 发货单Dao接口实现类
 **/
@Repository
public class SoDetailDaoImpl extends BaseServiceImpl<SoDetailMapper, SoDetail> implements SoDetailDao {
	@Override
	public boolean saveOrUpdateBatch(List<SoDetail> soDetailList) {
		return super.saveOrUpdateBatch(soDetailList);
	}

	@Override
	public List<SoDetailEditResponse> getSoDetailEditBySoBillId(Long soBillId) {
		return super.baseMapper.selectSoDetailEditBySoBillId(soBillId);
	}

	@Override
	public boolean removeByIdList(List<Long> detailIdList) {
		return super.removeByIds(detailIdList);
	}

	@Override
	public Page<SoDetailForDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, Long soBillId) {
		return super.baseMapper.pageForSoDetailBySoBillId(page, soBillId);
	}

	@Override
	public Page<NotSoPickPageResponse> pageNotSoPick(IPage<Object> page, NotSoPickPageQuery notSoPickPageQuery) {
		return super.baseMapper.pageNotSoPick(page, notSoPickPageQuery);
	}

	@Override
	public List<NotSoPickExcelResponse> notSoPickListByQuery(NotSoPickPageQuery notSoPickPageQuery) {
		return super.baseMapper.notSoPickListByQuery(notSoPickPageQuery);
	}

	@Override
	public List<LineNoAndSkuSelectResponse> getLineNoAndSkuCodeById(Long soBillId) {
		return super.baseMapper.selectLineNoAndSkuCodeById(soBillId);
	}

	@Override
	public List<SoDetail> getBySoBillId(Long soBillId) {
		LambdaQueryWrapper<SoDetail> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(SoDetail::getSoBillId, soBillId);
		return list(queryWrapper);
	}

	@Override
	public PickByPcSoDetailResponse getPickByPcDetail(SoDetailAndStockRequest soDetailAndStockRequest) {
		return super.baseMapper.getPickByPcDetail(soDetailAndStockRequest);
	}

	@Override
	public IPage<SoDetail> getSoDetailPage(Long soBillId, IPage<SoDetail> page) {
		AssertUtil.notEmpty(String.valueOf(soBillId), "发货单ID不能为空");
		LambdaQueryWrapper<SoDetail> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(SoDetail::getSoBillId, soBillId);
		return super.baseMapper.selectPage(page, queryWrapper);
	}

	@Override
	public List<SoDetailForDistResponse> getSoDetailForDistribute(Long soBillId) {
		return super.baseMapper.getSoDetailForDistribute(soBillId);
	}

	@Override
	public SoDetail getSoDetailById(Long soDetailId) {
		return super.getById(soDetailId);
	}

	@Override
	public void update(SoDetail soDetail) {
		if (!super.saveOrUpdate(soDetail)) {
			throw new ServiceException("修改发货单明细失败");
		}
	}
}
