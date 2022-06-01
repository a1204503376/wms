package org.nodes.wms.dao.basics.BarcodeRule.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;

/**
 * 条码规则实体类
 *
 * @author liangmei
 * @since 2019-12-16
 */
@Data
public class SysBarcodeRuleResponse  implements Serializable {


	private static final long serialVersionUID = 5864743288813403201L;
	/**
	 * 条码规则定义ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sbrId;
	/**
	 * 规则
	 */
	private String barcodeRule;
	/**
	 * 条码类型
	 */
	private Integer barcodeType;

}
