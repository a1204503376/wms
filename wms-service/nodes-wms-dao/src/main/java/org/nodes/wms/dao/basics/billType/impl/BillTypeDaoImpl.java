package org.nodes.wms.dao.basics.billType.impl;

import org.nodes.wms.dao.basics.billType.BillTypeDao;
import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.dao.basics.billType.mapper.BillTypeMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单据类型 DAO实现类
 */
@Repository
public class BillTypeDaoImpl
	extends BaseServiceImpl<BillTypeMapper, BillType>
	implements BillTypeDao {

	@Override
	public List<BillTypeSelectResponse>
	listTop10ByBillTypeCdBillTypeName(String ioType, String billTypeCd, String billTypeName) {
		return super.baseMapper.listTop10ByBillTypeCdBillTypeName(ioType,billTypeCd, billTypeName);
	}
}
