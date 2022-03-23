package org.nodes.wms.core.stock.core.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.springblade.core.tool.utils.Func;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * author: pengwei
 * date: 2021/10/14 14:32
 * description: 库存明细增加DTO
 */
@Data
public class StockDetailAddDTO {

	/**
	 * 库存id
	 */
	@NotNull
	private Long stockId;

	private Long whId;

	private Long id;
	/**
	 * 对应库存对象
	 */
	private Stock stock;
	/**
	 * 容器编码
	 */
	private String lpnCode = StringPool.EMPTY;
	/**
	 * 箱号
	 */
	private String boxCode = StringPool.EMPTY;
	/**
	 * 增加数量
	 */
	@NotNull
	@Min(value = 1)
	private BigDecimal stockQty;
	/**
	 * 订单明细id
	 */
	private Long billDetailId;
	/**
	 * 订单id
	 */
	private Long soBillId;
	/**
	 * 波次id
	 */
	private Long wellenId;
	/**
	 * 序列号集合
	 */
	private List<String> serialList;
	/**
	 * 系统日志id
	 */
	private Long systemProcId;

	public String getBoxCode() {
		return Func.isNull(boxCode) ? StringPool.EMPTY : boxCode;
	}

	public String getLpnCode() {
		return Func.isNull(lpnCode) ? StringPool.EMPTY : lpnCode;
	}
}
