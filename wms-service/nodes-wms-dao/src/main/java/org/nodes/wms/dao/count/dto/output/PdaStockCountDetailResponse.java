package org.nodes.wms.dao.count.dto.output;

import lombok.Data;
import org.nodes.wms.dao.count.enums.CountDetailStateEnum;

import java.util.List;

/**
 * PDA显示盘点单明细对象
 */
@Data
public class PdaStockCountDetailResponse {
	/**
	 * 盘点单明细ID
	 */
	private Long countDetailId;
	/**
	 * 盘点单明细状态
	 */
	private CountDetailStateEnum countDetailState;
	/**
	 * 库位ID
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 箱号和箱内物品总数量对象集合
	 */
	private List<PdaBoxQtyResponse> pdaBoxQtyResponseList;
	/**
	 * 盘点人
	 */
	private String userName;
	/**
	 * 是否是人工拣货区/拣货区
	 */
	private Boolean isPickLocation;
}
