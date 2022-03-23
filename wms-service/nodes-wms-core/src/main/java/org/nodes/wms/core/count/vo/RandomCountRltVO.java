package org.nodes.wms.core.count.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.stock.core.entity.Stock;

import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 随机盘点返回对象
 * @create 20200227
 */
@Data
public class RandomCountRltVO {

	/**
	 * 库存集合
	 */
	@ApiModelProperty("库存集合")
	private List<Stock> stockList;
}
