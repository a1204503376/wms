
package org.nodes.wms.core.basedata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.basedata.entity.SkuInstock;

/**
 * 物品入库设置视图实体类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuInstockVO对象", description = "物品入库设置")
public class SkuInstockVO extends SkuInstock {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 上架策略名称
	 */
	@ApiModelProperty("上架策略名称")
	private String ssiName;

	/**
	 * 上架策略执行类型名称
	 */
	@ApiModelProperty("上架策略执行类型名称")
	private String ssiProcTypeName;

	/**
	 * 上架库区名称
	 */
	@ApiModelProperty("上架库区名称")
	private String zoneName;
	/**
	 * 上架库位编码
	 */
	@ApiModelProperty("上架库位")
	private String locCode;
	/**
	 * 质检库区名称
	 */
	@ApiModelProperty("质检库区名称")
	private String qcZoneName;
	/**
	 * 质检库位编码
	 */
	@ApiModelProperty("质检库位编码")
	private String qcLocCode;
	/**
	 * 不合格品库区名称
	 */
	@ApiModelProperty("不合格品库区名称")
	private String unqualifiyZoneName;
	/**
	 * 不合格品库位编码
	 */
	@ApiModelProperty("不合格品库位编码")
	private String unqualifiyLocCode;
	/**
	 * 退货库区名称
	 */
	@ApiModelProperty("退货库区名称")
	private String returnZoneName;
	/**
	 * 退货库位编码
	 */
	@ApiModelProperty("退货库位编码")
	private String returnLocCode;
	/**
	 * 拣货库区名称
	 */
	@ApiModelProperty("拣货库区名称")
	private String pickZoneName;
	/**
	 * 拣货库位编码
	 */
	@ApiModelProperty("拣货库位编码")
	private String pickLocCode;
}
