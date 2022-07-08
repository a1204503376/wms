package org.nodes.wms.dao.basics.skulot.impl;

import org.apache.commons.lang.NullArgumentException;
import org.nodes.wms.dao.basics.skulot.SkuLotValDao;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotVal;
import org.nodes.wms.dao.basics.skulot.mapper.SkuLotValMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SkuLotValDaoImpl
	extends BaseServiceImpl<SkuLotValMapper, SkuLotVal>
	implements SkuLotValDao {

	@Override
	public SkuLotVal getByWhIdAndWoId(Long whId, Long woId) {
		if (Func.isNull(whId) || Func.isNull(woId)){
			throw new NullArgumentException("SkuLotValDaoImpl.find");
		}

		List<SkuLotVal> skuLotValList = super.lambdaQuery()
			.eq(SkuLotVal::getWhId, whId)
			.eq(SkuLotVal::getWoId, woId)
			.list();
		if (Func.isNotEmpty(skuLotValList)){
			return  skuLotValList.get(0);
		}

		return null;
	}
}
