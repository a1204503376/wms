package org.nodes.core.tool.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.nodes.core.tool.utils.StringPool;
import org.springblade.core.tenant.mp.TenantEntity;

import java.math.BigDecimal;

/**
 * @program: WmsCore
 * @description: 批属性验证基类
 * @author: pengwei
 * @create: 2020-12-18 14:45
 **/
@Data
public class SkuLotValBaseEntity extends TenantEntity implements ISkuLotValBase {
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust1;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust2;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust3;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust4;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust5;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust6;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust7;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust8;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust9;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust10;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust11;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust12;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust13;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust14;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust15;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust16;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust17;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust18;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust19;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust20;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust21;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust22;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust23;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust24;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust25;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust26;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust27;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust28;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust29;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust30;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp1;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp2;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp3;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp4;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp5;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp6;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp7;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp8;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp9;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp10;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp11;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp12;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp13;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp14;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp15;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp16;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp17;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp18;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp19;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp20;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp21;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp22;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp23;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp24;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp25;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp26;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp27;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp28;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp29;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp30;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix1;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix2;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix3;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix4;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix5;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix6;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix7;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix8;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix9;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix10;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix11;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix12;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix13;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix14;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix15;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix16;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix17;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix18;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix19;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix20;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix21;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix22;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix23;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix24;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix25;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix26;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix27;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix28;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix29;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix30;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask1;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask2;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask3;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask4;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask5;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask6;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask7;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask8;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask9;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask10;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask11;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask12;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask13;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask14;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask15;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask16;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask17;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask18;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask19;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask20;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask21;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask22;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask23;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask24;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask25;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask26;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask27;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask28;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask29;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	private String skuLotMixMask30;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask1;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask2;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask3;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask4;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask5;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask6;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask7;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask8;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask9;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask10;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask11;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask12;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask13;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask14;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask15;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask16;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask17;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask18;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask19;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask20;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask21;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask22;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask23;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask24;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask25;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask26;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask27;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask28;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask29;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask30;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType1;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType2;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType3;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType4;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType5;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType6;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType7;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType8;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType9;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType10;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType11;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType12;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType13;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType14;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType15;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType16;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType17;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType18;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType19;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType20;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType21;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType22;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType23;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType24;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType25;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType26;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType27;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType28;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType29;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType30;
	/**
	 * 长度限制1
	 */
	@ApiModelProperty("长度限制1")
	@Range(max = 99, min = 0, message = "长度限制1 的值应介于0~99之间")
	private Integer skuLen1;
	/**
	 * 长度限制2
	 */
	@ApiModelProperty("长度限制2")
	@Range(max = 99, min = 0, message = "长度限制2 的值应介于0~99之间")
	private Integer skuLen2;
	/**
	 * 长度限制3
	 */
	@ApiModelProperty("长度限制3")
	@Range(max = 99, min = 0, message = "长度限制3 的值应介于0~99之间")
	private Integer skuLen3;
	/**
	 * 长度限制4
	 */
	@ApiModelProperty("长度限制4")
	@Range(max = 99, min = 0, message = "长度限制4 的值应介于0~99之间")
	private Integer skuLen4;
	/**
	 * 长度限制5
	 */
	@ApiModelProperty("长度限制5")
	@Range(max = 99, min = 0, message = "长度限制5 的值应介于0~99之间")
	private Integer skuLen5;
	/**
	 * 长度限制6
	 */
	@ApiModelProperty("长度限制6")
	@Range(max = 99, min = 0, message = "长度限制6 的值应介于0~99之间")
	private Integer skuLen6;
	/**
	 * 长度限制7
	 */
	@ApiModelProperty("长度限制7")
	@Range(max = 99, min = 0, message = "长度限制7 的值应介于0~99之间")
	private Integer skuLen7;
	/**
	 * 长度限制8
	 */
	@ApiModelProperty("长度限制8")
	@Range(max = 99, min = 0, message = "长度限制8 的值应介于0~99之间")
	private Integer skuLen8;
	/**
	 * 长度限制9
	 */
	@ApiModelProperty("长度限制9")
	@Range(max = 99, min = 0, message = "长度限制9 的值应介于0~99之间")
	private Integer skuLen9;
	/**
	 * 长度限制10
	 */
	@ApiModelProperty("长度限制10")
	@Range(max = 99, min = 0, message = "长度限制10 的值应介于0~99之间")
	private Integer skuLen10;
	/**
	 * 长度限制11
	 */
	@ApiModelProperty("长度限制11")
	@Range(max = 99, min = 0, message = "长度限制11 的值应介于0~99之间")
	private Integer skuLen11;
	/**
	 * 长度限制12
	 */
	@ApiModelProperty("长度限制12")
	@Range(max = 99, min = 0, message = "长度限制12 的值应介于0~99之间")
	private Integer skuLen12;
	/**
	 * 长度限制13
	 */
	@ApiModelProperty("长度限制13")
	@Range(max = 99, min = 0, message = "长度限制13 的值应介于0~99之间")
	private Integer skuLen13;
	/**
	 * 长度限制14
	 */
	@ApiModelProperty("长度限制14")
	@Range(max = 99, min = 0, message = "长度限制14 的值应介于0~99之间")
	private Integer skuLen14;
	/**
	 * 长度限制15
	 */
	@ApiModelProperty("长度限制15")
	@Range(max = 99, min = 0, message = "长度限制15 的值应介于0~99之间")
	private Integer skuLen15;
	/**
	 * 长度限制16
	 */
	@ApiModelProperty("长度限制16")
	@Range(max = 99, min = 0, message = "长度限制16 的值应介于0~99之间")
	private Integer skuLen16;
	/**
	 * 长度限制17
	 */
	@ApiModelProperty("长度限制17")
	@Range(max = 99, min = 0, message = "长度限制17 的值应介于0~99之间")
	private Integer skuLen17;
	/**
	 * 长度限制18
	 */
	@ApiModelProperty("长度限制18")
	@Range(max = 99, min = 0, message = "长度限制18 的值应介于0~99之间")
	private Integer skuLen18;
	/**
	 * 长度限制19
	 */
	@ApiModelProperty("长度限制19")
	@Range(max = 99, min = 0, message = "长度限制19 的值应介于0~99之间")
	private Integer skuLen19;
	/**
	 * 长度限制20
	 */
	@ApiModelProperty("长度限制20")
	@Range(max = 99, min = 0, message = "长度限制20 的值应介于0~99之间")
	private Integer skuLen20;
	/**
	 * 长度限制21
	 */
	@ApiModelProperty("长度限制21")
	@Range(max = 99, min = 0, message = "长度限制21 的值应介于0~99之间")
	private Integer skuLen21;
	/**
	 * 长度限制22
	 */
	@ApiModelProperty("长度限制22")
	@Range(max = 99, min = 0, message = "长度限制22 的值应介于0~99之间")
	private Integer skuLen22;
	/**
	 * 长度限制23
	 */
	@ApiModelProperty("长度限制23")
	@Range(max = 99, min = 0, message = "长度限制23 的值应介于0~99之间")
	private Integer skuLen23;
	/**
	 * 长度限制24
	 */
	@ApiModelProperty("长度限制24")
	@Range(max = 99, min = 0, message = "长度限制24 的值应介于0~99之间")
	private Integer skuLen24;
	/**
	 * 长度限制25
	 */
	@ApiModelProperty("长度限制25")
	@Range(max = 99, min = 0, message = "长度限制25 的值应介于0~99之间")
	private Integer skuLen25;
	/**
	 * 长度限制26
	 */
	@ApiModelProperty("长度限制26")
	@Range(max = 99, min = 0, message = "长度限制26 的值应介于0~99之间")
	private Integer skuLen26;
	/**
	 * 长度限制27
	 */
	@ApiModelProperty("长度限制27")
	@Range(max = 99, min = 0, message = "长度限制27 的值应介于0~99之间")
	private Integer skuLen27;
	/**
	 * 长度限制28
	 */
	@ApiModelProperty("长度限制28")
	@Range(max = 99, min = 0, message = "长度限制28 的值应介于0~99之间")
	private Integer skuLen28;
	/**
	 * 长度限制29
	 */
	@ApiModelProperty("长度限制29")
	@Range(max = 99, min = 0, message = "长度限制29 的值应介于0~99之间")
	private Integer skuLen29;
	/**
	 * 长度限制30
	 */
	@ApiModelProperty("长度限制30")
	@Range(max = 99, min = 0, message = "长度限制30 的值应介于0~99之间")
	private Integer skuLen30;

