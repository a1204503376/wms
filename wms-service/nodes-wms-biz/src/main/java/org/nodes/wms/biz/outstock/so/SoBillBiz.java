package org.nodes.wms.biz.outstock.so;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.common.log.dto.output.LogDetailPageResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.findSoHeaderByNoRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillAddOrEditRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoDetailAndStockRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.*;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.stock.dto.output.SerialSelectResponse;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 发货单业务接口
 **/
public interface SoBillBiz {

	/**
	 * 分页查询
	 *
	 * @param query:             分页参数
	 * @param soHeaderPageQuery: 分页查询条件请求对象
	 * @return Page<SoHeaderPageResponse> 发货单头表分页响应对象
	 */
	Page<SoHeaderPageResponse> getPage(Query query, SoHeaderPageQuery soHeaderPageQuery);

	/**
	 * 新增发货单
	 *
	 * @param soBillAddOrEditRequest: 新增或编辑发货单请求对象
	 * @return SoHeader 发货单对象
	 */
	SoHeader add(SoBillAddOrEditRequest soBillAddOrEditRequest);

	/**
	 * 批量删除
	 *
	 * @param soBillIdList: 发货单id
	 * @return true: 删除成功，false: 删除失败
	 */
	boolean remove(List<Long> soBillIdList);

	/**
	 * 编辑发货单
	 *
	 * @param soBillAddOrEditRequest: 新增或编辑发货单请求对象
	 * @return SoHeader 发货单对象
	 */
	SoHeader edit(SoBillAddOrEditRequest soBillAddOrEditRequest);

	/**
	 * 获取编辑发货单信息
	 *
	 * @param soBillId: 发货单id
	 * @return SoBillEditResponse 发货单编辑响应对象
	 */
	SoBillEditResponse findSoBillByEdit(Long soBillId);

	/**
	 * 查看明细：根据发货单id获取头表信息
	 *
	 * @param soBillId: 发货单id
	 * @return SoHeaderForDetailResponse 查看明细头表信息响应对象
	 */
	SoHeaderForDetailResponse findSoHeaderForDetailBySoBillId(Long soBillId);

	/**
	 * 关闭发货单
	 *
	 * @param soBillId: 发货单id
	 */
	void closeById(Long soBillId);

	/**
	 * 导出
	 *
	 * @param soHeaderPageQuery: 导出时条件参数
	 * @param response:          响应对象
	 */
	void export(SoHeaderPageQuery soHeaderPageQuery, HttpServletResponse response);

	/**
	 * 根据发货单id分页查询发货单日志
	 *
	 * @param soBillId: 发货单id
	 * @param page:     分页参数
	 * @return PageLogForSoDetailResponse> 发货单日志分页响应对象
	 */
	Page<LogDetailPageResponse> pageLogById(IPage<?> page, Long soBillId);

	/**
	 * pc发货返回前端头表信息
	 *
	 * @param soBillIdRequest 发货的头表id
	 * @return 头表信息
	 */
	PickByPcSoHeaderResponse getSoHeaderByPickPc(SoBillIdRequest soBillIdRequest);

	/**
	 * 根据发货单id查找分页时的发货单信息
	 *
	 * @param soBillId : 发货单id
	 * @return SoBillDistributedResponse 发货单信息
	 */
	SoBillDistributedResponse findSoBillForDistributeBySoBillId(Long soBillId);

	/**
	 * 根据发货单编码/上游编码/任务号
	 *
	 * @param page    分页对象
	 * @param request 请求条件 包含no
	 * @return 拣货分页查询响应对象
	 */
	IPage<FindAllPickingResponse> getAllPickingByNo(IPage<?> page, findSoHeaderByNoRequest request);

	/**
	 * pc拣货获取前端出库明细和库存信息
	 *
	 * @param soDetailAndStockRequest 包含行号和发货单id
	 * @return 出库明细和库存信息
	 */
	SoDetailAndStockResponse getSoDetailAndStock(SoDetailAndStockRequest soDetailAndStockRequest);

