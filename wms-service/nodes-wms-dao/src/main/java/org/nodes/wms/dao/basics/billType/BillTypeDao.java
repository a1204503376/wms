package org.nodes.wms.dao.basics.billType;

import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;

import java.util.List;

/**
 * 单据类型 DAO接口
 */
public interface BillTypeDao {

	/**
	 * 根据ioType查询单据类型信息
	 *
	 * @param ioType 类型模式 i:入库 o:出库 ‘’全部
	 * @return List<BillTypeSelectResponse>
	 */
	List<BillTypeSelectResponse> listByIoType(String ioType);
}
