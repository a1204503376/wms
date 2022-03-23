
package org.nodes.wms.core.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

/**
 * 条码规则实体类
 *
 * @author liangmei
 * @since 2019-12-16
 */
@Data
@TableName("sys_barcode_rule")
@ApiModel(value = "BarcodeRule对象", description = "BarcodeRule对象")
public class BarcodeRule extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	* 条码规则定义ID
	*/
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "条码规则定义ID")
	@TableId(value = "sbr_id", type = IdType.ASSIGN_ID)
		private Long sbrId;
	/**
	* 库房ID
	*/
	@JsonSerialize(using = ToStringSerializer.class)
		@ApiModelProperty(value = "库房ID")
		private Long whId;
	/**
	* 规则
	*/
		@ApiModelProperty(value = "规则")
		private String barcodeRule;
	/**
	* 条码类型
	*/
		@ApiModelProperty(value = "条码类型")
		private Integer barcodeType;

}
