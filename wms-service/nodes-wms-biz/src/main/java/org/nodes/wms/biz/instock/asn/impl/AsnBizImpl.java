package org.nodes.wms.biz.instock.asn.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.biz.instock.asn.modular.AsnFactory;
import org.nodes.wms.dao.instock.asn.AsnDao;
import org.nodes.wms.dao.instock.asn.dto.input.AsnRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.springframework.stereotype.Service;

/**
 * ASN单 业务类
 */
@Service
@RequiredArgsConstructor
public class AsnBizImpl implements AsnBiz {

	private final AsnDao asnDao;
	private final AsnFactory asnFactory;

	@Override
	public Page<PageResponse> getAsnPageForWrapper(IPage<?> page,
												   PageParamsQuery pageParamsQuery) {
		return asnDao.getAsnPageForWrapper(page, pageParamsQuery);
	}

	@Override
	public Page<PageResponse> getAsnPage(IPage<?> page, PageParamsQuery pageParamsQuery) {
		return asnDao.getAsnPage(page, pageParamsQuery);
	}

	@Override
	public void save(AsnRequest asnRequest) {
		AsnHeader asnHeader = asnFactory.createAsnHeader(asnRequest);
		AsnDetail asnDetail = asnFactory.createAsnDetail(asnRequest);
		asnDao.addAsnHeaderAndAsnDetail(asnHeader, asnDetail);
	}
}
