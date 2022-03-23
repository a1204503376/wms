package org.nodes.modules.wms.log.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.vo.*;
import org.nodes.wms.core.outstock.loading.vo.TruckSerialVO;
import org.nodes.wms.core.log.system.entity.SystemProc;

import java.util.List;

/**
 * 系统日志视图实体类
 *
 * @author NodeX
 * @since 2020-02-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SystemProcVO对象", description = "系统日志")
public class SystemProcVO extends SystemProc {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;

	/**
	 * 序列号集合
	 */
	@ApiModelProperty("序列号集合")
	private List<SerialVO> serialList;

	/**
	 * 序列号日志集合
	 */
	@ApiModelProperty("序列号日志集合")
	private List<SerialLogVO> serialLogList;

	/**
	 * 批次号集合
	 */
	@ApiModelProperty("批次号集合")
	private List<LotVO> lotList;

	/**
	 * 批次号日志集合
	 */
	@ApiModelProperty("批次号日志集合")
	private List<LotLogVO> lotLogList;

	/**
	 * 库存占用集合
	 */
	@ApiModelProperty("库存占用集合")
	private List<StockOccupyVO> stockOccupyList;

	/**
	 * 库存日志集合
	 */
	@ApiModelProperty("库存日志集合")
	private List<StockLogVO> stockLogList;

	/**
	 * 装车发货序列号日志集合
	 */
	@ApiModelProperty("装车发货序列号日志集合")
	private List<TruckSerialVO> truckSerialList;

	/**
	 * 数据项目描述
	 */
	@ApiModelProperty("数据项目描述")
	private String dataItemDesc;
}
