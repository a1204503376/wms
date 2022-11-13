package org.nodes.wms.biz.outstock.plan;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.dto.intput.SoPickPlanPageQuery;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanPageResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 拣货计划相关业务
 *
 * @author nodes
 */
public interface SoPickPlanBiz {

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
	 * 运行分配策略
	 *
	 * @param soHeader       发货单信息
	 * @param soDetials      发货单未发明细
	 * @param existPickPlans 已经存在的拣货计划
	 * @return 运行的信息，如果全部分配成功则返回分配成功
	 */
	String runPickStrategy(SoHeader soHeader, List<SoDetail> soDetials, List<SoPickPlan> existPickPlans);

	/**
	 * 根据拣货计划ID修改拣货计划数量
	 *
	 * @param pickPlanId  拣货计划ID
	 * @param pickRealQty 拣货实际数量
	 */
	void updatePickRealQty(Long pickPlanId, BigDecimal pickRealQty);

	/**
	 * 根据拣货计划拣货，
	 * 该函数会执行如下操作：1. 释放库存占用 2.移动库存到出库暂存区 3.更新拣货计划 4.生产并保存拣货记录
	 *
	 * @param soDetail     必填，出库单明细
	 * @param pickPlan     必填，拣货计划
	 * @param pickQty      必填，本次拣货数量
	 * @param serialNoList 非必填，序列号集合
	 * @return 拣货记录
	 */
	LogSoPick pickByPlan(SoDetail soDetail, SoPickPlan pickPlan,
						 BigDecimal pickQty, List<String> serialNoList);

	/**
	 * 根据任务ID查询跟当前任务相关联的拣货计划
	 *
	 * @param taskId 任务ID
	 * @return 拣货计划
	 */
	List<SoPickPlan> findPickByTaskId(Long taskId);

	/**
	 * 取消分配
	 *
	 * @param soPickPlanList 需要取消分配的拣货计划
	 * @param soHeader       发货单
	 */
	void cancelPickPlan(List<SoPickPlan> soPickPlanList, SoHeader soHeader);

	/**
	 * 取消分配
	 *
	 * @param soPickPlanIdList 需要取消分配的拣货计划
	 * @param soHeader         发货单
	 */
	void cancelPickPlan(SoHeader soHeader, List<Long> soPickPlanIdList);

	/**
	 * 根据工作任务取消拣货计划
	 *
	 * @param task 任务id
	 */
	void cancelPickPlan(WmsTask task);

	/**
	 * 关闭单据的时候取消未执行完毕的分配记录
	 *
	 * @param soHeader 发货单
	 */
	void cancelPickPlanByClose(SoHeader soHeader);

	/**
	 * 根据分配计划占用库存并保存分配计划
	 *
	 * @param soPickPlanList 分配计划
	 */
	void occupyStockAndSavePlan(List<SoPickPlan> soPickPlanList);

	/**
	 * 获取分配记录分页
	 *
	 * @param query               分页条件
	 * @param soPickPlanPageQuery 查询条件
	 * @return 分页对象
	 */
	Page<SoPickPlanPageResponse> page(Query query, SoPickPlanPageQuery soPickPlanPageQuery);

	/**
	 * 导出分配记录
	 *
	 * @param soPickPlanPageQuery 查询条件
	 * @param response            响应对象
	 */
	void export(SoPickPlanPageQuery soPickPlanPageQuery, HttpServletResponse response);

	/**
	 * 根据库存id和发货单id查询拣货计划
	 *
	 * @param stockIdList 库存id
	 * @param soBillId    发货单id
	 * @return 拣货计划
	 */
	List<SoPickPlan> findByStockIdsAndSoBillId(List<Long> stockIdList, Long soBillId);

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
	 * 根据任务ID库存ID查询跟当前任务相关联的拣货计划
	 *
	 * @param taskId  任务ID
	 * @param stockId 库存ID
	 * @return 拣货计划
	 */
	List<SoPickPlan> findPickByTaskId(Long taskId, Long stockId);

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
	 * 根据拣货计划ID软删除拣货计划
	 *
	 * @param pickPlanId 拣货计划ID
	 */
	void deletePickByPickPlanId(Long pickPlanId);

	/**
	 * 根据发货单详情ID查询对应的拣货计划
	 *
	 * @param soDetailId 发货单详情ID
	 * @return 拣货计划
	 */
	List<SoPickPlan> findPickBySoDetailId(Long soDetailId);

	/**
	 * 根据发货单ID和箱码查询拣货计划
	 *
	 * @param soBillId 发货单ID
	 * @param boxCode  箱码
	 * @return 拣货计划
	 */
	List<SoPickPlan> findSoPickPlanByBoxCode(Long soBillId, String boxCode);

	/**
	 * 根据拣货计划ID查询拣货计划
	 *
	 * @param pickPlanId 拣货计划ID
	 * @return 拣货计划
	 */
	SoPickPlan findSoPickPlanById(Long pickPlanId);

	/**
	 * 根据箱码查询对应拣货计划
	 *
	 * @param boxCode 箱码
	 * @return 拣货计划
	 */
	List<SoPickPlan> findSoPickPlanByBoxCode(String boxCode);

	/**
	 * 更新拣货计划中的任务id
	 *
	 * @param soPickPlanList 拣货计划集合，必填
	 * @param taskId         任务id，必填
	 */
	void setTaskId(List<SoPickPlan> soPickPlanList, Long taskId);

	/**
	 * 更新拣货计划中原有的分配的库存
	 *
	 * @param soPickPlan 拣货计划，必填
	 * @param newStock   分配新的库存，必填
	 * @param oldStock   旧的库存，必填
	 */
	void updatePlanOfStock(SoPickPlan soPickPlan, Stock newStock, Stock oldStock);
}
