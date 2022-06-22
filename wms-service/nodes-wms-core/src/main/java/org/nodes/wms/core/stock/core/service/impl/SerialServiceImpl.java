package org.nodes.wms.core.stock.core.service.impl;

import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.core.stock.core.entity.SerialLog;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.enums.SerialLogProcTypeEnum;
import org.nodes.wms.core.stock.core.mapper.SerialMapper;
import org.nodes.wms.core.stock.core.service.ISerialLogService;
import org.nodes.wms.core.stock.core.service.ISerialService;
import org.nodes.wms.core.stock.core.wrapper.SerialLogWrapper;
import org.nodes.wms.core.stock.core.wrapper.SerialWrapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 序列号 服务实现类
 *
 * @author zx
 * @since 2020-02-21
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SerialServiceImpl<M extends SerialMapper, T extends Serial>
	extends BaseServiceImpl<SerialMapper, Serial>
	implements ISerialService {

	@Autowired
	ISerialLogService serialLogService;

	@Override
	public List<Serial> saveBatch(List<String> serialNumberList, Stock stock, StockDetail stockDetail,
								  Long systemProcId) {
		// 查询出已存在的序列号
		List<Serial> serialList = new ArrayList<>();
		if (Func.isNotEmpty(serialNumberList)) {
			serialList = super.list(Condition.getQueryWrapper(new Serial()).lambda()
				.in(Serial::getSerialNumber, serialNumberList));
		}
		List<Serial> serial_list = new ArrayList<>();
		List<SerialLog> serial_log_list = new ArrayList<>();
		for (String serialNumber : serialNumberList) {
			Serial serial = serialList.stream().filter(u -> Func.equals(u.getSerialNumber(), serialNumber))
				.findFirst().orElse(null);
			if (Func.isNotEmpty(serial)) {
				// 获取库存物品信息
				Sku sku = SkuCache.getById(stock.getSkuId());
				if (Func.isNotEmpty(sku)) {
					serial.setSkuCode(sku.getSkuCode());
					serial.setSkuName(sku.getSkuName());
					// 验证物品id是否一致
					if (!Func.equals(serial.getSkuId(), stock.getSkuId())) {
						if (Func.isEmpty(sku)) {
							throw new ServiceException("序列号与当前物品" + stock.getSkuId() + "不匹配！");
						} else {
							throw new ServiceException("序列号与当前物品" + sku.getSkuName() + "不匹配！");
						}
					}
					// 更新当前序列号
					serial.setStockId(stock.getStockId());
					serial.setStockDetailId(stockDetail.getId());
					serial.setWhId(stock.getWhId());
					serial.setLotNumber(stock.getLotNumber());
					serial.setSystemProcId(systemProcId);
					serial_list.add(serial);
				}
			} else {
				// 新增序列号
				serial = SerialWrapper.build().entity(serialNumber, stock, stockDetail, systemProcId);
				serial_list.add(serial);
			}
			// 存储序列号日志
			SerialLog serialLog = SerialLogWrapper.build().entity(
				serial, Func.isEmpty(serial.getSerialId()) ? SerialLogProcTypeEnum.New : SerialLogProcTypeEnum.Update);
			serial_log_list.add(serialLog);
		}
		if (super.saveOrUpdateBatch(serial_list, serial_list.size())) {
			serial_log_list.stream().filter(u -> Func.equals(SerialLogProcTypeEnum.New.getIndex(), u.getProType()))
				.forEach(serialLog -> {
					Serial serial = serial_list.stream().filter(item -> {
						return Func.equals(item.getSerialNumber(), serialLog.getSerialNumber());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(serial)) {
						serialLog.setSerialId(serial.getSerialId());
					}
				});
			serialLogService.saveBatch(serial_log_list, serial_log_list.size());
		}

		return serial_list;
	}

	public Boolean removeBatch(List<String> serialNumberList, Stock stock, StockDetail stockDetail,
							   Long systemProcId) {
		// 查询出已存在的序列号
		List<Serial> serialList = new ArrayList<>();
		if (Func.isNotEmpty(serialNumberList)) {
			serialList = super.list(Condition.getQueryWrapper(new Serial()).lambda()
				.in(Serial::getSerialNumber, serialNumberList));
		}
		List<Serial> remove_list = new ArrayList<>();
		List<Serial> update_list = new ArrayList<>();
		for (Serial serial : serialList) {
			if (BigDecimalUtil.le(stockDetail.getStockQty().subtract(stockDetail.getPickQty()), BigDecimal.ZERO)) {
				remove_list.add(serial);
			} else {
				update_list.add(serial);
			}
		}
		if (Func.isNotEmpty(update_list)) {
			this.saveBatch(NodesUtil.toList(update_list, Serial::getSerialNumber), stock, stockDetail, systemProcId);
		}
		return super.removeByIds(remove_list);
	}

	@Override
	public List<Serial> listByStockId(Long stockId) {
		Serial serialQuery = new Serial();
		serialQuery.setStockId(stockId);

		return super.list(Condition.getQueryWrapper(serialQuery));
	}

	@Override
	public List<Serial> listByStockDetailId(Long stockDetailId) {
		return super.list(Condition.getQueryWrapper(new Serial()).lambda()
			.eq(Serial::getStockDetailId, stockDetailId));
	}
}
