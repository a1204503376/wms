
package org.nodes.wms.core.outstock.so.service;

import org.nodes.wms.core.outstock.so.entity.PickPlan;
import org.nodes.wms.core.outstock.so.vo.PickSkuVO;
import org.nodes.wms.core.outstock.so.vo.PickTaskSubmitVO;
import org.nodes.wms.core.outstock.so.vo.PickTaskVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 按箱拣货 服务类
 *
 * @author pengwei
 * @since 2020-02-10
 */
public interface IPickByBoxService extends BaseService<PickPlan> {


	/**
	 * 以任务ID为条件,并通过库位编码排序查询拣货计划第一条
	 *
	 * @return
	 */
	PickTaskVO getByTaskId(Map<String, String> param);

	List<PickPlan> getPickInfoByOrder(Map<String, String> param);
	/**
	 * 提交拣货信息
	 *
	 * @param pickTaskSubmitVO
	 * @return
	 */
	PickTaskVO submitPickInfo(PickTaskSubmitVO pickTaskSubmitVO);


	PickSkuVO getTotalScanQtyBySku(String wellenNo, String skuCode);
}
