package org.nodes.wms.dao.instock.asn.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.asn.AsnDao;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entitits.AsnDetail;
import org.nodes.wms.dao.instock.asn.entitits.AsnHeader;
import org.nodes.wms.dao.instock.asn.mapper.AsnHeaderMapper;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

/**
 * ASN单据 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class AsnDaoImpl implements AsnDao {

	private final AsnHeaderMapper asnHeaderRepository;

	@Override
	public Page<PageResponse> getAsnPageForWrapper(IPage<?> page, PageParamsQuery pageParamsQuery) {
		QueryWrapper<?> queryWrapper = Wrappers.query();
		queryWrapper.like(Func.isNotBlank(pageParamsQuery.getAsnBillNo()), "h.asn_bill_no", pageParamsQuery.getAsnBillNo());
		queryWrapper.like(Func.isNotBlank(pageParamsQuery.getSkuCode()), "d.sku_code", pageParamsQuery.getSkuCode());

		return asnHeaderRepository.getAsnPageForWrapper(page, queryWrapper);
	}

	@Override
	public void addAsnHeaderAndAsnDetail(AsnHeader asnHeader, AsnDetail asnDetail) {

	}

	@Override
	public Page<PageResponse> getAsnPage(IPage<?> page, PageParamsQuery pageParamsQuery) {
		return asnHeaderRepository.getAsnPage(page, pageParamsQuery);
	}
}
