package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.core.stock.core.entity.StockPack;

/**
 * 尾箱打包视图实体类
 *
 * @author pengwei
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockPackVO对象", description = "尾箱打包")
public class StockPackVO extends StockPack {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 库位编码
	 */
	@ApiModelProperty("库位编码")
	private String locCode;

	/**
	 * 打包状态
	 */
	@ApiModelProperty("打包状态")
	private String packStateDesc;

	/**
	 * 尾箱打包日志IDS
	 */
	@ApiModelProperty(value = "尾箱打包日志IDS")
	private String lspIds;

	/**
	 * 任务ID
	 */
	@ApiModelProperty(value = "任务ID")
	private String taskId;
	/**
	 * 系统日志ID
	 */
	@ApiModelProperty("系统日志ID")
	private Long systemProcId;

	/**
	 * 批属性
	 */
	@ApiModelProperty("批属性")
	private SkuLotBaseEntity skuLots;

	/**
	 * 尾箱打包日志ID
	 */
	@ApiModelProperty(value = "尾箱打包日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long lspId;
}
