package org.nodes.wms.dao.stock.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.stock.SerialDao;
import org.nodes.wms.dao.stock.dto.input.SerialPageQuery;
import org.nodes.wms.dao.stock.dto.output.SerialExcelResponse;
import org.nodes.wms.dao.stock.dto.output.SerialPageResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;
import org.nodes.wms.dao.stock.mapper.SerialMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 库存序列号Dao接口实现类
 **/
@Repository
public class SerialDaoImpl extends BaseServiceImpl<SerialMapper, Serial> implements SerialDao {

	@Override
	public List<Serial> getSerialBySerialNo(List<String> serialNoList) {
		if (Func.isEmpty(serialNoList)) {
			throw new ServiceException("序列号查询参考为空");
		}

		LambdaQueryWrapper<Serial> queryWrapper = getLambdaQuery();
		queryWrapper
			.eq(Serial::getSerialState, SerialStateEnum.IN_STOCK.getCode())
			.in(Serial::getSerialNumber, serialNoList);
		return super.list(queryWrapper);
	}

	@Override
	public List<Serial> getOutBoundSerialBySerialNo(List<String> serialNoList) {
		if (Func.isEmpty(serialNoList)) {
			throw new ServiceException("序列号查询参考为空");
		}

		LambdaQueryWrapper<Serial> queryWrapper = getLambdaQuery();
		queryWrapper
			.eq(Serial::getSerialState, SerialStateEnum.OUT_STOCK.getCode())
			.in(Serial::getSerialNumber, serialNoList);
		return super.list(queryWrapper);
	}

	@Override
	public List<Serial> getSerialByStockId(Long stockId) {
		if (Func.isNull(stockId)) {
			throw new NullArgumentException("getSerialByStockId");
		}

		LambdaQueryWrapper<Serial> queryWrapper = getLambdaQuery();
		queryWrapper.eq(Serial::getStockId, stockId)
			.eq(Serial::getSerialState, SerialStateEnum.IN_STOCK.getCode());
		return super.list(queryWrapper);
	}

	@Override
	public List<String> getSerialNoByStockId(Long stockId) {
		AssertUtil.notNull(stockId, "根据库存ID获取序列号集合库存ID为NULL");
		List<Serial> serialList = getSerialByStockId(stockId);
		if (Func.isNotEmpty(serialList)) {
			return serialList.stream()
				.map(Serial::getSerialNumber)
				.collect(Collectors.toList());
		}

		return null;
	}

	@Override
	public void updateSerialState(List<String> serialNoList, SerialStateEnum state, Long stockId) {
		if (Func.isEmpty(serialNoList)) {
			throw new NullArgumentException("更新序列号状态");
		}

		UpdateWrapper<Serial> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.in(Serial::getSerialNumber, serialNoList);
		Serial serial = new Serial();
		serial.setSerialState(state);
		if (!Func.isNull(stockId)) {
			serial.setStockId(stockId);
		}
		if (!super.update(serial, updateWrapper)) {
			throw new ServiceException("更新序列号状态失败");
		}
	}

	@Override
	public int getSerialCountByStockId(Long stockId) {
		LambdaQueryWrapper<Serial> queryWrapper = getLambdaQuery();
		queryWrapper.eq(Serial::getStockId, stockId)
			.eq(Serial::getSerialState, SerialStateEnum.IN_STOCK.getCode());
		return super.count(queryWrapper);
	}

	@Override
	public Page<SerialPageResponse> getPage(SerialPageQuery serialPageQuery, IPage<?> page) {
		return super.baseMapper.getPage(serialPageQuery, page);
	}

	@Override
	public List<SerialExcelResponse> listByQuery(SerialPageQuery serialPageQuery) {
		return super.baseMapper.listByQuery(serialPageQuery);
	}

	@Override
	public Serial getSerialSerialNo(String serialNo) {
		return super.lambdaQuery()
			.like(Serial::getSerialNumber, serialNo)
			.eq(Serial::getSerialState, SerialStateEnum.IN_STOCK)
			.or()
			.eq(Serial::getSerialNumber, serialNo)
			.eq(Serial::getSerialState, SerialStateEnum.IN_STOCK)
			.one();
	}

	@Override
	public void updateSerialStockIdBySerialNo(Long sourceStockId, Long targetStockId, String serialNo) {
		AssertUtil.notNull(sourceStockId, "更新序列号失败，原库存ID为空");
		AssertUtil.notNull(targetStockId, "更新序列号失败，目标库存ID为空");
		AssertUtil.notNull(serialNo, "更新序列号失败，序列号为空");
		UpdateWrapper<Serial> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(Serial::getSerialNumber, serialNo)
			.eq(Serial::getStockId, sourceStockId);
		Serial serial = new Serial();
		serial.setStockId(targetStockId);
		if (super.baseMapper.update(serial, updateWrapper) < 0) {
			throw new ServiceException("更新序列号状态失败");
		}
	}

	private LambdaQueryWrapper<Serial> getLambdaQuery() {
		return Wrappers.lambdaQuery(Serial.class);
	}
}
