
package org.nodes.wms.core.outstock.so.entity;

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

/**
 * 波次划分表实体类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Data
@TableName("so_wellen")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Wellen对象", description = "波次划分表")
public class Wellen extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 波次ID
	 */
	@ApiModelProperty(value = "波次ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wellen_id", type = IdType.ASSIGN_ID)
	private Long wellenId;
	/**
	 * 波次编号
	 */
	@ApiModelProperty(value = "波次编号")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenNo;
	/**
	 * 10：波次创建，20：分配完成，30：开始执行，40：波次关闭
	 */
	@ApiModelProperty(value = "10：波次创建，20：分配完成，30：开始执行，40：波次关闭")
	private Integer wellenState;
	/**
	 * 波次划分字段1
	 */
	@ApiModelProperty(value = "波次划分字段1")
	private String wellenItem1;
	/**
	 * 波次划分字段2
	 */
	@ApiModelProperty(value = "波次划分字段2")
	private String wellenItem2;
	/**
	 * 波次划分字段3
	 */
	@ApiModelProperty(value = "波次划分字段3")
	private String wellenItem3;
	/**
	 * 波次划分字段4
	 */
	@ApiModelProperty(value = "波次划分字段4")
	private String wellenItem4;
	/**
	 * 波次划分字段5
	 */
	@ApiModelProperty(value = "波次划分字段5")
	private String wellenItem5;
	/**
	 * 波次划分字段6
	 */
	@ApiModelProperty(value = "波次划分字段6")
	private String wellenItem6;
	/**
	 * 波次划分字段7
	 */
	@ApiModelProperty(value = "波次划分字段7")
	private String wellenItem7;
	/**
	 * 波次划分字段8
	 */
	@ApiModelProperty(value = "波次划分字段8")
	private String wellenItem8;
	/**
	 * 波次划分字段9
	 */
	@ApiModelProperty(value = "波次划分字段9")
	private String wellenItem9;
	/**
	 * 波次划分字段10
	 */
	@ApiModelProperty(value = "波次划分字段10")
	private String wellenItem10;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 多单合并标识（Y：是    N：否）
	 */
	@ApiModelProperty("多单合并标识（Y：是    N：否）")
	private String billMultiple;
}
