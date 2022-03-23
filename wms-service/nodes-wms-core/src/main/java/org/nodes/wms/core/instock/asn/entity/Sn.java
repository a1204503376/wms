package org.nodes.wms.core.instock.asn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;

/**
 * 入库货品序列号实体类
 *
 * @author NodeX
 * @since 2020-01-15
 */
@Data
@TableName("asn_sn")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Sn对象", description = "入库货品序列号")
public class Sn extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 序列号id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "序列号id")
	private Long snDetailId;
	/**
	 * 序列号
	 */
	@ApiModelProperty(value = "序列号")
	private String snDetailCode;
	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	private Long asnBillId;
	/**
	 * 订单明细id
	 */
	@ApiModelProperty(value = "订单明细id")
	private Long asnDetailId;
	/**
	 * 序列号状态
	 */
	@ApiModelProperty(value = "序列号状态")
	private String snStatus;

	/**
	 * 箱号
	 */
	@ApiModelProperty(value = "箱号")
	private String boxCode;

	/**
	 * 序号
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "分组编号")
	private String groupNum;
}
