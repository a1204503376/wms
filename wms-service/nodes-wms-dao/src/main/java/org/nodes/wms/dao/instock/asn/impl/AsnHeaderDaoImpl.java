package org.nodes.wms.dao.instock.asn.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.asn.AsnDetailDao;
import org.nodes.wms.dao.instock.asn.AsnHeaderDao;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.nodes.wms.dao.instock.asn.mapper.AsnHeaderMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ASN单据 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class AsnHeaderDaoImpl
	extends BaseServiceImpl<AsnHeaderMapper,AsnHeader>
	implements AsnHeaderDao {

	private final AsnDetailDao asnDetailDao;

	private final AsnHeaderMapper asnHeaderRepository;

	@Override
	public Page<PageResponse> selectPageAsnBill(IPage<?> page, PageParamsQuery pageParamsQuery) {
		QueryWrapper<?> queryWrapper = Wrappers.query();
		queryWrapper.eq(Func.isNotBlank(pageParamsQuery.getAsnBillNo()), "h.asn_bill_no", pageParamsQuery.getAsnBillNo())
			.eq(Func.isNotBlank(pageParamsQuery.getSkuCode()), "d.sku_code", pageParamsQuery.getSkuCode())
			.in(Func.isNotEmpty(pageParamsQuery.getAsnBillState()), "h.asn_bill_state", pageParamsQuery.getAsnBillState())
			.ge(Func.isNotEmpty(pageParamsQuery.getCreateTimeBegin()), "h.create_time", pageParamsQuery.getCreateTimeBegin())
			.le(Func.isNotEmpty(pageParamsQuery.getCreateTimeEnd()), "h.create_time", pageParamsQuery.getCreateTimeEnd())
			.and(wrapper -> wrapper.like(Func.isNotBlank(pageParamsQuery.getSuppliers()), "h.s_code", pageParamsQuery.getSuppliers())
				.or().like(Func.isNotBlank(pageParamsQuery.getSuppliers()), "h.s_name", pageParamsQuery.getSuppliers()))
			.eq(Func.isNotBlank(pageParamsQuery.getExternalOrderNo()), "h.external_order_no", pageParamsQuery.getExternalOrderNo())
			.eq(Func.isNotBlank(pageParamsQuery.getExternalCreateUser()), "h.external_create_user", pageParamsQuery.getExternalCreateUser())
			.eq(Func.isNotBlank(pageParamsQuery.getWhCode()), "h.wh_code", pageParamsQuery.getWhCode())
			.orderByDesc(Func.isEmpty(page.orders()), "h.create_time");
		return asnHeaderRepository.selectPageAsnBill(page, queryWrapper);
	}

	@Override
	public void addAsnHeaderAndAsnDetail(AsnHeader asnHeader, AsnDetail asnDetail) {

	}

	@Override
	public AsnDetailResponse selectAsnContactDetailByAsnBillId(List<Long> idList) {
		return asnHeaderRepository.selectAsnContactDetailByAsnBillId(idList);
	}

	@Override
	public Boolean deleteAsnHeaderById(List<Long> idList) {
		return super.deleteLogic(idList);
	}

	@Override
	public Boolean deleteAsnDetailByAsnBillId(List<Long> idList) {
		return super.deleteLogic(idList);
	}
}
