package org.nodes.wms.dao.count.dto.input;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 生成盘点单记录报告请求对象
 *
 * @author nodes
 */
@Data
public class GenerateCountReport implements Serializable {
	private static final long serialVersionUID = -6618006876486530077L;
	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 库存ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 余额
	 */
	private BigDecimal stockBalance;

	/**
	 * 是否无误
	 */
	private Boolean isValid;
}
