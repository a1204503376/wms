package org.nodes.wms.dao.count.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 盘点单编辑请求对象
 */
@Data
public class FindPdaStockCountResponseListRequest implements Serializable {
	private static final long serialVersionUID = 489756629570332225L;
	/**
	 * 盘点单编码
	 */
	private String countBillNo;
	/**
	 * 库房ID
	 */
	@NotNull
	private Long whId;
}
