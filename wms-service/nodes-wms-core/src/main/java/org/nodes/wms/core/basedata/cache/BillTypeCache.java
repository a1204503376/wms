package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.core.basedata.service.IBillTypeService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 单据类型
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class BillTypeCache {

	public static final String BILL_TYPE_CACHE = "wms:bill_type";

	static final String BILL_TYPE_ID = "bill_type:id";

	static final String BILL_TYPE_CODE = "bill_type:code:";

	static IBillTypeService billTypeService;

	static {
		billTypeService = SpringUtil.getBean(IBillTypeService.class);
	}

	public static BillType getById(Long id) {
		return CacheUtil.get(BILL_TYPE_CACHE, BILL_TYPE_ID, id, () -> billTypeService.getById(id));
	}

	public static BillType getByCode(String code) {
		return CacheUtil.get(BILL_TYPE_CACHE, BILL_TYPE_CODE, code, () -> {
			return billTypeService.list(Condition.getQueryWrapper(new BillType()).lambda()
				.eq(BillType::getBillTypeCd, code)).stream().findFirst().orElse(null);
		});
	}
}
