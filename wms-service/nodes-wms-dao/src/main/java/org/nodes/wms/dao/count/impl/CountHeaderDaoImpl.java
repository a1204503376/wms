package org.nodes.wms.dao.count.impl;

import org.nodes.wms.dao.count.CountHeaderDao;
import org.nodes.wms.dao.count.entity.CountHeader;
import org.nodes.wms.dao.count.enums.StockCountStateEnum;
import org.nodes.wms.dao.count.mapper.CountHeaderMapper;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 盘点单头
 */
@Repository
public class CountHeaderDaoImpl
	extends BaseServiceImpl<CountHeaderMapper, CountHeader>
	implements CountHeaderDao {

	@Override
	public List<CountHeader> selectByCountBillNo(String countBillNo) {
		return super.lambdaQuery()
			.like(CountHeader::getCountBillNo, countBillNo)
			.in(CountHeader::getCountBillState, StockCountStateEnum.CREATE, StockCountStateEnum.COUNTING)
			.select(
				CountHeader::getCountBillId,
				CountHeader::getCountBillNo,
				CountHeader::getCreator,
				BaseEntity::getCreateTime)
			.list();
	}
}
