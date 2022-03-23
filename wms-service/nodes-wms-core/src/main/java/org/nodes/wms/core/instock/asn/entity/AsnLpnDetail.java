
package org.nodes.wms.core.instock.asn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;

/**
 * 入库容器明细 实体类
 *
 * @author zhonglianshuai
 * @since 2020-02-14
 */
@Data
@TableName("asn_lpn_detail")
@ApiModel(value = "AsnLpnDetail对象", description = "AsnLpnDetail对象")
public class AsnLpnDetail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 容器明细id
	*/
		@TableId(type = IdType.ASSIGN_ID)
		@JsonSerialize(using = ToStringSerializer.class)
		@ApiModelProperty(value = "容器明细id")
		private Long asnLpnId;
	/**
	* 订单id
	*/
	    @JsonSerialize(using = ToStringSerializer.class)
		@ApiModelProperty(value = "订单id")
		private Long asnBillId;
	/**
	* 单据编码
	*/
		@ApiModelProperty(value = "单据编码")
		private String asnBillNo;
	/**
	* 订单明细id
	*/
	    @JsonSerialize(using = ToStringSerializer.class)
		@ApiModelProperty(value = "订单明细id")
		private Long asnDetailId;
	/**
	* 序列号
	*/
		@ApiModelProperty(value = "序列号")
		private String snDetailCode;
	/**
	* 容器编码
	*/
		@ApiModelProperty(value = "容器编码")
		private String lpnCode;
	/**
	* 数量
	*/
		@ApiModelProperty(value = "数量")
		private Long lpnQty;
	/**
	* 物品ID
	*/
		@ApiModelProperty(value = "物品ID")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long skuId;
	/**
	* 物品名称
	*/
		@ApiModelProperty(value = "物品名称")
		private String skuName;
	/**
	* 包装ID
	*/
		@ApiModelProperty(value = "包装ID")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long wspId;
	/**
	* 包装层级
	*/
		@ApiModelProperty(value = "包装层级")
		private Integer skuLevel;
	/**
	* 计量单位编码
	*/
		@ApiModelProperty(value = "计量单位编码")
		private String umCode;
	/**
	* 计量单位名称
	*/
		@ApiModelProperty(value = "计量单位名称")
		private String umName;
	/**
	* 规格
	*/
		@ApiModelProperty(value = "规格")
		private String skuSpec;
	/**
	* 容器状态
	*/
		@ApiModelProperty(value = "容器状态")
		private String lpnStatus;


}
