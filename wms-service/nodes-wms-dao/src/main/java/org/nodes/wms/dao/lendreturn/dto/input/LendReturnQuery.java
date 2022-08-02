package org.nodes.wms.dao.lendreturn.dto.input;

import lombok.Data;
import org.nodes.wms.dao.lendreturn.enums.LendReturnTypeEnum;

import java.util.Date;
import java.util.List;

/**
 * 借出归还记录查询请求对象
 */
@Data
public class LendReturnQuery {
	/**
	 * 借出归还类别
	 */
	private List<LendReturnTypeEnum> typeList;

	/**
	 * 借出归还人姓名
	 */
	private String lendReturnName;

	/**
	 * 物品id集合
	 */
	private List<Long> skuIdList;

	/**
	 * 单据编码
	 */
	private String billNo;

	/**
	 * 创建时间 开始
	 */
	private Date createTimeBegin;

	/**
	 * 创建时间 结束
	 */
	private Date createTimeEnd;
}
