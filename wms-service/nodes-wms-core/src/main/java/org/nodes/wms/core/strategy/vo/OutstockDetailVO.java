
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.OutstockDetail;

import java.util.List;

/**
 * 视图实体类
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockDetailVO对象", description = "OutstockDetailVO对象")
public class OutstockDetailVO extends OutstockDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 上架计算代码描述
	 */
	@ApiModelProperty("分配计算代码描述")
	private String outstockFunctionDesc;
	@ApiModelProperty(value = "物品是否允许混放描述")
	private String skuMixDesc;
	@ApiModelProperty(value = "单据批属性信息")
	private List<OutstockConfigLotVO> detailConfigLot;
	@ApiModelProperty(value = "单据自定义属性信息")
	private List<OutstockConfigUdfVO> detailConfigUdf;
	@ApiModelProperty(value = "货源库区")
	private List<OutstockZoneDetailVO> outstockZoneDetail;
	@ApiModelProperty("执行条件信息")
	private List<OutstockConfigVO> outstockConfigList;
}
