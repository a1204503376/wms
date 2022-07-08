package org.nodes.wms.core.stock.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * 库存操作参数基类
 *
 * @author pengwei
 * @since 2019-12-27
 */
@Data
public class StockProcDTO extends SkuLotBaseEntity {

	/**
	 * 系统日志ID
	 */
	@ApiModelProperty("系统日志ID")
	private Long SystemProcId;
	/**
	 * 事务类型
	 */
	@NotNull(message = "未指定事务类型 eventType")
	@ApiModelProperty("事务类型")
	private EventTypeEnum eventType;
}
