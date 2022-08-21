package org.nodes.wms.dao.outstock.soPickPlan.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分配记录查询dto
 **/
@Data
public class SoPickPlanPageQuery implements Serializable {

	private static final long serialVersionUID = -4352510917899908388L;

	/**
	 * 发货单编码
	 */
	private String soBillNo;

	/**
	 * 单据类型
	 */
	private List<String> billTypeCdList;

	/**
	 * 物品id
	 */
	private List<Long> skuIdList;

	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * LPN
	 */
	private String lpnCode;


	/**
	 * 拣货时间 - 开始
	 */
	private String createTimeBegin;

	/**
	 * 拣货时间 - 结束
	 */
	private String createTimeEnd;

	/**
	 * 拣货人
	 */
	private String createUser;
}
