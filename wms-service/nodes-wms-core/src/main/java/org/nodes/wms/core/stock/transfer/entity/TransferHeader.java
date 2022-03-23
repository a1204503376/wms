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
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;

/**
 * 库内移动表头实体类
 *
 * @author pengwei
 * @since 2020-08-03
 */
@Data
@TableName("wms_transfer_header")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TransferHeader对象", description = "库内移动表头")
public class TransferHeader extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 库内移动表头ID
	 */
	@ApiModelProperty(value = "库内移动表头ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "transfer_bill_id", type = IdType.ASSIGN_ID)
	private Long transferBillId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库房编码
	 */
	@ApiModelProperty(value = "库房编码")
	private String whCode;
	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;
	/**
	 * 单据业型
	 */
	@ApiModelProperty(value = "单据业型")
	private Integer billType;
	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态")
	private Integer billState;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;


}
