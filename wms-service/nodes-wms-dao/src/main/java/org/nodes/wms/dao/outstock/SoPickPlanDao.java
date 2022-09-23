package org.nodes.wms.dao.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.outstock.soPickPlan.dto.intput.SoPickPlanPageQuery;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanPageResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.springblade.core.mp.base.BaseService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 拣货计划 Dao接口
 **/
public interface SoPickPlanDao extends BaseService<SoPickPlan> {

	/**
	 * 根据发货单id和发货单明细id查询拣货计划信息
	 *
	 * @param soBillId   查询参数
	 * @param soDetailId 查询参数
	 * @return 拣货计划
	 */
	List<SoPickPlanForDistributionResponse> getBySoBillIdAndSoDetailId(Long soBillId, Long soDetailId);

	/**
	 * 判断是否有分配中的计划
	 *
	 * @param soBillId 出库单id
	 * @return true：表示出库单存在有效的分配计划
	 */
	boolean hasEnablePickPlan(Long soBillId);

	/**
	 * 根据出库id查找所有有效的拣货计划
	 *
	 * @param soBillId 出库单id
	 * @return 拣货计划集合
	 */
	List<SoPickPlan> findBySoHeaderId(Long soBillId);

	/**
	 * 根据拣货计划ID修改拣货计划数量
	 *
	 * @param pickPlanId       拣货计划ID
	 * @param pickRealTotalQty 拣货总实际数量，含之前的拣货数量
	 */
	void updatePickRealQty(Long pickPlanId, BigDecimal pickRealTotalQty);

	/**
	 * 根据任务ID查询拣货计划
	 *
	 * @param taskId 任务ID
	 * @return 拣货计划
	 */
	List<SoPickPlan> getPickByTaskId(Long taskId);

	/**
	 * 更新拣货计划中的任务id
	 *
	 * @param soPickPlanList 拣货计划
	 * @param taskId         任务id
	 */
	void updateTask(List<SoPickPlan> soPickPlanList, Long taskId);

	/**
	 * 获取分配记录分页
	 *
	 * @param page                分页条件
	 * @param soPickPlanPageQuery 查询条件
	 * @return 分页对象
	 */
	Page<SoPickPlanPageResponse> getPage(IPage<Object> page, SoPickPlanPageQuery soPickPlanPageQuery);

	/**
	 * 根据库存id查询拣货计划
	 *
	 * @param stockIdList 库存id
	 * @param soBillId    发货单id
	 * @return 拣货计划
	 */
	List<SoPickPlan> getByStockIdsAndSoBillId(List<Long> stockIdList, Long soBillId);

	/**
	 * 根据分配记录id集合查找分配记录
	 *
	 * @param soPickPlanIdList 分配id集合
	 * @return List<SoPickPlan>
	 */
	List<SoPickPlan> getByPickPlanIds(List<Long> soPickPlanIdList);

	/**
	 * 根据拣货计划ID修改拣货计划
	 *
	 * @param pickPlanId   拣货计划ID
	 * @param stockId      库存ID
	 * @param location     库位
	 * @param zone         库区
	 * @param boxCode      箱码-可为空
	 * @param stockBalance 库存余额-可为空
	 */
	void updatePickByPartParam(Long pickPlanId, Long stockId, Location location, Zone zone, String boxCode, BigDecimal stockBalance);

	/**
	 * 根据任务和库存ID查询对应的拣货计划
	 *
	 * @param taskId  任务ID
	 * @param stockId 库存ID
	 * @return 对应的拣货计划
	 */
	List<SoPickPlan> getPickByTaskIdAndStockId(Long taskId, Long stockId);


	/**
	 * 根据任务Id和库存ID修改拣货计划
	 *
	 * @param taskId        任务ID
	 * @param sourceStockId 原库存ID
	 * @param targetStockId 目标库存ID
	 * @param location      库位
	 * @param zone          库区
	 */
	void updatePickByTaskIdAndStockId(Long taskId, Long sourceStockId, Long targetStockId, Location location, Zone zone);

	/**
	 * 根据发货单详情ID查询对应的拣货计划
	 *
	 * @param soDetailId 发货单详情ID
	 * @return 拣货计划
	 */
	List<SoPickPlan> selectPickBySoDetailId(Long soDetailId);

	/**
	 * 根据发货单ID和箱码查询拣货计划
	 *
	 * @param soBillId 发货单ID
	 * @param boxCode  箱码
	 * @return 拣货计划
	 */
	List<SoPickPlan> selectSoPickPlanByBoxCode(Long soBillId, String boxCode);

	/**
	 * 根据拣货计划ID查询拣货计划
	 *
	 * @param pickPlanId 拣货计划ID
	 * @return 拣货计划
	 */
	SoPickPlan selectSoPickPlanById(Long pickPlanId);
}
