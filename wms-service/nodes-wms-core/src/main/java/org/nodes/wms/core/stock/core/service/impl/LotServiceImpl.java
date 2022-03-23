
package org.nodes.wms.core.stock.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.stock.core.cache.LotCache;
import org.nodes.wms.core.stock.core.dto.LotDTO;
import org.nodes.wms.core.stock.core.entity.Lot;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.mapper.LotMapper;
import org.nodes.wms.core.stock.core.service.ILotLogService;
import org.nodes.wms.core.stock.core.service.ILotService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.wrapper.LotWrapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 批次号 服务实现类
 *
 * @author pengwei
 * @since 2019-12-25
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class LotServiceImpl<M extends LotMapper, T extends Lot>
	extends BaseServiceImpl<LotMapper, Lot>
	implements ILotService {

	@Autowired
	ILotLogService lotLogService;

	@Autowired
	ISystemProcService systemProcService;

	@Override
	public boolean save(Lot entity) {
		boolean result = super.save(entity);
		if (result && lotLogService.save(entity)) {
			//LotCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(Lot entity) {
		boolean result = super.updateById(entity);
		if (result && lotLogService.save(entity)) {
			//LotCache.saveOrUpdate(entity);
		}
		return result;
	}

	/**
	 * 处理批次信息（如果提供了批次号，验证在数据库中是否存在，如果不存在则创建；提供了批属性，根据批属性查询批次信息）
	 *
	 * @param lotDTO 查询条件
	 * @return 批次号
	 */
	@Override
	public synchronized Lot chkLotNumber(LotDTO lotDTO) {
		Lot rltLot = null;
		if (Func.isNotEmpty(lotDTO.getLotNumber())) {
			// 根据提供的 批次号 查询数据库
			rltLot = super.list().stream().filter(u -> {
				return u.getLotNumber().equals(lotDTO.getLotNumber()) && u.getSkuId().equals(lotDTO.getSkuId());
			}).findFirst().orElse(null);
			if (Func.isEmpty(rltLot)) {
				// 如果批次信息不存在，存储当前批次信息
				this.save(lotDTO);
				rltLot = lotDTO;
			} else {
				// 如果批次信息存在，验证批属性与提供的批属性是否一致
				this.validateSkuLot(LotWrapper.build().entityDTO(rltLot), lotDTO);
				if (!StockStatusEnum.NORMAL.getIndex().equals(rltLot.getLotStatus())) {
					throw new ServiceException("批次号[" + rltLot.getLotNumber() + "]已被冻结，请解冻后再操作！");
				}
			}
		} else {
			// 根据 批属性 查询批次信息
			QueryWrapper<Lot> lotWrapper = new QueryWrapper<>();
			lotWrapper.eq("wh_id", lotDTO.getWhId());
			lotWrapper.eq("sku_id", lotDTO.getSkuId());
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				lotWrapper.eq("sku_lot" + i, lotDTO.skuLotGet(i));
			}
			lotWrapper.last("limit 1");
			rltLot = super.getOne(lotWrapper);
			if (Func.isNotEmpty(rltLot)) {
				if (!StockStatusEnum.NORMAL.getIndex().equals(rltLot.getLotStatus())) {
					throw new ServiceException("批次号[" + rltLot.getLotNumber() + "]已被冻结，请解冻后再操作！");
				}
			} else if (Func.isEmpty(rltLot)) {
				// 如果批次信息不存在
				Lot lot = new Lot();
				lot.setLotNumber(LotCache.create());
				while (super.count(Condition.getQueryWrapper(lot)) > 0) {
					lot.setLotNumber(LotCache.create());
				}
				lotDTO.setLotNumber(lot.getLotNumber());
				// 存储该批次号
				this.save(lotDTO);
				rltLot = lotDTO;
				// 存储批次号日志
				lotLogService.save(lotDTO);
			}
		}

		return rltLot;
	}


	/**
	 * 对比批属性
	 *
	 * @param lot    数据表批次信息
	 * @param lotDTO 提供的批次信息
	 */
	private void validateSkuLot(LotDTO lot, LotDTO lotDTO) {
		String msg = "物品批次信息不正确（当前批属性%s：%s，原批属性%s：%s）！";

		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (!lotDTO.skuLotChk(i)) {
				continue;
			}
			if (lot.skuLotGet(i).equals(lotDTO.skuLotGet(i))) {
				continue;
			}
			throw new ServiceException(String.format(msg, i, lotDTO.skuLotGet(i), i, lot.skuLotGet(i)));
		}
	}


	/**
	 *批次冻结
	 */
	@Override
	public synchronized boolean lock(String lotNumber){
		List<String> lotNumbers = Func.toStrList(lotNumber);
		if (Func.isEmpty(lotNumbers)) {
			throw new ServiceException("批次号不能为空");
		}
		List<Lot> lots = super.list(Condition.getQueryWrapper(new Lot())
		.lambda()
		.in(Lot::getLotNumber,lotNumbers)
		);
		if (Func.isEmpty(lots)) {
			throw new ServiceException("批次号[" + lotNumber + "] 不存在! ");
		}
		// 插入系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.LOCK_LOT);
		systemProcParam.setDataType(DataTypeEnum.LotId);
		systemProcParam.setAction(ActionEnum.LOCK_LOT);
		systemProcParam.setBillNo(lotNumber);
		systemProcParam.setRemark(StockStatusEnum.LOCK_FILL.getName());
		systemProcService.create(systemProcParam);
		for(Lot lot:lots) {
			lot.setLotStatus(StockStatusEnum.LOCK_FILL.getIndex());
			super.updateById(lot);
		}
		return true;
	}

	/**
	 * 批次解冻
	 */
	@Override
	public synchronized boolean unlock(String lotNumber){
		List<String> lotNumbers = Func.toStrList(lotNumber);
		if(Func.isEmpty(lotNumbers)){
			throw new ServiceException("批次号不能为空");
		}
		List<Lot> lots = super.list(Condition.getQueryWrapper(new Lot())
			.lambda()
			.in(Lot::getLotNumber,lotNumbers)
		);
		if (Func.isEmpty(lots)) {
			throw new ServiceException("批次号[" + lotNumber + "] 不存在! ");
		}
		// 插入系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.UNLOCK_LOT);
		systemProcParam.setDataType(DataTypeEnum.LotId);
		systemProcParam.setAction(ActionEnum.UNLOCK_LOT);
		systemProcParam.setBillNo(lotNumber);
		systemProcParam.setRemark(StockStatusEnum.NORMAL.getName());
		systemProcService.create(systemProcParam);
		for (Lot lot : lots) {
			lot.setLotStatus(StockStatusEnum.NORMAL.getIndex());
			super.updateById(lot);
		}
		return true;
	}





}
