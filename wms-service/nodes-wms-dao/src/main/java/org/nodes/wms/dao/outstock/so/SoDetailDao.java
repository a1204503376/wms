package org.nodes.wms.dao.outstock.so;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailForDetailResponse;
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
	 * @param page: 分页参数
	 * @param soBillId: 发货单id
	 * @return Page<SoDetailForDetailResponse> 发货单明细分页信息
	 */
	Page<SoDetailForDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, Long soBillId);

	/**
	 * 分页查询未发货记录
	 *
	 * @param page: 分页参数
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
}
