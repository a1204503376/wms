package org.nodes.wms.dao.basics.lpntype.dto.output;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 容器管理ById查询返回类
 **/
@Data
public class LpnTypeByIdResponse implements Serializable {
	private static final long serialVersionUID = -2814681205548453824L;

	/**
	 * 要查询的容器id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 容器类型(1:箱,2:托)
	 */
	private Integer lpnType;
	/**
	 * 容器类型编码
	 */
	private String code;
	/**
	 * 容器编码生成规则
	 */
	private String lpnNoRule;
	/**
	 * 容器重量(KG)
	 */
	private BigDecimal weight;
}
