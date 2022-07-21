package org.nodes.wms.biz.outstock.so;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillAddOrEditRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoDetailAndStockRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.*;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 发货单业务接口
 **/
public interface SoHeaderBiz {

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
	 * @param query:    分页参数
	 * @return PageLogForSoDetailResponse> 发货单日志分页响应对象
	 */
	Page<LogForSoDetailResponse> pageLogById(Query query, Long soBillId);

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
	SoBillDistributedResponse findSoBillForDistBySoBillId(Long soBillId);

	/**
	 * pc拣货获取前端出库明细和库存信息
	 *
	 * @param soDetailAndStockRequest 包含行号和发货单id
	 * @return 出库明细和库存信息
	 */
	SoDetailAndStockResponse getSoDetailAndStock(SoDetailAndStockRequest soDetailAndStockRequest);
}
