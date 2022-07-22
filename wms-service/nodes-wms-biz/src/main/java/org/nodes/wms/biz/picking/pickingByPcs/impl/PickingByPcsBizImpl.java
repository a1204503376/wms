package org.nodes.wms.biz.picking.pickingByPcs.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.outstock.so.SoHeaderBiz;
import org.nodes.wms.biz.picking.pickingByPcs.PickingByPcsBiz;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.dao.picking.dto.input.FindAllPickingRequest;
import org.nodes.wms.dao.picking.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.picking.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.picking.dto.output.PickingByBoxResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PickingByPcsBizImpl implements PickingByPcsBiz {
	private final SoHeaderBiz soHeaderBiz;

	@Override
	public IPage<FindAllPickingResponse> selectAllPickingByNo(FindAllPickingRequest request, Query query) {
		request.setBillDetailState(SoDetailStateEnum.Allocated.getIndex());
		return soHeaderBiz.getAllPickingByNo(Condition.getPage(query), request);
	}

	@Override
	public PickingByBoxResponse pickingByPcsAction(PickingByBoxRequest request) {
		return null;
	}
}
