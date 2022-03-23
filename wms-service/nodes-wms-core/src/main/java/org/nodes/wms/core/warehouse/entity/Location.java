
package org.nodes.wms.core.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库位管理 实体类
 *
 * @author zhongls
 * @since 2019-12-11
 */
@Data
@TableName("wms_location")
@ApiModel(value = "Location对象", description = "Location对象")
public class Location extends TenantEntity {

	private static final long serialVersionUID = 1L;
	public static final String DATA_TYPE = StringPool.ONE;

	public static final String STATUS = "loc_status";
	/**
	 * 库位ID
	 */
	@ApiModelProperty(value = "库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "loc_id", type = IdType.ASSIGN_ID)
	private Long locId;
	/**
	 * 库区ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库区ID")
	@NotNull
	private Long zoneId;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	@NotNull
	private Long whId;
	/**
	 * 库位编码
	 */
	@ApiModelProperty(value = "库位编码")
	private String locCode;
	/**
	 * 库位类型
	 */
	@ApiModelProperty(value = "库位类型")
	private Integer locType;
	/**
	 * 库位种类
	 */
	@ApiModelProperty(value = "库位种类")
	private Integer locCategory;
	/**
	 * 库位处理
	 */
	@ApiModelProperty(value = "库位处理")
	private Integer locHandling;
	/**
	 * 分配区
	 */
	@ApiModelProperty(value = "分配区")
	private String allocationZone;

	/**
	 * 使用状态
	 */
	@ApiModelProperty(value = "使用状态")
	private Integer locFlag;

	/**
	 * ABC分类
	 */
	@ApiModelProperty(value = "ABC分类")
	private Integer abc;


	/**
	 * 库存差异是否自动冻结货位
	 */
	@ApiModelProperty(value = "库存差异是否自动冻结货位")
	private String lockFlag;
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer locStatus;
	/**
	 * 路线顺序
	 */
	@ApiModelProperty(value = "路线顺序")
	private Integer logicAllocation;

	/**
	 * 长
	 */
	@ApiModelProperty(value = "长")
	private BigDecimal locLength;
	/**
	 * 宽
	 */
	@ApiModelProperty(value = "宽")
	private BigDecimal locWide;
	/**
	 * 高
	 */
	@ApiModelProperty(value = "高")
	private BigDecimal locHigh;
	/**
	 * 货架层
	 */
	@ApiModelProperty(value = "货架层")
	private String locLevel;
	/**
	 * X坐标
	 */
	@ApiModelProperty(value = "X坐标")
	private Integer xCode;
	/**
	 * Y坐标
	 */
	@ApiModelProperty(value = "Y坐标")
	private Integer yCode;
	/**
	 * Z坐标
	 */
	@ApiModelProperty(value = "Z坐标")
	private Integer zCode;
	/**
	 * 定方位
	 */
	@ApiModelProperty(value = "定方位")
	private String orientation;
	/**
	 * 容量
	 */
	@ApiModelProperty(value = "容量")
	private BigDecimal capacity;
	/**
	 * 载重量
	 */
	@ApiModelProperty(value = "载重量")
	private BigDecimal loadWeight;
	/**
	 * 最大存放件数
	 */
	@ApiModelProperty(value = "最大存放件数")
	private Integer itemNum;
	/**
	 * 最大存放托数
	 */
	@ApiModelProperty(value = "最大存放托数")
	private Integer trayNum;
	/**
	 * 盘点单编码
	 */
	@ApiModelProperty(value = "盘点单编码")
	private String countBillNo;
	/**
	 * 库位共享宽度
	 */
	@ApiModelProperty(value = "库位共享宽度")
	private String shareWidth;
	/**
	 * 库位共享重量
	 */
	@ApiModelProperty(value = "库位共享重量")
	private String shareWeight;
	/**
	 * 混放货品
	 */
	@ApiModelProperty(value = "混放货品")
	private String locSkuMix;
	/**
	 * 混放批号
	 */
	@ApiModelProperty(value = "混放批号")
	private String locLotNoMix;
	/**
	 * 混放批属性1
	 */
	@ApiModelProperty(value = "混放批属性1")
	private String locLotMix1;
	/**
	 * 混放批属性2
	 */
	@ApiModelProperty(value = "混放批属性2")
	private String locLotMix2;
	/**
	 * 混放批属性3
	 */
	@ApiModelProperty(value = "混放批属性3")
	private String locLotMix3;
	/**
	 * 混放批属性4
	 */
	@ApiModelProperty(value = "混放批属性4")
	private String locLotMix4;
	/**
	 * 混放批属性5
	 */
	@ApiModelProperty(value = "混放批属性5")
	private String locLotMix5;
	/**
	 * 混放批属性6
	 */
	@ApiModelProperty(value = "混放批属性6")
	private String locLotMix6;
	/**
	 * 混放批属性7
	 */
	@ApiModelProperty(value = "混放批属性7")
	private String locLotMix7;
	/**
	 * 混放批属性8
	 */
	@ApiModelProperty(value = "混放批属性8")
	private String locLotMix8;
	/**
	 * 混放批属性9
	 */
	@ApiModelProperty(value = "混放批属性9")
	private String locLotMix9;
	/**
	 * 混放批属性10
	 */
	@ApiModelProperty(value = "混放批属性10")
	private String locLotMix10;
	/**
	 * 混放批属性11
	 */
	@ApiModelProperty(value = "混放批属性11")
	private String locLotMix11;
	/**
	 * 混放批属性12
	 */
	@ApiModelProperty(value = "混放批属性12")
	private String locLotMix12;
	/**
	 * 混放批属性13
	 */
	@ApiModelProperty(value = "混放批属性13")
	private String locLotMix13;
	/**
	 * 混放批属性14
	 */
	@ApiModelProperty(value = "混放批属性14")
	private String locLotMix14;
	/**
	 * 混放批属性15
	 */
	@ApiModelProperty(value = "混放批属性15")
	private String locLotMix15;
	/**
	 * 混放批属性16
	 */
	@ApiModelProperty(value = "混放批属性16")
	private String locLotMix16;
	/**
	 * 混放批属性17
	 */
	@ApiModelProperty(value = "混放批属性17")
	private String locLotMix17;
	/**
	 * 混放批属性18
	 */
	@ApiModelProperty(value = "混放批属性18")
	private String locLotMix18;
	/**
	 * 混放批属性19
	 */
	@ApiModelProperty(value = "混放批属性19")
	private String locLotMix19;
	/**
	 * 混放批属性20
	 */
	@ApiModelProperty(value = "混放批属性20")
	private String locLotMix20;
	/**
	 * 混放批属性21
	 */
	@ApiModelProperty(value = "混放批属性21")
	private String locLotMix21;
	/**
	 * 混放批属性22
	 */
	@ApiModelProperty(value = "混放批属性22")
	private String locLotMix22;
	/**
	 * 混放批属性23
	 */
	@ApiModelProperty(value = "混放批属性23")
	private String locLotMix23;
	/**
	 * 混放批属性24
	 */
	@ApiModelProperty(value = "混放批属性24")
	private String locLotMix24;
	/**
	 * 混放批属性25
	 */
	@ApiModelProperty(value = "混放批属性25")
	private String locLotMix25;
	/**
	 * 混放批属性26
	 */
	@ApiModelProperty(value = "混放批属性26")
	private String locLotMix26;
	/**
	 * 混放批属性27
	 */
	@ApiModelProperty(value = "混放批属性27")
	private String locLotMix27;
	/**
	 * 混放批属性28
	 */
	@ApiModelProperty(value = "混放批属性28")
	private String locLotMix28;
	/**
	 * 混放批属性29
	 */
	@ApiModelProperty(value = "混放批属性29")
	private String locLotMix29;
	/**
	 * 混放批属性30
	 */
	@ApiModelProperty(value = "混放批属性30")
	private String locLotMix30;

	/**
	 * 上次库位盘点发布时间
	 */
	@ApiModelProperty(value = "上次库位盘点发布时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastCycleDate;
	/**
	 * 上次盘点货位日期
	 */
	@ApiModelProperty(value = "上次盘点货位日期")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastLocCountDate;


	/**
	 * 获取库位批属性混放模式
	 *
	 * @param index 索引(从1开始)
	 * @return 批属性混放模式(1 : 不允许, 2 : 允许)
	 */
	public String getLocLotMix(Integer index) {
		switch (index) {
			case 1:
				return this.locLotMix1;
			case 2:
				return this.locLotMix2;
			case 3:
				return this.locLotMix3;
			case 4:
				return this.locLotMix4;
			case 5:
				return this.locLotMix5;
			case 6:
				return this.locLotMix6;
			case 7:
				return this.locLotMix7;
			case 8:
				return this.locLotMix8;
			case 9:
				return this.locLotMix9;
			case 10:
				return this.locLotMix10;
			case 11:
				return this.locLotMix11;
			case 12:
				return this.locLotMix12;
			case 13:
				return this.locLotMix13;
			case 14:
				return this.locLotMix14;
			case 15:
				return this.locLotMix15;
			case 16:
				return this.locLotMix16;
			case 17:
				return this.locLotMix17;
			case 18:
				return this.locLotMix18;
			case 19:
				return this.locLotMix19;
			case 20:
				return this.locLotMix20;
			case 21:
				return this.locLotMix21;
			case 22:
				return this.locLotMix22;
			case 23:
				return this.locLotMix23;
			case 24:
				return this.locLotMix24;
			case 25:
				return this.locLotMix25;
			case 26:
				return this.locLotMix26;
			case 27:
				return this.locLotMix27;
			case 28:
				return this.locLotMix28;
			case 29:
				return this.locLotMix29;
			case 30:
				return this.locLotMix30;
			default:
				return StringPool.EMPTY;
		}
	}
}
