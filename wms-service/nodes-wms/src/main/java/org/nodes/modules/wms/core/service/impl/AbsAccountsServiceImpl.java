package org.nodes.modules.wms.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.modules.wms.core.service.IAccountService;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.AccountsVo;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 台账查询抽象基类
 */
public abstract class AbsAccountsServiceImpl implements IAccountService {
	protected IStockService stockService;
	public AbsAccountsServiceImpl(){
		stockService = SpringUtil.getBean(IStockService.class);
	}
	private String group;
	private AccountsVo accountsVoParam;
	private String groupTemp;
	private static Map<String,Integer> childFlag = new HashMap<>();
	@Override
	public List<AccountsVo> getAccountsByOptType(List<Long> skuIds, String startTime, String endTime, String group,AccountsVo accountsVo) {
		this.groupTemp = group;
		this.group = group;
		this.accountsVoParam = accountsVo;
		return null;
	}

	@Override
	public List<Long> getSkuIdsByStockIds(String stockIds){
		if(Func.isEmpty(stockIds)){
			return null;
		}
		List<Stock> stocks = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, Func.toLongList(stockIds)));
		if(Func.isNotEmpty(stocks)){
			List<Long> skuIds = NodesUtil.toList(stocks, Stock::getSkuId);
			return skuIds;
		}
		return null;
	}
	/**
	 * 组装共通字段信息和操作数等信息
	 * @param fromAccounts
	 * @return
	 */
	public List<AccountsVo> optData(List<AccountsVo> fromAccounts){
		if(Func.isNotEmpty(accountsVoParam)){
			String temp = null;
			if("billNo".equals(groupTemp)){
				temp = accountsVoParam.getBillNo();
			}else if("optTime".equals(groupTemp)){
				temp = accountsVoParam.getOptTime();
			}else if("optUser".equals(groupTemp)){
				temp = accountsVoParam.getOptUser();
			}else if("optType".equals(groupTemp)){
				temp = accountsVoParam.getOptType();
			}
			if(Func.isNotEmpty(temp)){
				this.group = null;
			}
		}

		List<AccountsVo> accountsVos = fromAccounts.stream()
			.map(accountsVo -> {
				accountsVo.setOptType(getOptType());
				if(Func.isNotEmpty(group)){
					String key = null;
					if("billNo".equals(group)){
						key = accountsVo.getBillNo();
					}else if("optTime".equals(group)){
						key = accountsVo.getOptTime();
					}else if("optUser".equals(group)){
						key = accountsVo.getOptUser();
					}else if("optType".equals(group)){
						key = accountsVo.getOptType();
					}
					if (Func.isNotEmpty(childFlag)&&Func.isNotEmpty(childFlag.get(key))) {
						childFlag.put(key, childFlag.get(key) + 1);
					} else {
						childFlag.put(key, 1);
					}
				}

				return accountsVo;
			}).collect(Collectors.toList());

		if(Func.isNotEmpty(accountsVoParam)){
			String temp = null;
			if("billNo".equals(groupTemp)){
				temp = accountsVoParam.getBillNo();
			}else if("optTime".equals(groupTemp)){
				temp = accountsVoParam.getOptTime();
			}else if("optUser".equals(groupTemp)){
				temp = accountsVoParam.getOptUser();
			}else if("optType".equals(groupTemp)){
				temp = accountsVoParam.getOptType();
			}
			if(Func.isNotEmpty(temp)){
				List<AccountsVo> collect = accountsVos.stream().filter(accountsVo -> {
					if ("billNo".equals(groupTemp)) {
						return accountsVoParam.getBillNo().equals(accountsVo.getBillNo());
					} else if ("optTime".equals(groupTemp)) {
						return accountsVoParam.getOptTime().equals(accountsVo.getOptTime());
					} else if ("optUser".equals(groupTemp)) {
						return accountsVoParam.getOptUser().equals(accountsVo.getOptUser());
					} else if ("optType".equals(groupTemp)) {
						return accountsVoParam.getOptType().equals(accountsVo.getOptType());
					}
					return false;
				}).collect(Collectors.toList());
				return collect;
			}
		}
		return accountsVos;
	}

	/**
	 * 获取操作类型
	 * @return
	 */
	protected abstract String getOptType();

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public Map<String, Integer> getChildFlag() {
		return childFlag;
	}
}
