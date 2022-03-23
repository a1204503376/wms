
package org.nodes.wms.core.basedata.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.basedata.entity.SkuOutstock;

/**
 * 物品出库设置视图实体类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuOutstockVO对象", description = "物品出库设置")
public class SkuOutstockVO extends SkuOutstock {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 分配策略名称
	 */
	@ApiModelProperty("分配策略名称")
	private String ssoName;
	/**
	 * 默认出库包装名称
	 */
	@ApiModelProperty("默认出库包装名称")
	private String wspName;

	/**
	 * 包装明细ID
	 */
	@ApiModelProperty("包装明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspdId;

	/**
	 * 质检库区名称
	 */
	@ApiModelProperty("质检库区名称")
	private String qcZoneName;
	/**
	 * 质检库位名称
	 */
	@ApiModelProperty("质检库位编码")
	private String qcLocCode;
	/**
	 * 不合格品库区名称
	 */
	@ApiModelProperty("不合格品库区名称")
	private String unqualifiyZoneName;
	/**
	 * 不合格品库位名称
	 */
	@ApiModelProperty("不合格品库位编码")
	private String unqualifiyLocCode;
	/**
	 * 优先出库库区名称
	 */
	@ApiModelProperty("优先出库库区名称")
	private String firstSoZoneName;
	/**
	 * 默认出库层级
	 */
	@ApiModelProperty("默认出库层级")
	private String skuLevelDesc;
	/**
	 * 默认出库库位
	 */
	@ApiModelProperty("默认出库库位")
	private String locCode;

	/**
	 * 周转方式1
	 */
	@ApiModelProperty("周转方式1")
	private String turnoverType1Desc;
	/**
	 * 周转类型1
	 */
	@ApiModelProperty("周转类型1")
	private String turnoverItem1Desc;
	/**
	 * 周转方式2
	 */
	@ApiModelProperty("周转方式2")
	private String turnoverType2Desc;
	/**
	 * 周转类型2
	 */
	@ApiModelProperty("周转类型2")
	private String turnoverItem2Desc;
	/**
	 * 周转方式3
	 */
	@ApiModelProperty("周转方式3")
	private String turnoverType3Desc;
	/**
	 * 周转类型3
	 */
	@ApiModelProperty("周转类型3")
	private String turnoverItem3Desc;
}
