package org.nodes.modules.wms.core.service.impl;

import org.nodes.modules.wms.core.service.IAccountService;
import org.nodes.wms.core.stock.core.vo.AccountsVo;

import java.util.List;

/**
 * 收货查询
 */
public class AsnAccountServiceImpl extends AbsAccountsServiceImpl{
	@Override
	protected String getOptType() {
		return IAccountService.optTypeMap.get("1");
	}

	@Override
	public List<AccountsVo> getAccountsByOptType(List<Long> skuIds, String startTime, String endTime, String group, AccountsVo accountsVo) {
		super.getAccountsByOptType(skuIds,startTime,endTime,group,accountsVo);
		List<AccountsVo> accountsWithAsn = stockService.getAccountsWithAsn(skuIds,startTime,endTime);
		return optData(accountsWithAsn);
	}
}
