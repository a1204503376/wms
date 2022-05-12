package org.nodes.wms.dao.basics.billType;

import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;

import java.util.List;

/**
 * 单据类型 DAO接口
 */
public interface BillTypeDao {

	/**
	 * 根据单据类型编码或者单据类型名称查询前10个单据类型信息
	 *
	 * @param ioType       类型模式 i:入库 o:出库 ‘’全部
	 * @param billTypeCode 单据类型编码
	 * @param billTypeName 单据类型名称
	 * @return List<BillTypeSelectResponse>
	 */
	List<BillTypeSelectResponse> listTop10ByBillTypeCdBillTypeName(String ioType,String billTypeCode, String billTypeName);
}
