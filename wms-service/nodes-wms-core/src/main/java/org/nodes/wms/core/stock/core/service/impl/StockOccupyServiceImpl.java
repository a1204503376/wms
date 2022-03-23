
package org.nodes.wms.core.stock.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.stock.core.dto.StockOccupyDTO;
import org.nodes.wms.core.stock.core.dto.StockOccupySubtractDTO;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockProcTypeEnum;
import org.nodes.wms.core.stock.core.mapper.StockOccupyMapper;
import org.nodes.wms.core.stock.core.service.IStockLogService;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 库存占用表 服务实现类
 *
 * @author pengwei
 * @since 2020-02-17
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StockOccupyServiceImpl<M extends StockOccupyMapper, T extends StockOccupy>
	extends BaseServiceImpl<StockOccupyMapper, StockOccupy>
	implements IStockOccupyService {

	/**
	 * 根据库存占用信息
	 * @param stockId 库存ID
	 * @param pickPlanId 拣货计划ID
	 * @param wcrId 盘点记录ID
	 * @return 库存占用集合
	 */
	@Override
	public List<StockOccupy> list(Long stockId, Long pickPlanId, Long wcrId) {
		StockOccupy stockOccupyQuery = new StockOccupy();
		if (ObjectUtil.isNotEmpty(stockId)) {
			stockOccupyQuery.setStockId(stockId);
		}
		if (ObjectUtil.isNotEmpty(pickPlanId)) {
			stockOccupyQuery.setPickPlanId(pickPlanId);
		}
		if (ObjectUtil.isNotEmpty(wcrId)) {
			stockOccupyQuery.setWcrId(wcrId);
		}
		return super.list(Condition.getQueryWrapper(stockOccupyQuery));
	}

	@Override
	public void updateOccupyQty(StockOccupy stockOccupy) {
		if (BigDecimalUtil.le(stockOccupy.getOccupyQty(), BigDecimal.ZERO)) {
			super.removeById(stockOccupy.getWsoId());
		} else {
			UpdateWrapper<StockOccupy> updateWrapper = new UpdateWrapper<>();
			updateWrapper.lambda()
				.set(StockOccupy::getOccupyQty, stockOccupy.getOccupyQty())
				.set(StockOccupy::getUpdateTime, LocalDateTime.now())
				.eq(StockOccupy::getWsoId, stockOccupy.getWsoId());
			super.update(updateWrapper);
		}
	}

	@Override
	public void add(@NotNull StockOccupyDTO stockOccupy) {
		if (Func.isEmpty(stockOccupy)) {
			throw new ServiceException("参数：stockOccupy 不能为空！");
		}
		IStockService stockService = SpringUtil.getBean(IStockService.class);
		Stock stock = stockService.getById(stockOccupy.getStockId());
		if (ObjectUtil.isEmpty(stock)) {
			throw new ServiceException("指定库存不存在（库存ID：" + stockOccupy.getStockId() + "）！");
		}
		// 验证库存占用量(避免[占用量]超出[库存可用量])
		BigDecimal stockQty = stock.getStockQty().subtract(stock.getPickQty());
		BigDecimal occupyQty = stock.getOccupyQty();
		if (BigDecimalUtil.gt(occupyQty, stockQty)) {
			throw new ServiceException("指定库存[占用量]超出[库存可用量]！");
		}
		// 创建库存占用
		super.save(stockOccupy);
		// 修改占用数量
		if (stockOccupy.getOccupyType().equals(StockOccupyTypeEnum.PickPlan.getIndex()) ||
			stockOccupy.getOccupyType().equals(StockOccupyTypeEnum.StockPack.getIndex())||
			stockOccupy.getOccupyType().equals(StockOccupyTypeEnum.Replenish.getIndex()) ||
			stockOccupy.getOccupyType().equals(StockOccupyTypeEnum.Count.getIndex())
		) {
			stock.setOccupyQty(stock.getOccupyQty().add(stockOccupy.getOccupyQty()));
		}
		// 记录库存日志
		IStockLogService stockLogService = SpringUtil.getBean(IStockLogService.class);
		stockLogService.create(stock, null, StockProcTypeEnum.Update, stockOccupy.getSystemProcId());

		stockService.updateById(stock);
	}

	@Override
	public List<StockOccupy> subtract(@NotNull StockOccupySubtractDTO stockOccupyReduce) {

		IStockService stockService = SpringUtil.getBean(IStockService.class);
		IStockLogService stockLogService = SpringUtil.getBean(IStockLogService.class);

		List<StockOccupy> stockOccupyList = null;
		if (Func.isEmpty(stockOccupyReduce)) {
			throw new ServiceException("参数：stockOccupyReduceDTO 不能为空！");
		}
		StockOccupy stockOccupyQuery = BeanUtil.copy(stockOccupyReduce, StockOccupy.class);
		if (Func.isEmpty(stockOccupyQuery)) {
			throw new ServiceException("封装库存占用扣减参数发生异常！");
		}
		stockOccupyList = super.list(Condition.getQueryWrapper(stockOccupyQuery));
		if (Func.isNotEmpty(stockOccupyList)) {
			for (StockOccupy stockOccupy : stockOccupyList) {
				Stock stock = stockService.getById(stockOccupy.getStockId());
				if (Func.isEmpty(stock)) {
					throw new ServiceException("指定库存不存在（库存ID：" + stockOccupy.getStockId() + "）！");
				}
				if (Func.isEmpty(stockOccupyReduce.getQty()) ||
					stockOccupyReduce.getQty().compareTo(BigDecimal.ZERO) <= 0 ||
					stockOccupy.getOccupyQty().compareTo(stockOccupyReduce.getQty()) < 0 ||
					stockOccupyReduce.getQty().equals(stockOccupy.getOccupyQty())) {
					// 删除库存占用
					super.removeById(stockOccupy);
				} else {
					// 更新库存占用数量
					stockOccupy.setOccupyQty(stockOccupy.getOccupyQty().subtract(stockOccupyReduce.getQty()));
					super.updateById(stockOccupy);
				}
				// 修改库存表占用数量
				if (stockOccupyReduce.getOccupyType() == StockOccupyTypeEnum.PickPlan.getIndex() ||
					stockOccupyReduce.getOccupyType() == StockOccupyTypeEnum.StockPack.getIndex()||
					stockOccupyReduce.getOccupyType() == StockOccupyTypeEnum.Replenish.getIndex() ||
					stockOccupyReduce.getOccupyType() == StockOccupyTypeEnum.Count.getIndex()) {
					// 拣货占用
					if (stock.getOccupyQty().subtract(stockOccupy.getOccupyQty()).compareTo(BigDecimal.ZERO) < 0) {
						stock.setOccupyQty(BigDecimal.ZERO);
					} else {
						stock.setOccupyQty(stock.getOccupyQty().subtract(stockOccupy.getOccupyQty()));
					}
				}
				// 记录库存日志
				stockLogService.create(stock, null, StockProcTypeEnum.Update, stockOccupy.getSystemProcId());

				stockService.updateById(stock);
			}
		}
		return stockOccupyList;
	}
}
