
package org.nodes.wms.core.stock.transfer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * 移动记录表实体类
 *
 * @author wangjw
 * @since 2020-02-27
 */
@Data
@TableName("wms_transfer_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TransferRecord对象", description = "移动记录表")
public class TransferRecord extends SkuLotBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 记录ID
	 */
	@TableId(value = "transfer_record_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "记录ID")
	private Long transferRecordId;
	/**
	 * 明细ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "明细ID")
	private Long transferDetailId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String transferCode;
	/**
	 * 移动类型
	 */
	@ApiModelProperty(value = "移动类型")
	private Integer transferType;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID")
	private Long woId;
	/**
	 * 货品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货品ID")
	private Long skuId;
	/**
	 * 原容器编码
	 */
	@ApiModelProperty(value = "原容器编码")
	private String fromLpn;
	/**
	 * 原库区编码
	 */
	@ApiModelProperty(value = "原库区编码")
	private String fromZoneCode;
	/**
	 * 原库区名称
	 */
	@ApiModelProperty(value = "原库区名称")
	private String fromZoneName;
	/**
	 * 原库位ID
	 */
	@ApiModelProperty("原库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long fromLocId;
	/**
	 * 原货位
	 */
	@ApiModelProperty(value = "原货位")
	private String fromLocCode;
	/**
	 * 目的容器编码
	 */
	@ApiModelProperty(value = "目的容器编码")
	private String toLpn;
	/**
	 * 目的库区编码
	 */
	@ApiModelProperty(value = "目的库区编码")
	private String toZoneCode;
	/**
	 * 目的库区名称
	 */
	@ApiModelProperty(value = "目的库区名称")
	private String toZoneName;
	/**
	 * 目的货位ID
	 */
	@ApiModelProperty("目的货位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long toLocId;
	/**
	 * 目的货位
	 */
	@ApiModelProperty(value = "目的货位")
	private String toLocCode;
	/**
	 * 数量
	 */
	@ApiModelProperty("数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal qty;
	/**
	 * 包装ID
	 */
	@ApiModelProperty("包装ID")
	private Long wspId;
	/**
	 * 包装层级
	 */
	@ApiModelProperty("包装层级")
	private Integer skuLevel;
	/**
	 * 计量单位编码
	 */
	@ApiModelProperty("计量单位编码")
	private String wsuCode;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty("计量单位名称")
	private String wsuName;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String lotNumber;

}
