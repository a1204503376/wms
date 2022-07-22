package org.nodes.wms.dao.basics.sku.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 物品下拉选择 返回对象
 */
@Data
public class FindSkuByCodeRequest implements Serializable {
	private static final long serialVersionUID = 21290733663648044L;
	/**
	 * 物品编码
	 */
	private String no;
}
