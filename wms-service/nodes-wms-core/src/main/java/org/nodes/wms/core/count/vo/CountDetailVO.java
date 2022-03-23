
package org.nodes.wms.core.count.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.vo.CountSkuQtyVO;
import org.nodes.wms.core.count.entity.CountDetail;

import java.util.List;

/**
 * 盘点单明细表视图实体类
 *
 * @author chz
 * @since 2020-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CountDetailVO对象", description = "盘点单明细表")
public class CountDetailVO extends CountDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 状态描述
	 */
	@ApiModelProperty(value = "状态描述")
	private String locStatusDesc;

	/**
	 * 盘点状态
	 */
	@ApiModelProperty(value = "盘点状态")
	private String countBillStateDesc;

	/**
	 * 盘点类型
	 */
	@ApiModelProperty(value = "盘点类型")
	private String countTagDesc;

	/**
	 * 盘点时间描述
	 */
	@ApiModelProperty(value = "盘点时间描述")
	private String ProcTimeDesc;

	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;

	/**
	 * 物品种类
	 */
	@ApiModelProperty(value = "物品种类")
	private int skuType;

	/**
	 * 手持选中状态专用
	 */
	@ApiModelProperty(value = "选中状态")
	private Boolean isModel=false;

	@ApiModelProperty(value="按物品盘点返回库位上所有的物品明细")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<CountDetail> countSkuDetails;

	@ApiModelProperty(value="物料库存数量及物料信息")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<CountSkuQtyVO> skuQtyVOS;
	@ApiModelProperty(value="盘点方式")
	private int countBy;
	/**
	 * 盘点模式
	 */
	@ApiModelProperty(value = "盘点模式")
	private String mode;
}
