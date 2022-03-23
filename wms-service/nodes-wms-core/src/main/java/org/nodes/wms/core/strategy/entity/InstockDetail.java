
package org.nodes.wms.core.strategy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.utils.StringPool;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 上架策略明细实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@TableName("st_instock_detail")
@ApiModel(value = "InstockDetail对象", description = "InstockDetail对象")
public class InstockDetail extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 上架策略明细ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "上架策略明细ID")
	@TableId(value = "ssid_id", type = IdType.ASSIGN_ID)
	private Long ssidId;
	/**
	 * 上架策略ID
	 */
	@ApiModelProperty(value = "上架策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssiId;
	/**
	 * 从库区类型
	 */
	@ApiModelProperty("从库区类型")
	private Integer fromZoneType;
	/**
	 * 从库区ID
	 */
	@ApiModelProperty(value = "从库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long fromZoneId;
	/**
	 * 从库位ID
	 */
	@ApiModelProperty(value = "自库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long fromLocId;
	/**
	 * 至库区类型
	 */
	@ApiModelProperty("至库区类型")
	private Integer toZoneType;
	/**
	 * 至库区ID
	 */
	@ApiModelProperty(value = "至库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long toZoneId;
	/**
	 * 至库位ID
	 */
	@ApiModelProperty(value = "至库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long toLocId;
	/**
	 * 库位排序类型
	 */
	@ApiModelProperty(value = "库位排序类型")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer locSortType;
	/**
	 * 库位排序方式 0：升序 1：降序
	 */
	@ApiModelProperty(value = "库位排序方式 0：升序 1：降序")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer locSortMode;
	/**
	 * 上架计算代码 随机存储，定址定位，合并存储，寻找空库位
	 */
	@ApiModelProperty(value = "上架计算代码 随机存储，定址定位，合并存储，寻找空库位")
	private Integer instockFunction;
	/**
	 * 物品abc分类
	 */
	@ApiModelProperty("物品abc分类")
	private Integer abc;

	/**
	 * 合并条件(重量) 0：不限制，1：限制
	 */
	@ApiModelProperty(value = "合并条件(重量) 0：不限制，1：限制")
	private Integer confMixWight;
	/**
	 * 合并条件(体积) 0：不限制，1：限制
	 */
	@ApiModelProperty(value = "合并条件(体积) 0：不限制，1：限制")
	private Integer confMixVolume;
	/**
	 * 合并条件(箱数) 0：不限制，1：限制
	 */
	@ApiModelProperty(value = "合并条件(箱数) 0：不限制，1：限制")
	private Integer confMixBoxCount;
	/**
	 * 物品是否允许混放 0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "物品是否允许混放 0：不允许混合，1：允许混合")
	private Integer skuMix;
	/**
	 * 混合存放1 0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放1 0：不允许混合，1：允许混合")
	private String skuLotMix1;
	/**
	 * 混合存放2 0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放2 0：不允许混合，1：允许混合")
	private String skuLotMix2;
	/**
	 * 混合存放3 0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放3 0：不允许混合，1：允许混合")
	private String skuLotMix3;
	/**
	 * 混合存放4 0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放4 0：不允许混合，1：允许混合")
	private String skuLotMix4;
	/**
	 * 混合存放5 0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放5 0：不允许混合，1：允许混合")
	private String skuLotMix5;
	/**
	 * 混合存放6
	 */
	@ApiModelProperty(value = "混合存放6")
	private String skuLotMix6;
	/**
	 * 混合存放7
	 */
	@ApiModelProperty(value = "混合存放7")
	private String skuLotMix7;
	/**
	 * 混合存放8
	 */
	@ApiModelProperty(value = "混合存放8")
	private String skuLotMix8;
	/**
	 * 混合存放9
	 */
	@ApiModelProperty(value = "混合存放9")
	private String skuLotMix9;
	/**
	 * 混合存放10
	 */
	@ApiModelProperty(value = "混合存放10")
	private String skuLotMix10;
	/**
	 * 混合存放11
	 */
	@ApiModelProperty(value = "混合存放11")
	private String skuLotMix11;
	/**
	 * 混合存放12
	 */
	@ApiModelProperty(value = "混合存放12")
	private String skuLotMix12;
	/**
	 * 混合存放13
	 */
	@ApiModelProperty(value = "混合存放13")
	private String skuLotMix13;
	/**
	 * 混合存放14
	 */
	@ApiModelProperty(value = "混合存放14")
	private String skuLotMix14;
	/**
	 * 混合存放15
	 */
	@ApiModelProperty(value = "混合存放15")
	private String skuLotMix15;
	/**
	 * 混合存放16
	 */
	@ApiModelProperty(value = "混合存放16")
	private String skuLotMix16;
	/**
	 * 混合存放17
	 */
	@ApiModelProperty(value = "混合存放17")
	private String skuLotMix17;
	/**
	 * 混合存放18
	 */
	@ApiModelProperty(value = "混合存放18")
	private String skuLotMix18;
	/**
	 * 混合存放19
	 */
	@ApiModelProperty(value = "混合存放19")
	private String skuLotMix19;
	/**
	 * 混合存放20
	 */
	@ApiModelProperty(value = "混合存放20")
	private String skuLotMix20;
	/**
	 * 混合存放21
	 */
	@ApiModelProperty(value = "混合存放21")
	private String skuLotMix21;
	/**
	 * 混合存放22
	 */
	@ApiModelProperty(value = "混合存放22")
	private String skuLotMix22;
	/**
	 * 混合存放23
	 */
	@ApiModelProperty(value = "混合存放23")
	private String skuLotMix23;
	/**
	 * 混合存放24
	 */
	@ApiModelProperty(value = "混合存放24")
	private String skuLotMix24;
	/**
	 * 混合存放25
	 */
	@ApiModelProperty(value = "混合存放25")
	private String skuLotMix25;
	/**
	 * 混合存放26
	 */
	@ApiModelProperty(value = "混合存放26")
	private String skuLotMix26;
	/**
	 * 混合存放27
	 */
	@ApiModelProperty(value = "混合存放27")
	private String skuLotMix27;
	/**
	 * 混合存放28
	 */
	@ApiModelProperty(value = "混合存放28")
	private String skuLotMix28;
	/**
	 * 混合存放29
	 */
	@ApiModelProperty(value = "混合存放29")
	private String skuLotMix29;
	/**
	 * 混合存放30
	 */
	@ApiModelProperty(value = "混合存放30")
	private String skuLotMix30;
	/**
	 * 处理顺序
	 */
	@ApiModelProperty(value = "处理顺序")
	private Integer ssidProcOrder;

	/**
	 * 获取混放模式
	 *
	 * @param index 索引(从 1 开始)
	 * @return 混放模式
	 */
	public String getSkuLotMix(Integer index) {
		switch(index) {
			case 1:
				return this.skuLotMix1;
			case 2:
				return this.skuLotMix2;
			case 3:
				return this.skuLotMix3;
			case 4:
				return this.skuLotMix4;
			case 5:
				return this.skuLotMix5;
			case 6:
				return this.skuLotMix6;
			case 7:
				return this.skuLotMix7;
			case 8:
				return this.skuLotMix8;
			case 9:
				return this.skuLotMix9;
			case 10:
				return this.skuLotMix10;
			case 11:
				return this.skuLotMix11;
			case 12:
				return this.skuLotMix12;
			case 13:
				return this.skuLotMix13;
			case 14:
				return this.skuLotMix14;
			case 15:
				return this.skuLotMix15;
			case 16:
				return this.skuLotMix16;
			case 17:
				return this.skuLotMix17;
			case 18:
				return this.skuLotMix18;
			case 19:
				return this.skuLotMix19;
			case 20:
				return this.skuLotMix20;
			case 21:
				return this.skuLotMix21;
			case 22:
				return this.skuLotMix22;
			case 23:
				return this.skuLotMix23;
			case 24:
				return this.skuLotMix24;
			case 25:
				return this.skuLotMix25;
			case 26:
				return this.skuLotMix26;
			case 27:
				return this.skuLotMix27;
			case 28:
				return this.skuLotMix28;
			case 29:
				return this.skuLotMix29;
			case 30:
				return this.skuLotMix30;
			default:
				return StringPool.EMPTY;
		}
	}
}
