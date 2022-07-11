package org.nodes.wms.dao.outstock.so;

import org.nodes.wms.dao.outstock.so.dto.output.SoDetailEditResponse;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;

import java.util.List;

/**
 * 出库单明细Dao接口
 **/
public interface SoDetailDao {

	/**
	 * 批量新增或修改出库单明细信息
	 *
	 * @param soDetailList: 出库单明细
	 * @return true: 新增或修改成功， false: 新增或修改失败
	 */
	boolean saveOrUpdateBatch(List<SoDetail> soDetailList);

	/**
	 * 获取编辑时出库单明细信息
	 *
	 * @param soBillId: 出库单id
	 * @return List<SoDetailEditResponse> 出库单明细信息
	 */
	List<SoDetailEditResponse> getSoDetailEditBySoBillId(Long soBillId);

	/**
	 * 批量删除出库单明细
	 *
	 * @param detailIdList:
	 * @return true: 删除成功，false: 删除失败
	 */
	boolean removeByIdList(List<Long> detailIdList);
}
