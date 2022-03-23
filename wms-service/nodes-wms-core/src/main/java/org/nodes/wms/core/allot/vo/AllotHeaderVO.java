
package org.nodes.wms.core.allot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.allot.entity.AllotHeader;

import java.util.List;

/**
 * 调拨单头表视图实体类
 *
 * @author Wangjw
 * @since 2020-01-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AllotHeaderVO对象", description = "调拨单头表")
public class AllotHeaderVO extends AllotHeader {
	private static final long serialVersionUID = 1L;

	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态名称")
	private String allotBillStateName;

	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;

	/**
	 * 出库单明细
	 */
	@ApiModelProperty(value = "调拨单明细")
	private List<AllotDetailVO> detailList;

	/**
	 * 调出库房名称
	 */
	@ApiModelProperty(value = "调出库房名称")
	private String sourceWhName;

	/**
	 * 调入库房名称
	 */
	@ApiModelProperty(value = "调入库房名称")
	private String targetWhName;

	/**
	 * 库区类型
	 */
	@ApiModelProperty(value = "库区类型")
	private String zoneName;

}
