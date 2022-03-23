package org.nodes.wms.core.instock.asn.service;

import org.nodes.wms.core.instock.asn.dto.AsnDetailDTO;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 收货单明细表 服务类
 *
 * @author zx
 * @since 2019-12-13
 */
public interface IAsnDetailService extends BaseService<AsnDetail> {

	boolean saveOrUpdate(AsnDetailDTO entity);
	boolean saveOrUpdateByAllot(AsnDetailDTO entity);

	/**
	 * 获取入库明细集合
	 *
	 * @param asnBillId 入库表头ID
	 * @return 入库明细集合
	 */
	List<AsnDetail> list(Long asnBillId);
}
