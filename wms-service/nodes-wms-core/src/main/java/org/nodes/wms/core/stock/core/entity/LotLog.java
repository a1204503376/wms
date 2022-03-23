
package org.nodes.wms.core.stock.core.entity;

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

/**
 * 批次号日志实体类
 *
 * @author pengwei
 * @since 2020-03-03
 */
@Data
@TableName("wms_lot_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LotLog对象", description = "批次号日志")
public class LotLog extends SkuLotBaseEntity {

	private static final long serialVersionUID = 1L;
	public static final String proTypeDict = "pro_type";

	/**
	 * 批次号日志ID
	 */
	@ApiModelProperty(value = "批次号日志ID")
	@TableId(value = "wlll_id", type = IdType.AUTO)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wlllId;
	/**
	 * 批次号ID
	 */
	@ApiModelProperty(value = "批次号ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long lotId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String lotNumber;
	/**
	 * 批次状态
	 */
	@ApiModelProperty("批次状态")
	private Integer lotStatus;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty(value = "物品名称")
	private String skuName;
	/**
	 * 处理类型(0：新增，1：更新，2：删除，10：锁定，11：解锁)
	 */
	@ApiModelProperty(value = "处理类型(0：新增，1：更新，2：删除，10：锁定，11：解锁)")
	private Integer proType;
	/**
	 * 系统日志ID
	 */
	@ApiModelProperty(value = "系统日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long systemProcId;

}
