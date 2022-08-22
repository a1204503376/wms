package org.nodes.wms.biz.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.*;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickingBySoBillIdResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.OutboundAccessAreaLocationQueryResponse;
import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillDistributedRequest;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.stock.dto.output.StockSoPickPlanResponse;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 出库业务
 *
 * @author nodesc
 */
public interface OutStockBiz {

	/**
	 * PC按件拣货
	 *
	 * @param request request
	 */
	void pickByPcsOnPc(PickByPcRequest request);

	/**
	 * 根据发货单id和发货单明细id查询拣货计划
	 *
	 * @param soBillId   发货单id 必填
	 * @param soDetailId 发货单明细id 非必填
	 * @return 拣货计划信息
	 */
	List<SoPickPlanForDistributionResponse> getSoPickPlanBySoBillIdAndSoDetailId(Long soBillId, Long soDetailId);

	/**
	 * 撤销拣货
	 *
	 * @param logSoPickIdList 拣货记录id
	 */
	void cancelOutStock(List<Long> logSoPickIdList);

	/**
	 * PDA按件拣货：根据单号查询出库单
	 *
	 * @param request 查询条件
	 * @param query   分页参数
	 * @return 返回多个能按件拣货的对象
	 */
	IPage<FindAllPickingResponse> findSoHeaderByNo(findSoHeaderByNoRequest request, Query query);

	/**
	 * 按件拣货动作
	 *
	 * @param request 请求对象
	 */
	void pickByPcs(PickByPcsRequest request);

	/**
	 * Pda根据发货单ID查询可以拣货的出库单明细
	 *
	 * @param request 请求对象-发货单ID
	 * @param query   分页参数
	 * @return 实现的分页
	 */
	IPage<FindPickingBySoBillIdResponse> findOpenSoDetail(FindOpenSoDetailRequest request, Query query);

	/**
	 * 按箱拣货动作
	 *
	 * @param request          请求参数
	 * @param taskProcTypeEnum 任务执行方式
	 */
	void pickByBox(PickByBoxCodeRequest request, WmsTaskProcTypeEnum taskProcTypeEnum);

	/**
	 * 查询出库接驳区的库位
	 *
	 * @param request 查询条件
	 * @return 多个库位信息
	 */
	List<OutboundAccessAreaLocationQueryResponse> findLocOfAgvPickTo(FindLocOfAgvPickToRequest request);

	/**
	 * 接驳区拣货动作
	 *
	 * @param request 请求参数
	 */
	Boolean pickOnAgvPickTo(OnAgvPickToRequest request);

	/**
	 * 接驳区移动
	 *
	 * @param request 请求参数
	 */
	void moveOnAgvPickTo(MoveOnAgvPickToRequest request);

	/**
	 * 分配：自动分配
	 *
	 * @param soBillId 发货单id
	 * @return 分配成功则返回分配成功的信息，否则返回实际运行的信息
	 */
	String autoDistribute(Long soBillId);

	/**
	 * 分配：取消分配
	 *
	 * @param soBillId 发货单id
	 * @return true：取消分配成功 false：取消分配失败
	 */
	boolean cancelDistribute(Long soBillId);

	/**
	 * 分配：确认下发
	 *
	 * @param soBillId 发货单id
	 * @return true：确认下发成功 false：确认下发失败
	 */
	boolean issued(Long soBillId);

	/**
	 * 分配：分配调整-根据物品id、生产批次、专用客户获取可分配的物品库存信息
	 *
	 * @param skuId   物品id
	 * @param skuLot1 生产批次
	 * @param skuLot4 专用客户
	 * @return  可分配物品库存信息
	 */
	List<StockSoPickPlanResponse> getStockByDistributeAdjust(Long skuId, String skuLot1, String skuLot4);

	/**
	 * 分配：分配手动调整-保存调整后的信息
	 *
	 * @param soBillDistributedRequest:
	 * @return true：保存成功 false：保存失败
	 */
	boolean manualDistribute(SoBillDistributedRequest soBillDistributedRequest);
}
