package org.nodes.wms.biz.picking.pickingByPcs;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.picking.dto.input.FindAllPickingRequest;
import org.nodes.wms.dao.picking.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.picking.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.picking.dto.output.PickingByBoxResponse;
import org.springblade.core.mp.support.Query;

public interface PickingByPcsBiz {
	/**
	 * 拣货分页查询
	 *
	 * @param request 查询条件
	 * @param query   分页参数
	 * @return 返回多个能按件拣货的对象
	 */
	IPage<FindAllPickingResponse> selectAllPickingByNo(FindAllPickingRequest request, Query query);

	/**
	 * 按件收货动作
	 *
	 * @param request 请求对象
	 * @return 是否拣货成功
	 */
	PickingByBoxResponse pickingByPcsAction(PickingByBoxRequest request);
}
