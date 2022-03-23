package org.nodes.wms.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库位占用信息
 * @create 20200408
 */
@Data
public class LocOccupyRltVO {
	/**
	 * 这个月占用库位数量
	 */
	@ApiModelProperty("这个月占用库位数量")
	private Integer currOccupyCount;

	/**
	 * 上个月占用库位数量
	 */
	@ApiModelProperty("上个月占用库位数量")
	private Integer lastOccupyCount;

	/**
	 * 比率
	 */
	@ApiModelProperty("比率")
	private Integer rate;
}
