package org.nodes.wms.dao.basics.billType;

import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;
import org.nodes.wms.dao.basics.billType.entities.BillType;

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

	/**
	 * 根据单据类型id获取单据类型实体
	 * @param billTypeId 单据类型id
	 * @return BillType 单据类型实体
	 */
	BillType getBillTypeById(Long billTypeId);

	/**
	 * 根据单据类型编码获取单据类型实体
	 * @param billTypeCd 单据类型编码
	 * @return BillType 单据类型实体
	 */
	BillType getBillTypeByCode(String billTypeCd);
}
