package org.nodes.wms.core.instock.asn.service;

import org.nodes.wms.core.instock.asn.dto.AsnByBoxSubmitDTO;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderBoxQueryDTO;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.vo.*;
import org.springblade.core.mp.base.BaseService;

import java.util.Map;

/**
 * 按箱收货 service接口
 */
public interface IAsnByBoxService extends BaseService<AsnHeader> {

	AsnHeaderVO getAsnHeader(AsnHeader asnHeader);

	AsnHeaderBoxQueryVO getAsnHeaderDetail(AsnHeaderBoxQueryDTO asnHeaderBoxQueryDTO);

	Map<String, Object> submitAsnHeader(AsnHeaderBoxSubmitVO dto);

	AsnDetailMinVO getFinishAsnDetail(Long asnBillId);

	String createBoxCode();

	/**
	 * 提交
	 *
	 * @param asnByBoxSubmitDTO 参见 AsnByBoxSubmitDTO
	 * @return 参见 AsnByBoxSubmitVO
	 */
	AsnByBoxSubmitVO submit(AsnByBoxSubmitDTO asnByBoxSubmitDTO);

//	AsnHeaderBoxQueryVO getLabelInfoWithStock(AsnHeaderBoxQueryDTO asnHeaderBoxQueryDTO);
}
