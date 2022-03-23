package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.relenishment.vo.RelDetailVo;

import java.util.List;

/**
 * 补货返回VO
 * @Author zx
 * @Date 2020/8/4
 **/
@Data
public class StockReplenishmentVO {

	@ApiModelProperty("任务完成数")
	private Integer finish;

	@ApiModelProperty("任务总数")
	private Integer total;

	@ApiModelProperty("补货明细列表")
	private List<RelDetailVo> detailList;

}