	/**
	 * 根据id获取发货单实体
	 *
	 * @param soBillId 发货单id
	 * @return 发货单实体
	 */
	SoHeader getSoHeaderById(Long soBillId);

	/**
	 * 修改发货单状态
	 *
	 * @param soHeader 发货单头表
	 */
	void updateSoBillState(SoHeader soHeader);

	/**
	 * 查看明细：根据发货单id分页查询发货单明细信息
	 *
	 * @param soBillIdRequest: 发货单id请求对象
	 * @param query:           分页参数
	 * @return Page<SoDetailForDetailResponse> 发货单明细分页信息
	 */
	Page<SoDetailForDetailResponse> pageSoDetailForDetailBySoBillId(Query query, SoBillIdRequest soBillIdRequest);

	/**
	 * 分页查询未发货记录
	 *
	 * @param query:              分页参数
	 * @param notSoPickPageQuery: 分页查询条件
	 * @return Page<NotSoPickPageResponse> 未发货记录分页对象
	 */
	Page<NotSoPickPageResponse> pageNotSoPick(Query query, NotSoPickPageQuery notSoPickPageQuery);

	/**
	 * 导出未发货记录
	 *
	 * @param notSoPickPageQuery: 导出时条件
	 * @param response:           响应对象
	 */
	void exportNotSoPick(NotSoPickPageQuery notSoPickPageQuery, HttpServletResponse response);

	/**
	 * 获取出库明细行号和物料编码下拉列表集合
	 *
	 * @param soBillId 发货单id
	 * @return 行号下拉列表数据
	 */
	List<LineNoAndSkuSelectResponse> getLineNoAndSkuSelectList(Long soBillId);

	/**
	 * 获取序列号下拉列表
	 *
	 * @param stockId 仓库id
	 * @return 序列号集合
	 */
	List<SerialSelectResponse> getSerialSelectResponseList(Long stockId);

	/**
	 * 根据发货单ID查询出库单明细
	 *
	 * @param soBillId 发货单ID
	 * @param query    分页参数
	 * @return 分页
	 */
	IPage<SoDetail> getPickingBySoBillId(Long soBillId, Query query);

	/**
	 * 根据Id获取发货单明细实体
	 *
	 * @param soDetailId 发货单明细id
	 * @return 发货单实体
	 */
	SoDetail getSoDetailById(Long soDetailId);

	/**
	 * 修改发货单明细
	 *
	 * @param soDetail 发货单实体
	 */
	void update(SoDetail soDetail);

	/**
	 * 修改发货单明细状态和剩余数量
	 *
	 * @param soDetail 发货单明细实体
	 * @param pickQty  拣货传入实收数量 撤销拣货传入撤销数量
	 */
	void updateSoDetailStatus(SoDetail soDetail, BigDecimal pickQty);

	/**
	 * 根据出库单id获取可以出库的明细
	 *
	 * @param soBillId 出库单头表id
	 * @return 可以出库的订单明细
	 */
	List<SoDetail> getEnableSoDetailBySoHeaderId(Long soBillId);

	/**
	 * 判断单据状态是否已经完成（含取消）
	 *
	 * @param soHeader 发货单
	 * @return true：表示发货单已经完成
	 */
	boolean isFinish(SoHeader soHeader);

	/**
	 * 更新发货单状态
	 *
	 * @param soBillId        发货单id
	 * @param soBillStateEnum 状态
	 */
	void updateState(Long soBillId, SoBillStateEnum soBillStateEnum);


	/**
	 * 根据发货单ID和物品编码查询发货单详情
	 *
	 * @param soBillId 发货单id
	 * @param skuCode  物品编码
	 * @return 发货单详情
	 */
	SoDetail findSoDetailByHeaderIdAndSkuCode(Long soBillId, String skuCode);
}
