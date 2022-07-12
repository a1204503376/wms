package org.nodes.wms.dao.basics.skulot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.wms.dao.basics.skulot.SkuLotDao;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;
import org.nodes.wms.dao.basics.skulot.mapper.SkuLotMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

@Repository
public class SkuLotDaoImpl
	extends BaseServiceImpl<SkuLotMapper, SkuLot>
	implements SkuLotDao {
	@Override
	public SkuLot getSkuLotByWoId(Long woId) {
		if(Func.isEmpty(woId))
		{
			throw new NullArgumentException("SkuLotDaoImpl.getSkuLotByWoId参数为空");
		}
		LambdaQueryWrapper<SkuLot> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(SkuLot::getWoId,woId);
		return super.getOne(lambdaQueryWrapper);
	}
}
