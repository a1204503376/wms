package org.nodes.wms.core.count.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 随机盘点参数
 * @create 20200227
 */
@Data
public class RandomCountDTO extends SkuLotBaseEntity {

	/**
	 * 盘点单编号
	 */
	@ApiModelProperty("盘点单编号")
	private String countBillNo;
	/**
	 * 任务ID
	 */
	@ApiModelProperty("任务ID")
	private Long taskId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	private Long whId;
	/**
	 * 容器编码
	 */
	@ApiModelProperty("容器编码")
	private String lpnCode;
	/**
	 * 货位编码
	 */
	@ApiModelProperty("货位编码")
	@NotNull(message = "库位编码不能为空！")
	private String locCode;
	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@NotNull(message = "参数: skuId 不能为空")
	private Long skuId;
	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;
	/**
	 * 盘点数量
	 */
	@ApiModelProperty("盘点数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal countQty;
	/**
	 * 包装明细ID
	 */
	@ApiModelProperty("包装明细ID")
	private Long wspdId;
	/**
	 * 序列号集合
	 */
	@ApiModelProperty("序列号集合")
	private List<String> serialNumberList;
}
