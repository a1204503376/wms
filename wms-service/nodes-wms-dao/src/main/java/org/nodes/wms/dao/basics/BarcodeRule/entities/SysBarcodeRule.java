package org.nodes.wms.dao.basics.BarcodeRule.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;

/**
 * 条码规则实体类
 *
 * @author liangmei
 * @since 2019-12-16
 */
@Data
@TableName("sys_barcode_rule")
public class SysBarcodeRule extends TenantEntity implements Serializable {


	private static final long serialVersionUID = 8356625449790405800L;
	/**
	 * 条码规则定义ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "sbr_id", type = IdType.ASSIGN_ID)
	private Long sbrId;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 规则
	 */
	private String barcodeRule;
	/**
	 * 条码类型
	 */
	private Integer barcodeType;

}
