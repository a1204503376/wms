package org.nodes.wms.dao.instock.asn.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.asn.AsnHeaderDao;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillExportResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillViewResponse;
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

	@Override
	public Page<PageResponse> selectPageAsnBill(IPage<?> page, PageParamsQuery pageParamsQuery) {
		return super.baseMapper.selectPageAsnBill(page, pageParamsQuery);
	}

	@Override
	public boolean saveOrUpdateAsnHeader(AsnHeader asnHeader) {
		return super.saveOrUpdate(asnHeader);
	}

	@Override
	public AsnBillViewResponse getAsnBillViewDetailById(Long asnBillId) {
		return super.baseMapper.selectAsnBillViewDetailById(asnBillId);
	}

	@Override
	public boolean deleteAsnHeaderById(List<Long> idList) {
		return super.deleteLogic(idList);
	}

	@Override
	public List<AsnBillExportResponse> listByParamsQuery(PageParamsQuery pageParamsQuery) {
		return super.baseMapper.selectAsnBillList(pageParamsQuery);
	}

	@Override
	public AsnHeader getAsnHeaderByAsnBillId(Long asnBillId) {
		return super.baseMapper.selectAsnHeaderByAsnBillId(asnBillId);
	}
}
