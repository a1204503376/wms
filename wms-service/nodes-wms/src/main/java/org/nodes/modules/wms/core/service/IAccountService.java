package org.nodes.modules.wms.core.service;


import org.nodes.wms.core.stock.core.vo.AccountsVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 台账查询接口
 */
public interface IAccountService {
	/**
	 * 不同操作查询台账信息
	 */
	List<AccountsVo> getAccountsByOptType(List<Long> skuIds, String startTime, String endTime, String group, AccountsVo accountsVo);

	String getGroup();
	Map<String,Integer> getChildFlag();
	List<Long> getSkuIdsByStockIds(String stockIds);
	/**
	 * 操作类型
	 */
	Map<String,String> optTypeMap = new HashMap<String,String>() {{
		put("1", "入库");
		put("2", "出货");
//		put("3", "移入");
//		put("4", "盘盈");
//		put("5", "盘亏");
//		put("6", "上架");
//		put("7", "移出");
	}};
}