	/**
	 * 根据索引获取该批属性是否为必填项
	 *
	 * @param index 批属性索引（从1开始）
	 * @return 0：非必须，1：必须
	 */
	@Override
	public Integer skuLotMustGet(int index) {
		switch (index) {
			case 1:
				return this.getSkuLotMust1();
			case 2:
				return this.getSkuLotMust2();
			case 3:
				return this.getSkuLotMust3();
			case 4:
				return this.getSkuLotMust4();
			case 5:
				return this.getSkuLotMust5();
			case 6:
				return this.getSkuLotMust6();
			case 7:
				return this.getSkuLotMust7();
			case 8:
				return this.getSkuLotMust8();
			case 9:
				return this.getSkuLotMust9();
			case 10:
				return this.getSkuLotMust10();
			case 11:
				return this.getSkuLotMust11();
			case 12:
				return this.getSkuLotMust12();
			case 13:
				return this.getSkuLotMust13();
			case 14:
				return this.getSkuLotMust14();
			case 15:
				return this.getSkuLotMust15();
			case 16:
				return this.getSkuLotMust16();
			case 17:
				return this.getSkuLotMust17();
			case 18:
				return this.getSkuLotMust18();
			case 19:
				return this.getSkuLotMust19();
			case 20:
				return this.getSkuLotMust20();
			case 21:
				return this.getSkuLotMust21();
			case 22:
				return this.getSkuLotMust22();
			case 23:
				return this.getSkuLotMust23();
			case 24:
				return this.getSkuLotMust24();
			case 25:
				return this.getSkuLotMust25();
			case 26:
				return this.getSkuLotMust26();
			case 27:
				return this.getSkuLotMust27();
			case 28:
				return this.getSkuLotMust28();
			case 29:
				return this.getSkuLotMust29();
			case 30:
				return this.getSkuLotMust30();
			default:
				return 0;
		}
	}

