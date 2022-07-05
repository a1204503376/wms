package org.nodes.wms.dao.putway.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 呼叫Agv返回前端对象
 */
@Data
public class CallAgvResponse implements Serializable {
	private static final long serialVersionUID = -7188705174234888929L;
	/**
	 * 托盘号
	 */
	private String lpnCode;
	/**
	 * 数量
	 */
	private BigDecimal qty;
	/**
	 * 箱型
	 */
	private String lpnType;
	/**
	 * 箱码集合
	 */
	List<String> boxCodeList;

}
