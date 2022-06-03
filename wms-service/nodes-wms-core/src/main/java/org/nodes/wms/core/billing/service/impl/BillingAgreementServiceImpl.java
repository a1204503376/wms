package org.nodes.wms.core.billing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.billing.dto.OwnerBillPageDTO;
import org.nodes.wms.core.billing.entity.BillingAgreement;
import org.nodes.wms.core.billing.entity.BillingRuleDetail;
import org.nodes.wms.core.billing.entity.BillingRuleHeader;
import org.nodes.wms.core.billing.service.IBillingRuleDetailService;
import org.nodes.wms.core.billing.service.IBillingRuleHeaderService;
import org.nodes.wms.core.billing.vo.BillingAgreementVO;
import org.nodes.wms.core.billing.mapper.BillingAgreementMapper;
import org.nodes.wms.core.billing.service.IBillingAgreementService;
import org.nodes.wms.core.billing.vo.OwnerBillVO;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.nodes.wms.core.billing.wrapper.BillingAgreementWrapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 计费协议 服务实现类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class BillingAgreementServiceImpl<M extends BillingAgreementMapper, T extends BillingAgreement>
	extends BaseServiceImpl<BillingAgreementMapper, BillingAgreement>
	implements IBillingAgreementService {

	@Autowired
	IBillingRuleHeaderService billingRuleHeaderService;
	@Autowired
	IBillingRuleDetailService billingRuleDetailService;

	@Override
	public boolean save(BillingAgreement entity) {
		this.validData(entity);
		this.fillData(entity);
		return super.save(entity);
	}

	@Override
	public boolean updateById(BillingAgreement entity) {
		this.validData(entity);
		this.fillData(entity);
		return super.updateById(entity);
	}

	protected void validData(BillingAgreement entity) {
		int count = super.count(Condition.getQueryWrapper(new BillingAgreement()).lambda()
			.eq(BillingAgreement::getAgreementNo, entity.getAgreementNo())
			.ne(BillingAgreement::getId, entity.getId()));
		if (count > 0) {
			throw new ServiceException(String.format("协议号[%s] 已存在! ", entity.getAgreementNo()));
		}
		if (Func.isNotEmpty(entity.getEffectiveDate()) && Func.isNotEmpty(entity.getTerminationDate())) {
			if (entity.getTerminationDate().isBefore(entity.getEffectiveDate())) {
				throw new ServiceException("[解约日期] 不能早于 [生效日期]! ");
			}
		}
	}

	protected void fillData(BillingAgreement entity) {
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(entity.getWoId());
		if (Func.isNotEmpty(owner)) {
			entity.setOwnerCode(owner.getOwnerCode());
			entity.setOwnerName(owner.getOwnerName());
		}
	}

	@Override
	public IPage<OwnerBillVO> selectPageByOwnerBill(OwnerBillPageDTO ownerBillPageDTO, Query query) {
		IPage<BillingAgreement> billingAgreemenPage = super.page(Condition.getPage(query), Condition.getQueryWrapper(
			new BillingAgreement()).lambda().func(sql -> {
			if (Func.isNotEmpty(ownerBillPageDTO.getWoId())) {
				sql.eq(BillingAgreement::getWoId, ownerBillPageDTO.getWoId());
			}
			if (Func.isNotEmpty(ownerBillPageDTO.getBeginDate())) {
				sql.ge(BillingAgreement::getEffectiveDate, ownerBillPageDTO.getBeginDate());
			}
			if (Func.isNotEmpty(ownerBillPageDTO.getEndDate())) {
				sql.le(BillingAgreement::getTerminationDate, ownerBillPageDTO.getEndDate());
			}
		}));
		IPage<OwnerBillVO> ownerBillPage = new Page<>();
		ownerBillPage.setPages(billingAgreemenPage.getPages());
		ownerBillPage.setCurrent(billingAgreemenPage.getCurrent());
		ownerBillPage.setSize(billingAgreemenPage.getSize());
		ownerBillPage.setTotal(billingAgreemenPage.getTotal());
		ownerBillPage.setRecords(new ArrayList<>());
		List<Long> ruleHeaderIdList = NodesUtil.toList(
			billingAgreemenPage.getRecords(), BillingAgreement::getRuleHeaderId);
		List<BillingRuleHeader> ruleHeaderList = new ArrayList<>();
		List<BillingRuleDetail> ruleDetailList = new ArrayList<>();
		if (Func.isNotEmpty(ruleHeaderIdList)) {
			billingRuleHeaderService.listByIds(ruleHeaderIdList);
			ruleDetailList = billingRuleDetailService.list(Condition.getQueryWrapper(new BillingRuleDetail())
				.lambda()
				.in(BillingRuleDetail::getHeaderId, ruleHeaderIdList));
		}
		List<BillingRuleDetail> finalRuleDetailList = ruleDetailList;
		billingAgreemenPage.getRecords().forEach(item -> {
			OwnerBillVO ownerBill = new OwnerBillVO();
			ownerBill.setOwnerName(item.getOwnerName());
			ownerBill.setEffectiveDate(item.getEffectiveDate());
			ownerBill.setTerminationDate(item.getTerminationDate());
			ownerBill.setClosingDate(item.getClosingDate());
			ownerBill.setTotalAmount(BigDecimal.ZERO);
			BillingRuleHeader ruleHeader = ruleHeaderList.stream().filter(u -> {
				return u.getId().equals(item.getRuleHeaderId());
			}).findFirst().orElse(null);
			List<BillingRuleDetail> detailList = new ArrayList<>();
			if (Func.isNotEmpty(ruleHeader)) {
				detailList = finalRuleDetailList.stream().filter(u -> {
					return u.getHeaderId().equals(item.getRuleHeaderId());
				}).collect(Collectors.toList());
			}
			if (Func.isNotEmpty(detailList)) {
				LocalDate localDate = Func.isEmpty(item.getClosingDate()) ? LocalDate.now() : item.getClosingDate();
				BigDecimal totalAmount = detailList.stream().map(BillingRuleDetail::getAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.multiply(new BigDecimal(Period.between(item.getEffectiveDate(), localDate).getDays()));
				totalAmount = BigDecimalUtil.lt(totalAmount, BigDecimal.ZERO) ? BigDecimal.ZERO : totalAmount;
				ownerBill.setTotalAmount(totalAmount.divide(new BigDecimal(10000)));
			}
			ownerBillPage.getRecords().add(ownerBill);
		});

		return ownerBillPage;
	}
}
