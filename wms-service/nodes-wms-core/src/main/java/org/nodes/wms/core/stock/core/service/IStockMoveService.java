package org.nodes.wms.core.stock.core.service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.wms.core.stock.core.dto.StockProcDTO;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.springblade.core.tool.utils.SpringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存移动接口
 */
public interface IStockMoveService<T extends IStockMoveService.MoveParam>{
	IStockService stockService = SpringUtil.getBean(IStockService.class);
	ISerialService serialService = SpringUtil.getBean(ISerialService.class);
	IStockLogService stockLogService = SpringUtil.getBean(IStockLogService.class);
	List<Stock> move(T moveParam);
	@Data
	class MoveParam extends StockProcDTO {
		/**
		 * 原库存ID
		 */
		@ApiModelProperty("原库存ID")
		private Long sourceStockId;
		/**
		 * 原库位ID
		 */
		@ApiModelProperty("原库位ID")
		private Long sourceLocId;
		/**
		 * 原容器编码
		 */
		@ApiModelProperty("原容器编码")
		private String sourceLpnCode;
		/**
		 * 原箱号
		 */
		@ApiModelProperty("原箱号")
		private String sourceBoxCode;
		/**
		 * 目标库存ID
		 */
		@ApiModelProperty("目标库存ID")
		private Long targetStockId;
		/**
		 * 目标库位ID
		 */
		@ApiModelProperty("目标库位ID")
		private Long targetLocId;
		/**
		 * 目标容器编码
		 */
		@ApiModelProperty("目标容器编码")
		private String targetLpnCode;
		/**
		 * 目标箱号
		 */
		@ApiModelProperty("目标箱号")
		private String targetBoxCode;
		/**
		 * 物品ID
		 */
		@ApiModelProperty("物品ID")
		private Long skuId;
		/**
		 * 批次号
		 */
		@ApiModelProperty("批次号")
		private String lottNumber;
		/**
		 * 移库数量
		 */
		@ApiModelProperty("移库数量")
		@JsonSerialize(using = BigDecimalSerializer.class)
		private BigDecimal moveQty;

		/**
		 * 序列号集合
		 */
		@ApiModelProperty("序列号集合")
		private List<String> serialList = new ArrayList<>();
		/**
		 * 数据ID(事务类型=出库：拣货计划ID；事务类型=盘点：差异记录ID；)
		 */
		@ApiModelProperty("数据ID(事务类型=出库：拣货计划ID；事务类型=盘点：差异记录ID；)")
		private Long dataId;

		/**
		 * 发货单ID
		 */
		@ApiModelProperty("发货单ID")
		private Long soBillId;
	}
}
