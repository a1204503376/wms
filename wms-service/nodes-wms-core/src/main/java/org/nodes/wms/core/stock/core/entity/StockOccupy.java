
package org.nodes.wms.core.stock.core.entity;

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
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存占用表实体类
 *
 * @author pengwei
 * @since 2020-02-17
 */
@Data
@TableName("wms_stock_occupy")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockOccupy对象", description = "库存占用表")
public class StockOccupy extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 库存占用ID
	 */
	@ApiModelProperty(value = "库存占用ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wso_id", type = IdType.ASSIGN_ID)
	private Long wsoId;
	/**
	 * 事务ID(拣货分配占用：波次ID，盘点占用：盘点单ID，出库预约占用：出库单ID)
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "事务ID(拣货分配占用：波次ID，盘点占用：盘点单ID，出库预约占用：出库单ID)")
	private Long transId;
	/**
	 * 占用类型(10：拣货分配占用，20：盘点占用，30：出库预约占用)
	 */
	@ApiModelProperty(value = "占用类型(10：拣货分配占用，20：盘点占用，30：出库预约占用)")
	private Integer occupyType;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
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
	 * 系统日志ID
	 */
	@ApiModelProperty(value = "系统日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long systemProcId;
	/**
	 * 占用开始时间
	 */
	@ApiModelProperty(value = "占用开始时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime occupyTime;
	/**
	 * 占用数量
	 */
	@ApiModelProperty(value = "占用数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal occupyQty;
	/**
	 * 发货单ID(占用类型=10（拣货分配占用）or 30（出库预约占用）时必须)
	 */
	@ApiModelProperty(value = "发货单ID(占用类型=10（拣货分配占用）or 30（出库预约占用）时必须)")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 单据编码(占用类型=10（拣货分配占用）or 30（出库预约占用）时必须)
	 */
	@ApiModelProperty(value = "单据编码(占用类型=10（拣货分配占用）or 30（出库预约占用）时必须)")
	private String soBillNo;
	/**
	 * 出库单明细ID(占用类型=10（拣货分配占用）or 30（出库预约占用）时必须)
	 */
	@ApiModelProperty(value = "出库单明细ID(占用类型=10（拣货分配占用）or 30（出库预约占用）时必须)")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;
	/**
	 * 拣货计划ID(占用类型=10（拣货分配占用）or 30（出库预约占用）时必须)
	 */
	@ApiModelProperty(value = "拣货计划ID(占用类型=10（拣货分配占用）or 30（出库预约占用）时必须)")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pickPlanId;
	/**
	 * 盘点单报告ID(占用类型=20（盘点占用）时必须)
	 */
	@ApiModelProperty(value = "盘点单报告ID(占用类型=20（盘点占用）时必须)")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wcrId;

	/**
	 * 序列号
	 */
	@ApiModelProperty("序列号")
	private String serialNumber;

}
