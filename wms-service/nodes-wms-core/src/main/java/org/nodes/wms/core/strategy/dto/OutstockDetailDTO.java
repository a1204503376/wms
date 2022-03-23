
package org.nodes.wms.core.strategy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.*;

import java.util.List;

/**
 * 分配策略明细 数据传输对象实体类
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OutstockDetailDTO extends OutstockDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 分配策略批属性设定信息
	 */
	@ApiModelProperty(value = "批属性设定信息")
	private List<OutstockConfigLot> detailConfigLot;
	@ApiModelProperty(value = "批属性设定信息(客户端删除项)")
	private List<OutstockConfigLot> detailConfigLotDeletedList;
	/**
	 * 分配策略单据自定义属性信息
	 */
	@ApiModelProperty(value = "单据自定义属性信息")
	private List<OutstockConfigUdf> detailConfigUdf;
	@ApiModelProperty(value = "单据自定义属性信息(客户端删除项)")
	private List<OutstockConfigUdf> detailConfigUdfDeletedList;
	/**
	 * 分配策略货源库区信息
	 */
	@ApiModelProperty(value = "货源库区信息")
	private List<OutstockZoneDetail> outstockZoneDetail;
	@ApiModelProperty(value = "货源库区信息(客户端删除项)")
	private List<OutstockZoneDetail> outstockZoneDetailDeletedList;
	/**
	 * 分配策略执行条件
	 */
	@ApiModelProperty("分配策略执行条件信息")
	private List<OutstockConfig> outstockConfigList;
	@ApiModelProperty("分配策略执行条件信息(客户端删除项)")
	private List<OutstockConfig> outstockConfigDeletedList;
}
