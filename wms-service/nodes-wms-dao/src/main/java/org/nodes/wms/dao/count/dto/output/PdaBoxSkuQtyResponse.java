package org.nodes.wms.dao.count.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PDA显示箱内物品及其数量返回对象
 */
@Data
public class PdaBoxSkuQtyResponse implements Serializable {

	private static final long serialVersionUID = 6558578161004992218L;
	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * 库位ID
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 物品和数量对象集合
	 */
	private List<PdaSkuQtyResponse> pdaSkuQtyResponseList;
}
