package org.nodes.wms.biz.basics.billType;

import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;

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
}
