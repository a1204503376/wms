package org.nodes.wms.dao.outstock.so.dto.input;

import lombok.Data;
import org.nodes.wms.dao.outstock.so.dto.output.StockIdAndSoPickPlanQtyRequest;

import java.io.Serializable;
import java.util.List;

/**
 * 发货单分配信息请求类
 *
 * @author nodesc
 **/
@Data
public class SoBillDistributedRequest implements Serializable {

	private static final long serialVersionUID = 3597348777478468170L;

	/**
	 * 发货单id
	 */
	private Long soBillId;

	/**
	 * 发货单明细id
	 */
	private Long soDetailId;

	/**
	 * 原拣货计划id
	 */
	private List<Long> oldSoPickPlanList;

	/**
	 * 库存id和分配数量集合
	 */
	private List<StockIdAndSoPickPlanQtyRequest> stockIdAndSoPickPlanQtyList;
}
