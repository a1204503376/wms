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
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库内移动明细关联实体类
 *
 * @author pengwei
 * @since 2020-08-03
 */
@Data
@TableName("wms_transfer_detail_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TransferDetailItem对象", description = "库内移动明细关联")
public class TransferDetailItem extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 库内移动明细关联ID
	 */
	@ApiModelProperty(value = "库内移动明细关联ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wtdi_id", type = IdType.ASSIGN_ID)
	private Long wtdiId;
	/**
	 * 库内移动表头ID
	 */
	@ApiModelProperty(value = "库内移动表头ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long transferHeaderId;
	/**
	 * 库内移动明细ID
	 */
	@ApiModelProperty(value = "库内移动明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long transferDetailId;
	/**
	 * 订单表头ID
	 */
	@ApiModelProperty(value = "订单表头ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billId;
	/**
	 * 订单明细ID
	 */
	@ApiModelProperty(value = "订单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long detailId;
	/**
	 * 订单数量
	 */
	@ApiModelProperty(value = "订单数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal qty;
}