	/**
	 * 根据索引获取该批属性是否可见
	 *
	 * @param index 批属性索引（从1开始）
	 * @return 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@Override
	public Integer skuLotDispGet(int index) {
		switch (index) {
			case 1:
				return this.getSkuLotDisp1();
			case 2:
				return this.getSkuLotDisp2();
			case 3:
				return this.getSkuLotDisp3();
			case 4:
				return this.getSkuLotDisp4();
			case 5:
				return this.getSkuLotDisp5();
			case 6:
				return this.getSkuLotDisp6();
			case 7:
				return this.getSkuLotDisp7();
			case 8:
				return this.getSkuLotDisp8();
			case 9:
				return this.getSkuLotDisp9();
			case 10:
				return this.getSkuLotDisp10();
			case 11:
				return this.getSkuLotDisp11();
			case 12:
				return this.getSkuLotDisp12();
			case 13:
				return this.getSkuLotDisp13();
			case 14:
				return this.getSkuLotDisp14();
			case 15:
				return this.getSkuLotDisp15();
			case 16:
				return this.getSkuLotDisp16();
			case 17:
				return this.getSkuLotDisp17();
			case 18:
				return this.getSkuLotDisp18();
			case 19:
				return this.getSkuLotDisp19();
			case 20:
				return this.getSkuLotDisp20();
			case 21:
				return this.getSkuLotDisp21();
			case 22:
				return this.getSkuLotDisp22();
			case 23:
				return this.getSkuLotDisp23();
			case 24:
				return this.getSkuLotDisp24();
			case 25:
				return this.getSkuLotDisp25();
			case 26:
				return this.getSkuLotDisp26();
			case 27:
				return this.getSkuLotDisp27();
			case 28:
				return this.getSkuLotDisp28();
			case 29:
				return this.getSkuLotDisp29();
			case 30:
				return this.getSkuLotDisp30();
			default:
				return 0;
		}
	}

	/**
	 * 根据索引获取该批属性是否允许混放
	 * @param index 批属性索引(从1开始)
	 * @return 混合存放 0：不允许混合，1：允许混合
	 */
	@Override
	public Integer skuLotMixGet(int index) {
		switch (index) {
			case 1:
				return this.getSkuLotMix1();
			case 2:
				return this.getSkuLotMix2();
			case 3:
				return this.getSkuLotMix3();
			case 4:
				return this.getSkuLotMix4();
			case 5:
				return this.getSkuLotMix5();
			case 6:
				return this.getSkuLotMix6();
			case 7:
				return this.getSkuLotMix7();
			case 8:
				return this.getSkuLotMix8();
			case 9:
				return this.getSkuLotMix9();
			case 10:
				return this.getSkuLotMix10();
			case 11:
				return this.getSkuLotMix11();
			case 12:
				return this.getSkuLotMix12();
			case 13:
				return this.getSkuLotMix13();
			case 14:
				return this.getSkuLotMix14();
			case 15:
				return this.getSkuLotMix15();
			case 16:
				return this.getSkuLotMix16();
			case 17:
				return this.getSkuLotMix17();
			case 18:
				return this.getSkuLotMix18();
			case 19:
				return this.getSkuLotMix19();
			case 20:
				return this.getSkuLotMix20();
			case 21:
				return this.getSkuLotMix21();
			case 22:
				return this.getSkuLotMix22();
			case 23:
				return this.getSkuLotMix23();
			case 24:
				return this.getSkuLotMix24();
			case 25:
				return this.getSkuLotMix25();
			case 26:
				return this.getSkuLotMix26();
			case 27:
				return this.getSkuLotMix27();
			case 28:
				return this.getSkuLotMix28();
			case 29:
				return this.getSkuLotMix29();
			case 30:
				return this.getSkuLotMix30();
			default:
				return 0;
		}
	}

