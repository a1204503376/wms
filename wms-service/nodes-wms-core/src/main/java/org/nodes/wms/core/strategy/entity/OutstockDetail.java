
package org.nodes.wms.core.strategy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 分配策略明细 实体类
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Data
@TableName("st_outstock_detail")
@ApiModel(value = "OutstockDetail对象", description = "OutstockDetail对象")
public class OutstockDetail extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 分配策略明细ID
	 */
	@ApiModelProperty(value = "分配策略明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "ssod_id", type = IdType.ASSIGN_ID)
	private Long ssodId;
	/**
	 * 分配策略ID
	 */
	@ApiModelProperty(value = "分配策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssoId;
	/**
	 * 分配计算代码
	 */
	@ApiModelProperty(value = "分配计算代码")
	private String outstockFunction;
	/**
	 * 跨批次周转
	 */
	@ApiModelProperty("跨批次周转")
	private Integer acrossLot;
	/**
	 * 分配层级
	 */
	@ApiModelProperty("分配层级")
	private Integer skuLevel;
	/**
	 * 分配规则
	 */
	@ApiModelProperty("分配规则")
	private Integer allotRule;
	/**
	 * 周转方式1
	 */
	@ApiModelProperty("周转方式1")
	private Integer turnoverType1;
	/**
	 * 周转方式2
	 */
	@ApiModelProperty("周转方式2")
	private Integer turnoverType2;
	/**
	 * 周转方式3
	 */
	@ApiModelProperty("周转方式3")
	private Integer turnoverType3;
	/**
	 * 物品ABC分类
	 */
	@ApiModelProperty("物品ABC分类")
	private Integer abc;

	/**
	 * 超发标志
	 */
	@ApiModelProperty(value = "超发标志")
	private Integer overCountFlag;
	/**
	 * 处理顺序
	 */
	@ApiModelProperty(value = "处理顺序")
	private Integer ssodProcOrder;
	/**
	 * 是否启用
	 */
	@ApiModelProperty(value = "是否启用")
	private String isActive;
	/**
	 * 数据类型
	 */
	@ApiModelProperty(value = "数据类型")
	private String dataType;

}
