package org.nodes.wms.biz.basics.billType;

import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;
import org.nodes.wms.dao.basics.billType.entities.BillType;

import java.util.List;

/**
 * 单据类型 业务层接口
 */
public interface BillTypeBiz {

	/**
	 * 根据idType获取单据类型信息
	 *
	 * @param ioType： ”I“：入库，”O“：出库  “”：全部
	 * @return List<SkuSelectResponse>
	 */
	List<BillTypeSelectResponse> findBillTypeSelectResponseList(String ioType);

	/**
	 * 根据单据类型ID获取单据类型实体
	 * @param billTypeId 单据id
	 * @return BillType 单据实体
	 */
	BillType findBillTypeById(Long billTypeId);

	/**
	 * 根据单据类型编码获取单据类型实体
	 * @param billTypeCd 单据类型编码
	 * @return BillType 单据实体
	 */
	BillType findBillTypeByCode(String billTypeCd);
}
