package org.nodes.wms.core.stock.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.core.tool.utils.StringPool;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存增加（按件） 参数
 *
 * @author pengwei
 * @since 2019-12-26
 */
@Data
public class StockAddDTO extends StockProcDTO {

	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	private Long stockId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	private Long whId;

	@ApiModelProperty("库存明细ID")
	private Long sdId;
	/**
	 * 库位ID，库存ID为空时必填
	 */
	@ApiModelProperty("库位ID，库存ID为空时必填")
	private Long locId;
	/**
	 * 物品ID
	 */
	@NotNull(message = "参数: skuId 不能为空")
	@ApiModelProperty("物品ID")
	private Long skuId;

	/**
	 * 包装ID
	 */
	@NotNull(message = "参数: wspId 不能为空")
	@ApiModelProperty("包装ID")
	private Long wspId;
	/**
	 * 库存量
	 */
	@ApiModelProperty("库存量")
	@Min(message = "参数 stockQty 的值必须大于零", value = 1)
	@NotNull(message = "参数: stockQty 不能为空")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;
	/**
	 * 容器编码
	 */
	@ApiModelProperty("容器编码")
	private String lpnCode = StringPool.EMPTY;
	/**
	 * 箱号
	 */
	@ApiModelProperty("箱号")
	private String boxCode = StringPool.EMPTY;
	/**
	 * 单据ID
	 */
	@ApiModelProperty("单据ID")
	private Long billId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty("单据编码")
	private String billNo;
	/**
	 * 单据明细ID
	 */
	@ApiModelProperty("单据明细ID")
	private Long billDetailId;
	/**
	 * 波次id
	 */
	@ApiModelProperty("波次id")
	private Long wellenId;
	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;
	/**
	 * 数据ID(事务类型=出库：拣货计划ID；事务类型=盘点：差异记录ID；)
	 */
	@ApiModelProperty("数据ID(事务类型=出库：拣货计划ID；事务类型=盘点：差异报告ID；)")
	private Long dataId;
	/**
	 * 序列号集合
	 */
	@ApiModelProperty("序列号集合")
	private List<String> serialList = new ArrayList<>();
	/**
	 * 有效截至日期
	 */
	@ApiModelProperty("有效截至日期")
	private String validTime;
}
