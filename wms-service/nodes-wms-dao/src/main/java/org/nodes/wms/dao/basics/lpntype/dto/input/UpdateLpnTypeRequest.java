package org.nodes.wms.dao.basics.lpntype.dto.input;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 容器管理修改接收类
 **/
@Data
public class UpdateLpnTypeRequest implements Serializable {
	private static final long serialVersionUID = -2814681205548453824L;

	/**
	 * 要修改的容器id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 容器类型(1:箱,2:托)
	 */
	@JsonSerialize(using = ToStringSerializer.class)
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
