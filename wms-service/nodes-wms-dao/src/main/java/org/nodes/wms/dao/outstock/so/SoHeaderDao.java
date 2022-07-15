package org.nodes.wms.dao.outstock.so;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderExcelResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderForDetailResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderPageResponse;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;

import java.util.List;

/**
 * 发货单Dao接口
 **/
public interface SoHeaderDao {

	/**
	 * 分页
	 *
	 * @param page: 分页参数
	 * @param soHeaderPageQuery: 分页查询请求对象
	 * @return Page<SoHeaderPageResponse>
	 */
	Page<SoHeaderPageResponse> page(IPage<?> page, SoHeaderPageQuery soHeaderPageQuery);

	/**
	 * 新增或修改发货单头表信息
	 *
	 * @param soHeader: 发货单头表对象
	 * @return true: 新增或修改失败 false: 新增或修改失败
	 */
    boolean saveOrUpdateSoHeader(SoHeader soHeader);

	/**
	 * 获取编辑时发货单头表信息
	 *
	 * @param soBillId: 发货单id
	 * @return SoHeaderEditResponse 发货单编辑头表响应对象
	 */
	SoHeaderEditResponse getSoHeaderEditBySoBillId(Long soBillId);

	/**
	 * 批量删除
	 *
	 * @param soBillIdList: 发货单id
	 * @return true: 删除成功，false: 删除失败
	 */
	boolean removeByIdList(List<Long> soBillIdList);

	/**
	 * 根据发货单id获取发货单头表信息
	 *
	 * @param id: 发货单id
	 * @return SoHeader 发货单实体
	 */
	SoHeader getById(Long id);

	/**
	 * 查看明细：根据发货单id获取头表信息
	 *
	 * @param id: 发货单id
	 * @return SoHeaderForDetailResponse 查看明细头表信息响应对象
	 */
	SoHeaderForDetailResponse getSoHeaderForDetailById(Long id);

	/**
	 * 根据id修改发货单头表信息
	 *
	 * @param soHeader: 发货单头表对象
	 * @return true: 修改成功，false: 修改失败
	 */
    boolean updateSoHeaderById(SoHeader soHeader);

	/**
	 * 导出Excel
	 *
	 * @param soHeaderPageQuery: 导出时条件参数
	 * @return List<SoHeaderExcelResponse> 发货单数据
	 */
	List<SoHeaderExcelResponse> listByQuery(SoHeaderPageQuery soHeaderPageQuery);
}
