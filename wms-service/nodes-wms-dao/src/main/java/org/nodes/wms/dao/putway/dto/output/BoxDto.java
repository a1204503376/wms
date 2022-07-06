package org.nodes.wms.dao.putway.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 呼叫AGV返回前端箱码信息
 */
@Data
public class BoxDto implements Serializable {
	private static final long serialVersionUID = 5661683437967893433L;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 数量
	 */
	private BigDecimal qty;
}
