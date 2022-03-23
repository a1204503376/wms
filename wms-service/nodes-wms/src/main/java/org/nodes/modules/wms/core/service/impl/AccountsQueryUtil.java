package org.nodes.modules.wms.core.service.impl;

import org.nodes.modules.wms.core.service.IAccountService;
import org.nodes.wms.core.stock.core.vo.AccountsVo;
import org.springblade.core.tool.utils.Func;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 台账查询工具类
 */

public class AccountsQueryUtil {
	public static IAccountService iAccountService;
	public static List<AccountsVo> getAccounts(List<Long> skuIds, String startTime, String endTime
		, String group, AccountsVo accountsVo,String stockIds){
		List<AccountsVo> accountsVos = new ArrayList<>();
		iAccountService = new AsnAccountServiceImpl();
		iAccountService.getChildFlag().clear();
		List<Long> skuIdsByStockIds = iAccountService.getSkuIdsByStockIds(stockIds);
		if(Func.isNotEmpty(skuIdsByStockIds)){
			skuIds = skuIdsByStockIds;
		}
		accountsVos.addAll(iAccountService.getAccountsByOptType(skuIds,startTime,endTime,group,accountsVo));
		iAccountService = new PickAccountServiceImpl();
		List<AccountsVo> accountsByPick = iAccountService.getAccountsByOptType(skuIds, startTime, endTime, group, accountsVo);
		accountsByPick.forEach(accountsVo1 -> {
			accountsVo1.setOptQty(accountsVo1.getOptQty().negate());
		});
		accountsVos.addAll(accountsByPick);
//		iAccountService = new MoveAccountServiceImpl();
//		accountsVos.addAll(iAccountService.getAccountsByOptType(stockIds));
//		iAccountService = new ShelveAccountServiceImpl();
//		accountsVos.addAll(iAccountService.getAccountsByOptType(stockIds));
//		iAccountService = new InventoryGainAccountServiceImpl();
//		accountsVos.addAll(iAccountService.getAccountsByOptType(skuIds,startTime,endTime,group,accountsVo));
//		iAccountService = new InventoryLossAccountServiceImpl();
//		accountsVos.addAll(iAccountService.getAccountsByOptType(skuIds,startTime,endTime));
//		AbsAccountsServiceImpl absAccountsService = (AbsAccountsServiceImpl) iAccountService;
//		absAccountsService.clearCache();
		if(Func.isNotEmpty(iAccountService.getGroup())){
			Map<String, List<AccountsVo>> collect = null;
			if("billNo".equals(group)){
				collect = accountsVos.stream().collect(Collectors.groupingBy(AccountsVo::getBillNo));
			}else if("optTime".equals(group)){
				collect = accountsVos.stream().collect(Collectors.groupingBy(AccountsVo::getOptTime));
			}else if("optUser".equals(group)){
				collect = accountsVos.stream().collect(Collectors.groupingBy(AccountsVo::getOptUser));
			}else if("optType".equals(group)){
				collect = accountsVos.stream().collect(Collectors.groupingBy(AccountsVo::getOptType));
			}
			List<AccountsVo> newAccountsVos = new ArrayList<>();

			collect.forEach((key,value)->{
				BigDecimal reduce = value.stream().map(AccountsVo::getOptQty).reduce(BigDecimal.ZERO, BigDecimal::add);
				AccountsVo accountsVo1 = new AccountsVo();
				accountsVo1.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
				if("billNo".equals(group)){
					accountsVo1.setBillNo(key);
					accountsVo1.setOptQty(reduce);
					accountsVo1.setOptType(value.get(0).getOptType());
//					accountsVo1.setOptUser(value.get(0).getOptUser());
//					accountsVo1.setOptTime(value.get(0).getOptTime());
				}else if("optTime".equals(group)){
					accountsVo1.setOptTime(key);
//					accountsVo1.setBillNo(value.get(0).getBillNo());
					accountsVo1.setOptQty(reduce);
//					accountsVo1.setOptType(value.get(0).getOptType());
//					accountsVo1.setOptUser(value.get(0).getOptUser());
				}else if("optUser".equals(group)){
					accountsVo1.setOptUser(key);
//					accountsVo1.setOptTime(value.get(0).getOptTime());
//					accountsVo1.setBillNo(value.get(0).getBillNo());
					accountsVo1.setOptQty(reduce);
//					accountsVo1.setOptType(value.get(0).getOptType());
				}else if("optType".equals(group)){
					accountsVo1.setOptType(key);
//					accountsVo1.setOptUser(value.get(0).getOptUser());
//					accountsVo1.setOptTime(value.get(0).getOptTime());
//					accountsVo1.setBillNo(value.get(0).getBillNo());
					accountsVo1.setOptQty(reduce);
				}
				Integer isChilds = iAccountService.getChildFlag().get(key);
				if(isChilds>1){
					accountsVo1.setHasChildren(true);
				}else{
					accountsVo1.setHasChildren(false);
					accountsVo1.setOptType(value.get(0).getOptType());
					accountsVo1.setOptUser(value.get(0).getOptUser());
					accountsVo1.setOptTime(value.get(0).getOptTime());
					accountsVo1.setBillNo(value.get(0).getBillNo());
					accountsVo1.setOptQty(reduce);
				}
				newAccountsVos.add(accountsVo1);
			});
			List<AccountsVo> collect1 = sortAccountsByOptTime(newAccountsVos);

			return collect1;
		}
		List<AccountsVo> collect = sortAccountsByOptTime(accountsVos);
		return collect;
	}

	/**
	 * 根据操作日期排序
	 * @param accountsVos
	 * @return
	 */
	@NotNull
	private static List<AccountsVo> sortAccountsByOptTime(List<AccountsVo> accountsVos) {
		return accountsVos.stream()
			.sorted(Comparator.comparing((Function<AccountsVo, String>) accountsVo1 -> accountsVo1.getOptTime()
				, Comparator.nullsFirst(String::compareTo)).reversed())
			.collect(Collectors.toList());
	}
}
