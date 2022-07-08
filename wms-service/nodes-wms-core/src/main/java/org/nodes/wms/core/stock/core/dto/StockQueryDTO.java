package org.nodes.wms.core.stock.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;

import java.util.List;

/**
 * @program: WmsCore
 * @description: 库存查询数据传输对象
 * @author: pengwei
 * @create: 2020-11-13 14:21
 **/
@Data
public class StockQueryDTO extends SkuLotBaseEntity {

	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	private Long whId;
	/**
	 *货位编码
	 */
	@ApiModelProperty("货位编码")
	private String locCode;
	/**
	 *容器编码
	 */
	@ApiModelProperty("容器编码")
	private String lpnCode;
	/**
	 *物品ID
	 */
	@ApiModelProperty("物品ID")
	private Long skuId;
	/**
	 *批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;
	/**
	 *序列号集合
	 */
	@ApiModelProperty("序列号集合")
	private List<String> serialNumberList;
}
