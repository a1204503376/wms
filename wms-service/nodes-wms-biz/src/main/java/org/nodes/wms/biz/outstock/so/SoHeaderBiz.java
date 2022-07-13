package org.nodes.wms.biz.outstock.so;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillAddOrEditRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.SoBillEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderForDetailResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderPageResponse;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 出库单业务接口
 **/
public interface SoHeaderBiz {

	/**
	 * 分页查询
	 *
	 * @param query: 分页参数
	 * @param soHeaderPageQuery: 分页查询条件请求对象
	 * @return Page<SoHeaderPageResponse> 出库单头表分页响应对象
	 */
	Page<SoHeaderPageResponse> getPage(Query query, SoHeaderPageQuery soHeaderPageQuery);

	/**
	 * 新增出库单
	 *
	 * @param soBillAddOrEditRequest: 新增或编辑出库单请求对象
	 * @return SoHeader 出库单对象
	 */
    SoHeader add(SoBillAddOrEditRequest soBillAddOrEditRequest);

	/**
	 * 批量删除
	 *
	 * @param soBillIdList: 出库单id
	 * @return true: 删除成功，false: 删除失败
	 */
    boolean remove(List<Long> soBillIdList);

	/**
	 * 编辑出库单
	 *
	 * @param soBillAddOrEditRequest: 新增或编辑出库单请求对象
	 * @return SoHeader 出库单对象
	 */
    SoHeader edit(SoBillAddOrEditRequest soBillAddOrEditRequest);

	/**
	 * 获取编辑出库单信息
	 *
	 * @param soBillId: 出库单id
	 * @return SoBillEditResponse 出库单编辑响应对象
	 */
	SoBillEditResponse findSoBillByEdit(Long soBillId);

	/**
	 * 查看明细：根据出库单id获取头表信息
	 *
	 * @param soBillId: 出库单id
	 * @return SoHeaderForDetailResponse 查看明细头表信息响应对象
	 */
	SoHeaderForDetailResponse findSoHeaderForDetailBySoBillId(Long soBillId);

	/**
	 * 关闭出库单
	 *
	 * @param soBillId: 出库单id
	 */
    void closeById(Long soBillId);

	/**
	 * 导出
	 *
	 * @param soHeaderPageQuery: 导出时条件参数
	 * @param response: 响应对象
	 */
	void export(SoHeaderPageQuery soHeaderPageQuery, HttpServletResponse response);
}