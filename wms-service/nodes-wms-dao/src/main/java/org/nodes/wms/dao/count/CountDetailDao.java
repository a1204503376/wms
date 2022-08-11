package org.nodes.wms.dao.count;

import org.nodes.wms.dao.count.entity.CountDetail;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 盘点单明细 DAO接口
 */
public interface CountDetailDao extends BaseService<CountDetail> {


	/**
	 * 物理删除明细数据
	 */
	boolean deleteByIds(List<Long> ids);

	/**
	 * 根据盘点单ID查询明细集合
	 */
	List<CountDetail> selectByCountBillId(Long countBillId);

	/**
	 * 根据库位编码和箱码查询明细集合
	 *
	 * @param locCode 库位编码
	 * @param boxCode 箱码
	 * @return 盘点单明细
	 */
	CountDetail selectCountDetailByCode(String locCode, String boxCode);
}
