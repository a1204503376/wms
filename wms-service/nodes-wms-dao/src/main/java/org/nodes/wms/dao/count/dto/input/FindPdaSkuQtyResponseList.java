package org.nodes.wms.dao.count.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 盘点单编辑请求对象
 */
@Data
public class FindPdaSkuQtyResponseList implements Serializable {
	private static final long serialVersionUID = 7282904270153514291L;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 库房ID
	 */
	@NotNull
	private Long whId;
}
