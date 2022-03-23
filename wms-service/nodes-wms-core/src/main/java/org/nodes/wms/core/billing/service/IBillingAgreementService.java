package org.nodes.wms.core.billing.service;

import org.nodes.wms.core.billing.dto.OwnerBillPageDTO;
import org.nodes.wms.core.billing.entity.BillingAgreement;
import org.nodes.wms.core.billing.vo.BillingAgreementVO;
import org.nodes.wms.core.billing.vo.OwnerBillVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 计费协议 服务类
 *
 * @author NodeX
 * @since 2021-06-19
 */
public interface IBillingAgreementService extends BaseService<BillingAgreement> {

	/**
	 * 获取货主账单-分页
	 *
	 * @return
	 */
	IPage<OwnerBillVO> selectPageByOwnerBill(OwnerBillPageDTO ownerBillPage, Query query);
}
