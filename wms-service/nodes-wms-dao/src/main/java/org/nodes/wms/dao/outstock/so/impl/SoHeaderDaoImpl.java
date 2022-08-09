package org.nodes.wms.dao.outstock.so.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.outstock.so.SoHeaderDao;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.*;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.mapper.SoHeaderMapper;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.findSoHeaderByNoRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 发货单Dao接口实现类
 **/
@Repository
public class SoHeaderDaoImpl extends BaseServiceImpl<SoHeaderMapper, SoHeader> implements SoHeaderDao {

	@Override
	public Page<SoHeaderPageResponse> page(IPage<?> page, SoHeaderPageQuery soHeaderPageQuery) {
		return super.baseMapper.page(page, soHeaderPageQuery);
	}

	@Override
	public boolean removeByIdList(List<Long> soBillIdList) {
		return super.removeByIds(soBillIdList);
	}

	@Override
	public SoHeader getById(Long id) {
		return super.getById(id);
	}

	@Override
	public SoHeaderForDetailResponse getSoHeaderForDetailById(Long id) {
		return super.baseMapper.selectSoHeaderForDetailById(id);
	}

	@Override
	public boolean updateSoHeaderById(SoHeader soHeader) {
		return super.updateById(soHeader);
	}

	@Override
	public List<SoHeaderExcelResponse> listByQuery(SoHeaderPageQuery soHeaderPageQuery) {
		return super.baseMapper.listByQuery(soHeaderPageQuery);
	}

	@Override
	public PickByPcSoHeaderResponse getSoHeaderResponseById(Long soBillId) {
		return super.baseMapper.selectSoHeaderResponseById(soBillId);
	}

	@Override
	public IPage<FindAllPickingResponse> getAllPickingPage(IPage<?> page, findSoHeaderByNoRequest request) {
		AssertUtil.notEmpty(request.getNo(), "发货单编码/上游编码/任务号不能为空");
		AssertUtil.notEmpty(request.getWhId(), "库房编码不能为空");
		AssertUtil.notNull(request.getBillDetailState(), "单据状态不能为空");
		return super.baseMapper.getAllPickPage(page, request);
	}

	@Override
	public boolean saveOrUpdateSoHeader(SoHeader soHeader) {
		return super.saveOrUpdate(soHeader);
	}

	@Override
	public SoHeaderEditResponse getSoHeaderEditBySoBillId(Long soBillId) {
		return super.baseMapper.selectSoHeaderEditBySoBillId(soBillId);
	}

}
