package org.nodes.wms.dao.outstock.so;

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
	 */
	boolean saveOrUpdateBatch(List<SoDetail> soDetailList);
}
