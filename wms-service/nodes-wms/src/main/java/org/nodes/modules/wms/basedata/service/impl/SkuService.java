package org.nodes.modules.wms.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.modules.wms.basedata.service.ISkuService;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.dto.SkuDTO;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.mapper.SkuMapper;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.service.IAsnDetailService;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * author: pengwei
 * date: 2021/5/10 17:04
 * description: SkuService
 */
@Service
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuService extends org.nodes.wms.core.basedata.service.impl.SkuServiceImpl<SkuMapper, Sku> implements ISkuService {

	@Autowired
	ISoHeaderService soHeaderService;
	@Autowired
	ISoDetailService soDetailService;
	@Autowired
	IAsnHeaderService asnHeaderService;
	@Autowired
	IAsnDetailService asnDetailService;

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {

		// ????????????????????????????????????????????????
		List<SoHeader> soHeaderList = soHeaderService.list(Condition.getQueryWrapper(new SoHeader()).lambda()
			.notIn(SoHeader::getSoBillState,
				SoBillStateEnum.COMPLETED.getCode(),
				SoBillStateEnum.ALL_OUT_STOCK.getCode(),
				SoBillStateEnum.CANCELED.getCode()));
		QueryWrapper<SoDetail> soDetailQueryWrapper = Condition.getQueryWrapper(new SoDetail());
		soDetailQueryWrapper.lambda()
			.gt(SoDetail::getSurplusQty, BigDecimal.ZERO)
			.in(SoDetail::getSkuId, idList);
		if (Func.isNotEmpty(soHeaderList)) {
			soDetailQueryWrapper.lambda()
				.in(SoDetail::getSoBillId, NodesUtil.toList(soHeaderList, SoHeader::getSoBillId));
		}
		List<SoDetail> soDetailList = soDetailService.list(soDetailQueryWrapper);
		if (Func.isNotEmpty(soDetailList)) {
			throw new ServiceException(String.format(
				"?????????%s ?????????????????????????????????????????????????????????????????????", NodesUtil.join(soDetailList, "skuName")));
		}
		// ????????????????????????????????????????????????
		List<AsnHeader> asnHeaderList = asnHeaderService.list(Condition.getQueryWrapper(new AsnHeader()).lambda()
			.notIn(AsnHeader::getAsnBillState,
				AsnBillStateEnum.COMPLETED.getCode(),
				AsnBillStateEnum.CANCEL.getCode()));
		QueryWrapper<AsnDetail> asnDetailQueryWrapper = Condition.getQueryWrapper(new AsnDetail());
		asnDetailQueryWrapper.lambda()
			.gt(AsnDetail::getSurplusQty, BigDecimal.ZERO)
			.in(AsnDetail::getSkuId, idList);
		if (Func.isNotEmpty(asnHeaderList)) {
			asnDetailQueryWrapper.lambda()
				.in(AsnDetail::getAsnBillId, NodesUtil.toList(asnHeaderList, AsnHeader::getAsnBillId));
		}
		List<AsnDetail> asnDetailList = asnDetailService.list(asnDetailQueryWrapper);
		if (Func.isNotEmpty(asnDetailList)) {
			throw new ServiceException(String.format(
				"?????????%s ?????????????????????????????????????????????????????????????????????", NodesUtil.join(asnDetailList, "skuName")));
		}

		return super.removeByIds(idList);
	}

	@Override
	public boolean removeById(Serializable id) {
		List<Long> idList = new ArrayList() {{
			add(id);
		}};
		return this.removeByIds(idList);
	}

	@Override
	public boolean updateById(SkuDTO entity) {
		// ???????????????????????????
		Sku skuOld = super.getById(entity.getSkuId());
		if (Func.isNotEmpty(entity.getWspId()) && !Objects.equals(skuOld.getWspId(), entity.getWspId())) {
			// ????????????
			List<AsnHeader> asnHeaderList = asnHeaderService.list(Condition.getQueryWrapper(new AsnHeader())
				.lambda()
				.eq(AsnHeader::getAsnBillState, AsnBillStateEnum.PART.getCode()));
			if (Func.isNotEmpty(asnHeaderList)) {
				List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
					.lambda()
					.in(AsnDetail::getAsnBillId, NodesUtil.toList(asnHeaderList, AsnHeader::getAsnBillId))
					.eq(AsnDetail::getSkuId, entity.getSkuId()));
				if (Func.isNotEmpty(asnDetailList)) {
					String asnBillNos = NodesUtil.join(asnDetailList, "asnBillNo");
					throw new ServiceException(String.format("[%s] ???????????????????????????????????????????????????", asnBillNos));
				}
			}
			// ????????????
			List<SoHeader> soHeaderList = soHeaderService.list(Condition.getQueryWrapper(new SoHeader())
				.lambda()
				.eq(SoHeader::getSoBillState, SoBillStateEnum.EXECUTING.getCode()));
			if (Func.isNotEmpty(soHeaderList)) {
				List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
					.lambda()
					.in(SoDetail::getSoBillId, NodesUtil.toList(soHeaderList, SoHeader::getSoBillId))
					.eq(SoDetail::getSkuId, entity.getSkuId()));
				if (Func.isNotEmpty(soDetailList)) {
					String soBillNos = NodesUtil.join(soDetailList, "soBillNo");
					throw new ServiceException(String.format("[%s] ??????????????????????????????????????????", soBillNos));
				}
			}
			// ????????????????????????????????????
			asnHeaderList = asnHeaderService.list(Condition.getQueryWrapper(new AsnHeader())
				.lambda()
				.eq(AsnHeader::getAsnBillState, AsnBillStateEnum.NOT_RECEIPT.getCode()));
			if (Func.isNotEmpty(asnHeaderList)) {
				List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
					.lambda()
					.in(AsnDetail::getAsnBillId, NodesUtil.toList(asnHeaderList, AsnHeader::getAsnBillId))
					.eq(AsnDetail::getSkuId, entity.getSkuId()));
				if (Func.isNotEmpty(asnDetailList)) {
					UpdateWrapper<AsnDetail> asnDetailUpdateWrapper = new UpdateWrapper<>();
					asnDetailUpdateWrapper.lambda()
						.set(AsnDetail::getWspId, entity.getWspId())
						.in(AsnDetail::getAsnDetailId, NodesUtil.toList(asnDetailList, AsnDetail::getAsnDetailId));
					asnDetailService.update(asnDetailUpdateWrapper);
				}
			}
			// ????????????????????????????????????
			soHeaderList = soHeaderService.list(Condition.getQueryWrapper(new SoHeader())
				.lambda()
				.eq(SoHeader::getSoBillState, SoBillStateEnum.CREATE.getCode()));
			if (Func.isNotEmpty(soHeaderList)) {
				List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
					.lambda()
					.in(SoDetail::getSoBillId, NodesUtil.toList(soHeaderList, SoHeader::getSoBillId))
					.eq(SoDetail::getSkuId, entity.getSkuId()));
				if (Func.isNotEmpty(soDetailList)) {
					UpdateWrapper<SoDetail> soDetailUpdateWrapper = new UpdateWrapper<>();
					soDetailUpdateWrapper.lambda()
						.set(SoDetail::getWspId, entity.getWspId())
						.in(SoDetail::getSoDetailId, NodesUtil.toList(soDetailList, SoDetail::getSoDetailId));
					soDetailService.update(soDetailUpdateWrapper);
				}
			}
		}
		return super.updateById(entity);
	}

	@Override
	public boolean editValid(Long skuId) {
		//??????
		Sku skuOld = SkuCache.getById(skuId);
		// ????????????
		List<AsnHeader> asnHeaderList = asnHeaderService.list(Condition.getQueryWrapper(new AsnHeader())
			.lambda()
			.eq(AsnHeader::getAsnBillState, AsnBillStateEnum.PART.getCode()));
		if (Func.isNotEmpty(asnHeaderList)) {
			List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
				.lambda()
				.in(AsnDetail::getAsnBillId, NodesUtil.toList(asnHeaderList, AsnHeader::getAsnBillId))
				.eq(AsnDetail::getSkuId, skuId));
			if (Func.isNotEmpty(asnDetailList)) {
				String asnBillNos = NodesUtil.join(asnDetailList, "asnBillNo");
				throw new ServiceException(String.format("[%s] ???????????????????????????????????????????????????", asnBillNos));
			}
		}
		// ????????????
		List<SoHeader> soHeaderList = soHeaderService.list(Condition.getQueryWrapper(new SoHeader())
			.lambda()
			.eq(SoHeader::getSoBillState, SoBillStateEnum.EXECUTING.getCode()));
		if (Func.isNotEmpty(soHeaderList)) {
			List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
				.lambda()
				.in(SoDetail::getSoBillId, NodesUtil.toList(soHeaderList, SoHeader::getSoBillId))
				.eq(SoDetail::getSkuId, skuId));
			if (Func.isNotEmpty(soDetailList)) {
				String soBillNos = NodesUtil.join(soDetailList, "soBillNo");
				throw new ServiceException(String.format("[%s] ??????????????????????????????????????????", soBillNos));
			}
		}
		return true;
	}
}

