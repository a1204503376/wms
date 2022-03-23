
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;

import java.util.List;

/**
 *补货策略 视图实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RelenishmentDetailVO对象", description = "RelenishmentDetailVO对象")
public class RelenishmentDetailVO extends RelenishmentDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 补货计算代码描述
	 */
	@ApiModelProperty("补货计算代码描述")
	private String instockFunctionDesc;
	/**
	 * 从库区类型名称
	 */
	@ApiModelProperty("从库区类型名称")
	private String fromZoneTypeName;
	/**
	 * 从库区名称
	 */
	@ApiModelProperty("自库区名称")
	private String fromZoneName;
	/**
	 * 从库区编码
	 */
	@ApiModelProperty("至库区编码")
	private String fromLocCode;
	/**
	 * 至库区类型名称
	 */
	@ApiModelProperty("至库区类型名称")
	private String toZoneTypeName;
	/**
	 * 至库位名称
	 */
	@ApiModelProperty("自库位名称")
	private String toZoneName;
	/**
	 * 补货层级
	 */
	@ApiModelProperty("补货层级")
	private String skuLevelDesc;
	/**
	 * 至库位编码
	 */
	@ApiModelProperty("至库位编码")
	private String toLocCode;
	/**
	 * 合并条件（重量）描述
	 */
	@ApiModelProperty("合并条件（重量）描述")
	private String confMixWightDesc;
	/**
	 * 合并条件（重量）描述（Bool类型）
	 */
	@ApiModelProperty("合并条件（重量）描述（Bool类型）")
	private boolean confMixWightBool;
	/**
	 * 合并条件（体积）描述
	 */
	@ApiModelProperty("合并条件（体积）描述")
	private String confMixVolumeDesc;
	/**
	 * 合并条件（体积）描述（Bool类型）
	 */
	@ApiModelProperty("合并条件（体积）描述（Bool类型）")
	private boolean confMixVolumeBool;
	/**
	 * 合并条件（箱数）描述
	 */
	@ApiModelProperty("合并条件（箱数）描述")
	private String confMixBoxCountDesc;
	/**
	 * 合并条件（箱数）描述（Bool类型）
	 */
	@ApiModelProperty("合并条件（箱数）描述（Bool类型）")
	private boolean confMixBoxCountBool;
	/**
	 * 物品是否允许混放描述
	 */
	@ApiModelProperty("物品是否允许混放描述")
	private String skuMixDesc;

	/**
	 * 物品是否允许混放
	 */
	@ApiModelProperty("物品是否允许混放")
	private boolean skuMixBool;

	/**
	 * 批属性设定集合
	 */
	@ApiModelProperty("批属性设定集合")
	private List<InstockConfigLotVO> instockConfigLotList;
	/**
	 * 单据明细集合
	 */
	@ApiModelProperty("物品明细集合")
	private List<InstockConfigVO> instockConfigList;
	/**
	 * 混合存放1
	 */
	@ApiModelProperty("混合存放1")
	private String skuLotMix1Desc;
	/**
	 * 混合存放2
	 */
	@ApiModelProperty("混合存放2")
	private String skuLotMix2Desc;
	/**
	 * 混合存放3
	 */
	@ApiModelProperty("混合存放3")
	private String skuLotMix3Desc;
	/**
	 * 混合存放4
	 */
	@ApiModelProperty("混合存放4")
	private String skuLotMix4Desc;
	/**
	 * 混合存放5
	 */
	@ApiModelProperty("混合存放5")
	private String skuLotMix5Desc;
	/**
	 * 混合存放6
	 */
	@ApiModelProperty("混合存放6")
	private String skuLotMix6Desc;
	/**
	 * 混合存放7
	 */
	@ApiModelProperty("混合存放7")
	private String skuLotMix7Desc;
	/**
	 * 混合存放8
	 */
	@ApiModelProperty("混合存放8")
	private String skuLotMix8Desc;
	/**
	 * 混合存放9
	 */
	@ApiModelProperty("混合存放9")
	private String skuLotMix9Desc;
	/**
	 * 混合存放10
	 */
	@ApiModelProperty("混合存放10")
	private String skuLotMix10Desc;
	/**
	 * 混合存放11
	 */
	@ApiModelProperty("混合存放11")
	private String skuLotMix11Desc;
	/**
	 * 混合存放12
	 */
	@ApiModelProperty("混合存放12")
	private String skuLotMix12Desc;
	/**
	 * 混合存放13
	 */
	@ApiModelProperty("混合存放13")
	private String skuLotMix13Desc;
	/**
	 * 混合存放14
	 */
	@ApiModelProperty("混合存放14")
	private String skuLotMix14Desc;
	/**
	 * 混合存放15
	 */
	@ApiModelProperty("混合存放15")
	private String skuLotMix15Desc;
	/**
	 * 混合存放16
	 */
	@ApiModelProperty("混合存放16")
	private String skuLotMix16Desc;
	/**
	 * 混合存放17
	 */
	@ApiModelProperty("混合存放17")
	private String skuLotMix17Desc;
	/**
	 * 混合存放18
	 */
	@ApiModelProperty("混合存放18")
	private String skuLotMix18Desc;
	/**
	 * 混合存放19
	 */
	@ApiModelProperty("混合存放19")
	private String skuLotMix19Desc;
	/**
	 * 混合存放20
	 */
	@ApiModelProperty("混合存放20")
	private String skuLotMix20Desc;
	/**
	 * 混合存放21
	 */
	@ApiModelProperty("混合存放21")
	private String skuLotMix21Desc;
	/**
	 * 混合存放22
	 */
	@ApiModelProperty("混合存放22")
	private String skuLotMix22Desc;
	/**
	 * 混合存放23
	 */
	@ApiModelProperty("混合存放23")
	private String skuLotMix23Desc;
	/**
	 * 混合存放24
	 */
	@ApiModelProperty("混合存放24")
	private String skuLotMix24Desc;
	/**
	 * 混合存放25
	 */
	@ApiModelProperty("混合存放25")
	private String skuLotMix25Desc;
	/**
	 * 混合存放26
	 */
	@ApiModelProperty("混合存放26")
	private String skuLotMix26Desc;
	/**
	 * 混合存放27
	 */
	@ApiModelProperty("混合存放27")
	private String skuLotMix27Desc;
	/**
	 * 混合存放28
	 */
	@ApiModelProperty("混合存放28")
	private String skuLotMix28Desc;
	/**
	 * 混合存放29
	 */
	@ApiModelProperty("混合存放29")
	private String skuLotMix29Desc;
	/**
	 * 混合存放30
	 */
	@ApiModelProperty("混合存放30")
	private String skuLotMix30Desc;

}
