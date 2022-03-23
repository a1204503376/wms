
package org.nodes.wms.core.outstock.so.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.instock.asn.vo.LpnItemVo;
import org.nodes.wms.core.stock.core.vo.SerialVO;
import org.nodes.wms.core.outstock.so.dto.CreatePickPlanDTO;
import org.nodes.wms.core.outstock.so.dto.PickPlanDTO;
import org.nodes.wms.core.outstock.so.dto.SavePickPlanDTO;
import org.nodes.wms.core.outstock.so.entity.PickPlan;
import org.nodes.wms.core.outstock.so.vo.CreateDetailPickPlanVO;
import org.nodes.wms.core.outstock.so.vo.CreatePickPlanVO;
import org.nodes.wms.core.outstock.so.vo.PickTaskSubmitVO;
import org.nodes.wms.core.outstock.so.vo.PickTaskVO;
import org.springblade.core.mp.base.BaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 拣货计划表 服务类
 *
 * @author pengwei
 * @since 2020-02-10
 */
public interface IPickPlanService extends BaseService<PickPlan> {
	/**
	 * 回滚拣货计划
	 *
	 * @param billIdList 订单ID集合
	 * @return 是否成功
	 */
	boolean rollback(List<Long> billIdList);
	boolean lpnHasSerial(SerialVO serialVO);
	/**
	 * 按单分配 - 生成
	 *
	 * @param createPickPlanDTO 创建拣货计划参数
	 * @return 分配结果集合
	 */
	CreatePickPlanVO createPickPlan(CreatePickPlanDTO createPickPlanDTO);

	/**
	 * 按单分配 - 保存
	 *
	 * @param savePickPlanDTO 待保存的拣货计划信息
	 * @return 是否成功
	 */
	boolean savePickPlan(SavePickPlanDTO savePickPlanDTO);

	/**
	 * 获取按明细分配的信息
	 *
	 * @param billIdList 订单ID集合
	 * @return 可分配的明细结果
	 */
	List<CreateDetailPickPlanVO> getDetailPickPlan(List<Long> billIdList);
	PickTaskVO getByTaskId(Map<String, String> param);
	/**
	 * 提交拣货信息
	 *
	 * @param pickTaskSubmitVO
	 * @return
	 */
	PickTaskVO submitPickInfo(PickTaskSubmitVO pickTaskSubmitVO);

	PickTaskVO submitTrayPickInfo(PickTaskSubmitVO pickTaskSubmitVO);
	/**
	 * 关闭任务
	 *
	 * @param pickPlanDTO
	 * @return
	 */
	boolean closeTack(PickPlanDTO pickPlanDTO);
	/**
	 * 保存拣货日志
	 *
	 * @param pickPlanDTO
	 * @return
	 */
	boolean saveLogSoPick(PickPlanDTO pickPlanDTO);
	boolean moveStockSku(PickPlanDTO pickPlanDTO);


	IPage<PickPlan> page(IPage<PickPlan> page, PickPlanDTO pickPlan);

	PickTaskVO submitPickInfoWithOrder(PickTaskSubmitVO pickTaskSubmitVO);

    LpnItemVo getInfoByLpnCode(String lpnCode, String pickPlanId, BigDecimal qty);

	void cancelPick(String ids);
}
