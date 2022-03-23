
package org.nodes.wms.core.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.system.entity.BarcodeRule;

/**
 * 条码规则视图实体类
 *
 * @author liangmei
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BarcodeRuleVO对象", description = "BarcodeRuleVO对象")
public class BarcodeRuleVO extends BarcodeRule {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;
	/**
	 * 条码类型描述
	 */
	@ApiModelProperty(value = "条码类型描述")
	private String barcodeTypeDesc;
}
