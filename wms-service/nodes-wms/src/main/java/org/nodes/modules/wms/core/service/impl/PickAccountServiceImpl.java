package org.nodes.modules.wms.core.service.impl;

import org.nodes.modules.wms.core.service.IAccountService;
import org.nodes.wms.core.stock.core.vo.AccountsVo;

import java.util.List;

/**
 * 拣货查询
 */
public class PickAccountServiceImpl extends AbsAccountsServiceImpl{
	@Override
	protected String getOptType() {
		return IAccountService.optTypeMap.get("2");
	}
	@Override
	public List<AccountsVo> getAccountsByOptType(List<Long> skuIds, String startTime, String endTime, String group, AccountsVo accountsVo) {
		super.getAccountsByOptType(skuIds, startTime, endTime, group, accountsVo);
		List<AccountsVo> accountsWithPick = stockService.getAccountsWithPick(skuIds,startTime, endTime);
		return optData(accountsWithPick);
	}
}