	/**
	 * 根据索引获取该批属性格式化规则
	 * @param index 批属性索引(从1开始)
	 * @return 批属性格式化规则
	 */
	@Override
	public String skuLotMixMaskGet(int index) {
		switch (index) {
			case 1:
				return this.getSkuLotMixMask1();
			case 2:
				return this.getSkuLotMixMask2();
			case 3:
				return this.getSkuLotMixMask3();
			case 4:
				return this.getSkuLotMixMask4();
			case 5:
				return this.getSkuLotMixMask5();
			case 6:
				return this.getSkuLotMixMask6();
			case 7:
				return this.getSkuLotMixMask7();
			case 8:
				return this.getSkuLotMixMask8();
			case 9:
				return this.getSkuLotMixMask9();
			case 10:
				return this.getSkuLotMixMask10();
			case 11:
				return this.getSkuLotMixMask11();
			case 12:
				return this.getSkuLotMixMask12();
			case 13:
				return this.getSkuLotMixMask13();
			case 14:
				return this.getSkuLotMixMask14();
			case 15:
				return this.getSkuLotMixMask15();
			case 16:
				return this.getSkuLotMixMask16();
			case 17:
				return this.getSkuLotMixMask17();
			case 18:
				return this.getSkuLotMixMask18();
			case 19:
				return this.getSkuLotMixMask19();
			case 20:
				return this.getSkuLotMixMask20();
			case 21:
				return this.getSkuLotMixMask21();
			case 22:
				return this.getSkuLotMixMask22();
			case 23:
				return this.getSkuLotMixMask23();
			case 24:
				return this.getSkuLotMixMask24();
			case 25:
				return this.getSkuLotMixMask25();
			case 26:
				return this.getSkuLotMixMask26();
			case 27:
				return this.getSkuLotMixMask27();
			case 28:
				return this.getSkuLotMixMask28();
			case 29:
				return this.getSkuLotMixMask29();
			case 30:
				return this.getSkuLotMixMask30();
			default:
				return StringPool.EMPTY;
		}
	}

	/**
	 * 根据索引获取该批属性生成掩码
	 * @param index 批属性索引(从1开始)
	 * @return 批属性生成掩码
	 */
	@Override
	public Integer skuLotMaskGet(int index) {
		switch (index) {
			case 1:
				return this.getSkuLotMask1();
			case 2:
				return this.getSkuLotMask2();
			case 3:
				return this.getSkuLotMask3();
			case 4:
				return this.getSkuLotMask4();
			case 5:
				return this.getSkuLotMask5();
			case 6:
				return this.getSkuLotMask6();
			case 7:
				return this.getSkuLotMask7();
			case 8:
				return this.getSkuLotMask8();
			case 9:
				return this.getSkuLotMask9();
			case 10:
				return this.getSkuLotMask10();
			case 11:
				return this.getSkuLotMask11();
			case 12:
				return this.getSkuLotMask12();
			case 13:
				return this.getSkuLotMask13();
			case 14:
				return this.getSkuLotMask14();
			case 15:
				return this.getSkuLotMask15();
			case 16:
				return this.getSkuLotMask16();
			case 17:
				return this.getSkuLotMask17();
			case 18:
				return this.getSkuLotMask18();
			case 19:
				return this.getSkuLotMask19();
			case 20:
				return this.getSkuLotMask20();
			case 21:
				return this.getSkuLotMask21();
			case 22:
				return this.getSkuLotMask22();
			case 23:
				return this.getSkuLotMask23();
			case 24:
				return this.getSkuLotMask24();
			case 25:
				return this.getSkuLotMask25();
			case 26:
				return this.getSkuLotMask26();
			case 27:
				return this.getSkuLotMask27();
			case 28:
				return this.getSkuLotMask28();
			case 29:
				return this.getSkuLotMask29();
			case 30:
				return this.getSkuLotMask30();
			default:
				return 0;
		}
	}

