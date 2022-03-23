package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * PDA用物品移动提交VO
 *
 * @Author zx
 * @Date 2020/3/10
 **/
@Data
public class StockSkuMoveSubmitVO {

	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 数量
	 */
	@ApiModelProperty("数量")
	@Range(min = 1L, message = "数量必须大于 0")
	private BigDecimal moveQty;
	/**
	 * 库房id
	 */
	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 仓库编码
	 */
	@ApiModelProperty(value = "仓库编码")
	private String whCode;

	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@NotNull(message = "参数：skuId 不能为空！")
	private Long skuId;

	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	private String skuCode;

	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;
	/**
	 * 目标容器编码
	 */
	@ApiModelProperty(value = "拣货容器编码: 目标容器编码")
	private String targetLpnCode;

	/**
	 * 目标箱号编码
	 */
	@ApiModelProperty(value = "目标箱号编码")
	private String targetBoxCode;
	/**
	 * 原箱号编码
	 */
	@ApiModelProperty(value = "原箱号编码")
	private String sourceBoxCode;

	/**
	 * 落放位置:目标库位
	 */
	@ApiModelProperty(value = "落放位置:目标库位")
	private String targetLocCode;
	/**
	 * 源容器编码
	 */
	@ApiModelProperty(value = "源容器编码")
	private String sourceLpnCode;
	/**
	 * 源库位编码
	 */
	@ApiModelProperty(value = "源库位编码")
	@NotNull(message = "参数：sourceLocCode 不能为空！")
	private String sourceLocCode;

	/**
	 * 包装明细id
	 */
	@ApiModelProperty(value = "包装明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspdId;

	/**
	 * 是否为序列号管理
	 */
	@ApiModelProperty(value = "是否为序列号管理")
	private Integer isSn;

	/**
	 * 序列号列表
	 */
	@ApiModelProperty(value = "序列号列表")
	private List<String> serialList;

	/**
	 * 库位id
	 */
	@ApiModelProperty(value = "库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 货主ID
	 */
	@ApiModelProperty(value = "货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;


}
