package org.nodes.wms.biz.outstock.pickingbypcs.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.outstock.so.SoDetailBiz;
import org.nodes.wms.biz.outstock.so.SoHeaderBiz;
import org.nodes.wms.biz.outstock.pickingbypcs.PickingByPcsBiz;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.FindAllPickingRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.FindPickingBySoBillIdRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickingBySoBillIdResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.PickingByBoxResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PickingByPcsBizImpl implements PickingByPcsBiz {
	private final SoHeaderBiz soHeaderBiz;
	private final SoDetailBiz soDetailBiz;

	@Override
	public IPage<FindAllPickingResponse> selectAllPickingByNo(FindAllPickingRequest request, Query query) {
		request.setBillDetailState(SoDetailStateEnum.Allocated.getIndex());
		return soHeaderBiz.getAllPickingByNo(Condition.getPage(query), request);
	}

	@Override
	public PickingByBoxResponse pickingByPcsAction(PickingByBoxRequest request) {
		return null;
	}

	@Override
	public IPage<FindPickingBySoBillIdResponse> selectPickingBySoBillId(FindPickingBySoBillIdRequest request, Query query) {
		IPage<SoDetail> page = soDetailBiz.getPickingBySoBillId(request.getSoBillId(), query);
		AssertUtil.notNull(page, "查询结果为空");
		return page.convert(result -> {
			FindPickingBySoBillIdResponse vo = new FindPickingBySoBillIdResponse();
			BeanUtil.copyProperties(result, vo);
			return vo;
		});
	}
}