	/**
	 * 根据索引获取该批属性批掩码生成规则
	 * @param index 批属性索引
	 * @return 字典：sku_lot_val_ruler 1:自动允许用户编辑; 2:自动补允许用户编辑; 3:手动用户请求
	 */
	@Override
	public Integer skuLotEditTypeGet(int index) {
		switch (index) {
			case 1:
				return this.getSkuLotEditType1();
			case 2:
				return this.getSkuLotEditType2();
			case 3:
				return this.getSkuLotEditType3();
			case 4:
				return this.getSkuLotEditType4();
			case 5:
				return this.getSkuLotEditType5();
			case 6:
				return this.getSkuLotEditType6();
			case 7:
				return this.getSkuLotEditType7();
			case 8:
				return this.getSkuLotEditType8();
			case 9:
				return this.getSkuLotEditType9();
			case 10:
				return this.getSkuLotEditType10();
			case 11:
				return this.getSkuLotEditType11();
			case 12:
				return this.getSkuLotEditType12();
			case 13:
				return this.getSkuLotEditType13();
			case 14:
				return this.getSkuLotEditType14();
			case 15:
				return this.getSkuLotEditType15();
			case 16:
				return this.getSkuLotEditType16();
			case 17:
				return this.getSkuLotEditType17();
			case 18:
				return this.getSkuLotEditType18();
			case 19:
				return this.getSkuLotEditType19();
			case 20:
				return this.getSkuLotEditType20();
			case 21:
				return this.getSkuLotEditType21();
			case 22:
				return this.getSkuLotEditType22();
			case 23:
				return this.getSkuLotEditType23();
			case 24:
				return this.getSkuLotEditType24();
			case 25:
				return this.getSkuLotEditType25();
			case 26:
				return this.getSkuLotEditType26();
			case 27:
				return this.getSkuLotEditType27();
			case 28:
				return this.getSkuLotEditType28();
			case 29:
				return this.getSkuLotEditType29();
			case 30:
				return this.getSkuLotEditType30();
			default:
				return 0;
		}
	}

	/**
	 * 根据索引获取该批属性最大输入长度
	 * @param index 批属性索引
	 * @return 最大输入长度
	 */
	@Override
	public Integer skuLenGet(int index) {
		switch (index) {
			case 1:
				return this.getSkuLen1();
			case 2:
				return this.getSkuLen2();
			case 3:
				return this.getSkuLen3();
			case 4:
				return this.getSkuLen4();
			case 5:
				return this.getSkuLen5();
			case 6:
				return this.getSkuLen6();
			case 7:
				return this.getSkuLen7();
			case 8:
				return this.getSkuLen8();
			case 9:
				return this.getSkuLen9();
			case 10:
				return this.getSkuLen10();
			case 11:
				return this.getSkuLen11();
			case 12:
				return this.getSkuLen12();
			case 13:
				return this.getSkuLen13();
			case 14:
				return this.getSkuLen14();
			case 15:
				return this.getSkuLen15();
			case 16:
				return this.getSkuLen16();
			case 17:
				return this.getSkuLen17();
			case 18:
				return this.getSkuLen18();
			case 19:
				return this.getSkuLen19();
			case 20:
				return this.getSkuLen20();
			case 21:
				return this.getSkuLen21();
			case 22:
				return this.getSkuLen22();
			case 23:
				return this.getSkuLen23();
			case 24:
				return this.getSkuLen24();
			case 25:
				return this.getSkuLen25();
			case 26:
				return this.getSkuLen26();
			case 27:
				return this.getSkuLen27();
			case 28:
				return this.getSkuLen28();
			case 29:
				return this.getSkuLen29();
			case 30:
				return this.getSkuLen30();
			default:
				return 0;
		}
	}
}
