
package org.nodes.wms.core.outstock.so.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.stock.core.vo.StockVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成拣货计划VO对象实体类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Data
public class CreatePickPlanVO {
	private static final long serialVersionUID = 1L;

	/**
	 * 正常分配 - 拣货计划列表
	 */
	private List<CreatePickPlanDetailVO> pickPlanDetailList = new ArrayList<>();
	/**
	 * 异常分配 - 缺货列表
	 */
	private List<CreatePickPlanDetailVO> pickPlanDetailErrorList = new ArrayList<>();
	/**
	 * 其他库存, 分配策略外的库存信息
	 */
	private List<StockVO> otherStockList = new ArrayList();

	/**
	 * 是否提示
	 */
	@ApiModelProperty("是否提示")
	private boolean prompt;

	/**
	 * 是否允许编辑
	 */
	@ApiModelProperty("是否允许编辑")
	private boolean allowEdit;
}
