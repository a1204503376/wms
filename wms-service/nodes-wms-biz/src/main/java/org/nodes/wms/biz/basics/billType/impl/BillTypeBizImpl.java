package org.nodes.wms.biz.basics.billType.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.billType.BillTypeBiz;
import org.nodes.wms.dao.basics.billType.BillTypeDao;
import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectQuery;
import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单据类型 业务层实现类
 */
@Service
@RequiredArgsConstructor
public class BillTypeBizImpl implements BillTypeBiz {

	private final BillTypeDao billTypeDao;

	@Override
	public List<BillTypeSelectResponse> getBillTypeSelectResponseTop10List(BillTypeSelectQuery billTypeSelectQuery) {
		return billTypeDao.listTop10ByBillTypeCdBillTypeName(
			billTypeSelectQuery.getIoType(), billTypeSelectQuery.getKey(),billTypeSelectQuery.getKey()
		);
	}
}
