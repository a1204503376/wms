package org.nodes.wms.dao.basics.lpntype.dto.input;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 承运商管理接收类
 **/
@Data
public class NewLpnTypeRequest implements Serializable {
	private static final long serialVersionUID = -2814681205548453824L;
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
