package org.nodes.wms.dao.instock.asn.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.asn.AsnHeaderDao;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnExportResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.nodes.wms.dao.instock.asn.mapper.AsnHeaderMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ASN单头表 DAO 实现类
 */
@Repository
@RequiredArgsConstructor
public class AsnHeaderDaoImpl
	extends BaseServiceImpl<AsnHeaderMapper,AsnHeader>
	implements AsnHeaderDao {

	private final AsnHeaderMapper asnHeaderMapper;

	@Override
	public Page<PageResponse> selectPageAsnBill(IPage<?> page, PageParamsQuery pageParamsQuery) {
		return asnHeaderMapper.selectPageAsnBill(page, pageParamsQuery);
	}

	@Override
	public boolean insertAsnHeader(AsnHeader asnHeader) {
		return super.save(asnHeader);
	}

	@Override
	public AsnDetailResponse selectAsnContactDetailByAsnBillId(Long asnBillId) {
		return asnHeaderMapper.selectAsnContactDetail(asnBillId);
	}

	@Override
	public boolean deleteAsnHeaderById(List<Long> idList) {
		return super.deleteLogic(idList);
	}

	@Override
	public List<AsnExportResponse> listByParamsQuery(PageParamsQuery pageParamsQuery) {
		return asnHeaderMapper.selectAsnBillList(pageParamsQuery);
	}
}
