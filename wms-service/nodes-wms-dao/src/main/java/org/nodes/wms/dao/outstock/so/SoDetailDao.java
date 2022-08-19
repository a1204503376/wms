package org.nodes.wms.dao.outstock.so;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.dto.input.SoDetailAndStockRequest;
import org.nodes.wms.dao.outstock.so.dto.output.*;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;

import java.util.List;

/**
 * 发货单明细Dao接口
 **/
public interface SoDetailDao {

	/**
	 * 批量新增或修改发货单明细信息
	 *
	 * @param soDetailList: 发货单明细
	 * @return true: 新增或修改成功， false: 新增或修改失败
	 */
	boolean saveOrUpdateBatch(List<SoDetail> soDetailList);

	/**
	 * 获取编辑时发货单明细信息
	 *
	 * @param soBillId: 发货单id
	 * @return List<SoDetailEditResponse> 发货单明细信息
	 */
	List<SoDetailEditResponse> getSoDetailEditBySoBillId(Long soBillId);

	/**
	 * 批量删除发货单明细
	 *
	 * @param detailIdList:
	 * @return true: 删除成功，false: 删除失败
	 */
	boolean removeByIdList(List<Long> detailIdList);

	/**
	 * 查看明细：根据发货单id分页查询发货单明细信息
	 *
	 * @param page:     分页参数
	 * @param soBillId: 发货单id
	 * @return Page<SoDetailForDetailResponse> 发货单明细分页信息
	 */
	Page<SoDetailForDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, Long soBillId);

	/**
	 * 分页查询未发货记录
	 *
	 * @param page:               分页参数
	 * @param notSoPickPageQuery: 分页查询条件
	 * @return Page<NotSoPickPageResponse> 未发货记录分页对象
	 */
	Page<NotSoPickPageResponse> pageNotSoPick(IPage<Object> page, NotSoPickPageQuery notSoPickPageQuery);

	/**
	 * 根据 Query 中的条件查询未发货记录
	 *
	 * @param notSoPickPageQuery: 查询条件
	 * @return List<NotSoPickExcelResponse> 未发货记录
	 */
	List<NotSoPickExcelResponse> notSoPickListByQuery(NotSoPickPageQuery notSoPickPageQuery);

	/**
	 * 根据发货单id获取行号和物料编码
	 *
	 * @param soBillId 发货单id
	 * @return 行号和物料编码集合
	 */
	List<LineNoAndSkuSelectResponse> getLineNoAndSkuCodeById(Long soBillId);

	/**
	 * 根据发货单id获取发货单明细
	 *
	 * @param soBillId: 发货单id
	 * @return List<SoDetail> 发货单明细
	 */
	List<SoDetail> getBySoBillId(Long soBillId);

	/**
	 * pc拣货获取发货单明细
	 *
	 * @param soDetailAndStockRequest 包含行号和发货单id
	 * @return 发货单明细数据
	 */
	PickByPcSoDetailResponse getPickByPcDetail(SoDetailAndStockRequest soDetailAndStockRequest);

	/**
	 * 根据发货单ID查询出库单明细分页
	 *
	 * @param soBillId 发货单ID
	 * @param page     分页参数
	 * @return 分页
	 */
	IPage<SoDetail> getSoDetailPage(Long soBillId, IPage<SoDetail> page);

	/**
	 * 根据发货单id查找分配页面的发货明细信息
	 *
	 * @param soBillId 发货单id
	 * @return 发货单明细信息
	 */
	List<SoDetailForDistResponse> getSoDetailForDistribute(Long soBillId);

	/**
	 * 根据id获取发货单实体
	 *
	 * @param soDetailId 发货单id
	 * @return 发货单实体
	 */
	SoDetail getSoDetailById(Long soDetailId);

	/**
	 * 修改发货单明细
	 *
	 * @param soDetail 发货单明细实体
	 */
	void update(SoDetail soDetail);

	/**
	 * 根据发货单返回相对应的明细的ID
	 *
	 * @param soBillId 发货单ID
	 * @return SoDetail集合
	 */
	List<SoDetail> getSoDetailBySoHeaderId(Long soBillId);
}
