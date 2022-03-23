package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.strategy.entity.WellenDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 波次策略明细视图实体类
 *
 * @author NodeX
 * @since 2021-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WellenDetailVO对象", description = "波次策略明细")
public class WellenDetailVO extends WellenDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单字段 字典code=st_wellen_field
	 */
	@ApiModelProperty(value = "订单字段 字典code=st_wellen_field")
	private String billFieldName;
	/**
	 * 操作符 字典code=st_wellen_operation
	 */
	@ApiModelProperty(value = "操作符 字典code=st_wellen_operation")
	private String operationName;

	/**
	 * 条件 字典code=st_wellen_criteria
	 */
	@ApiModelProperty(value = "条件 字典code=st_wellen_criteria")
	private String criteriaName;

}
