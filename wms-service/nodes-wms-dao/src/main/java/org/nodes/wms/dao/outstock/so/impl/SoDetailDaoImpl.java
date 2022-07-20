package org.nodes.wms.dao.outstock.so.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.SoDetailDao;
import org.nodes.wms.dao.outstock.so.dto.output.LineNoAndSkuSelectResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailForDetailResponse;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.mapper.SoDetailMapper;
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
}
