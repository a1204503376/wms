package org.nodes.wms.dao.instock.asn.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.asn.AsnDetailDao;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.mapper.AsnDetailMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 彭永程
 * @date 2022-04-25 9:48
 **/
@Service
@RequiredArgsConstructor
public class AsnDetailDaoImpl
	extends BaseServiceImpl<AsnDetailMapper, AsnDetail>
	implements AsnDetailDao {

	private final AsnDetailMapper asnDetailMapper;
	@Override
	public List<Long> selectAsnDetailIdListByAsnBillId(List<Long> asnBillIdList) {
		return asnDetailMapper.selectAsnDetailId(asnBillIdList);
	}
}
