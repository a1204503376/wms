package org.nodes.wms.core.stock.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * author: pengwei
 * date: 2021/4/16 11:39
 * description: StockSubtractDTO
 */
@Data
public class StockSubtractDTO extends StockProcDTO {

	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	private Long stockId;

	/**
	 * 货位ID
	 */
	@ApiModelProperty("货位ID")
	private Long locId;

	/**
	 * 库位编码
	 */
	@ApiModelProperty("库位编码")
	private String locCode;

	/**
	 * 容器编码
	 */
	@ApiModelProperty("容器编码")
	private String lpnCode;

	/**
	 * 箱号
	 */
	@ApiModelProperty("箱号")
	private String boxCode;

	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	private Long skuId;

	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;

	/**
	 * 单据ID
	 */
	@ApiModelProperty("单据ID")
	private Long billId;

	/**
	 * 单据编号
	 */
	@ApiModelProperty("单据编号")
	private String billNo;

	/**
	 * 单据明细ID
	 */
	@ApiModelProperty("单据明细ID")
	private Long billDetailId;

	/**
	 * 下架数量
	 */
	@ApiModelProperty("下架数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@NotNull(message = "参数: pickQty 不能为空")
	@Min(value = 1, message = "参数: pickQty 必须大于零")
	private BigDecimal pickQty;

	/**
	 * 数据ID(事务类型=出库：拣货计划ID；事务类型=盘点：差异记录ID；)
	 */
	@ApiModelProperty("数据ID(事务类型=出库：拣货计划ID；事务类型=盘点：差异报告ID；)")
	private Long dataId;

	/**
	 * dataId 为拣货计划ID时，需要提供波次ID
	 */
	private Long wellenId;

	/**
	 * 序列号集合
	 */
	@ApiModelProperty("序列号集合")
	private List<String> serialList = new ArrayList<>();

	/**
	 * 输出不为空的字段(库存ID，库位ID，容器编码，箱号，批次号)
	 *
	 * @return 不为空的字段描述
	 */
	public String toSimpleString() {
		List<String> msgList = new ArrayList<>();
		if (Func.isNotEmpty(this.stockId)) {
			msgList.add("库存ID：" + this.stockId);
		}
		if (Func.isNotEmpty(this.locCode)) {
			msgList.add("库位编码：" + this.locCode);
		} else if (Func.isNotEmpty(this.locId)) {
			msgList.add("库位ID：" + this.locId);
		}
		if (Func.isNotEmpty(this.lpnCode)) {
			msgList.add("容器编码：" + this.lpnCode);
		}
		if (Func.isNotEmpty(this.boxCode)) {
			msgList.add("箱号：" + this.boxCode);
		}
		if (Func.isNotEmpty(this.lotNumber)) {
			msgList.add("批次号：" + this.lotNumber);
		}
		return StringUtil.join(msgList);
	}
}
