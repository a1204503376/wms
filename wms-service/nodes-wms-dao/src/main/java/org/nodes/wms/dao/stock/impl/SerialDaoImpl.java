package org.nodes.wms.dao.stock.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.wms.dao.stock.SerialDao;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;
import org.nodes.wms.dao.stock.mapper.SerialMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库存序列号Dao接口实现类
 **/
@Repository
public class SerialDaoImpl extends BaseServiceImpl<SerialMapper, Serial> implements SerialDao {

	@Override
	public List<Serial> getSerialBySerialNo(List<String> serialNoList) {
		if (Func.isEmpty(serialNoList)){
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
		if (Func.isEmpty(serialNoList)){
			throw new ServiceException("序列号查询参考为空");
		}

		LambdaQueryWrapper<Serial> queryWrapper = getLambdaQuery();
		queryWrapper
			.eq(Serial::getSerialState, SerialStateEnum.OUT_STOCK.getCode())
			.in(Serial::getSerialNumber, serialNoList);
		return super.list(queryWrapper);
	}

	private LambdaQueryWrapper<Serial> getLambdaQuery(){
		return Wrappers.lambdaQuery(Serial.class);
	}
}
