package org.nodes.wms.core.instock.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.SysCache;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.instock.asn.cache.AsnCache;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderDTO;
import org.nodes.wms.core.instock.asn.enums.SyncStateEnum;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.instock.asn.wrapper.AsnHeaderWrapper;
import org.nodes.wms.core.instock.purchase.dto.PoDetailDTO;
import org.nodes.wms.core.instock.purchase.dto.PoHeaderDTO;
import org.nodes.wms.core.instock.purchase.dto.PoHeaderQueryDTO;
import org.nodes.wms.core.instock.purchase.entity.PoDetail;
import org.nodes.wms.core.instock.purchase.entity.PoHeader;
import org.nodes.wms.core.instock.purchase.mapper.PoHeaderMapper;
import org.nodes.wms.core.instock.purchase.service.IPoDetailService;
import org.nodes.wms.core.instock.purchase.service.IPoHeaderService;
import org.nodes.wms.core.instock.purchase.vo.PoHeaderVO;
import org.nodes.wms.core.instock.purchase.wrapper.PoDetailWrapper;
import org.nodes.wms.core.instock.purchase.wrapper.PoHeaderWrapper;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ??????????????? ???????????????
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class PoHeaderServiceImpl<M extends PoHeaderMapper, T extends PoHeader>
	extends BaseServiceImpl<PoHeaderMapper, PoHeader>
	implements IPoHeaderService {

	@Autowired
	IPoDetailService poDetailService;

	@Autowired
	IAsnHeaderService asnHeaderService;

	@Override
	public boolean saveOrUpdate(PoHeaderDTO poHeader) {
		//????????????
		Dept dept = SysCache.getDept(poHeader.getDeptId());
		if (Func.isNotEmpty(dept)) {
			poHeader.setDeptCode(dept.getDeptCode());//????????????
			poHeader.setDeptName(dept.getDeptName());//????????????
		}
		if (Func.isEmpty(poHeader.getDetailList())) {
			throw new ServiceException(String.format("???????????????????????????"));
		}
		//??????
		Warehouse warehouse = WarehouseCache.getById(poHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("??????????????????????????????");
		}
		poHeader.setWhCode(warehouse.getWhCode());//????????????
		//????????????
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(poHeader.getWoId());
		if (Func.isEmpty(owner)) {
			throw new ServiceException("??????????????????????????????");
		}
		poHeader.setOwnerCode(owner.getOwnerCode());
		poHeader.setPoBillState(AsnBillStateEnum.NOT_RECEIPT.getCode());//????????????

		//??????????????????????????????
		if (Func.isEmpty(poHeader.getBillKey())) {
			poHeader.setBillKey(poHeader.getPoBillNo());
		}
		//??????????????????????????????
		if (Func.isEmpty(poHeader.getLastUpdateDate())) {
			poHeader.setLastUpdateDate(LocalDateTime.now());
		}
		//??????????????????????????????
		if (Func.isEmpty(poHeader.getPreCreateDate())) {
			poHeader.setPreCreateDate(LocalDateTime.now());
		}
		//????????????
		if (Func.isEmpty(poHeader.getCreateType())) {
			poHeader.setCreateType(10);
		}

		if (Func.isEmpty(poHeader.getPoBillId())) {
			while (super.count(Condition.getQueryWrapper(new PoHeader()).lambda()
				.eq(PoHeader::getPoBillNo, poHeader.getPoBillNo())) > 0) {
				poHeader.setPoBillNo(AsnCache.getPoBillNo());
			}
			poHeader.setPoBillState(AsnBillStateEnum.NOT_RECEIPT.getCode());
			poHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		}
		if (super.saveOrUpdate(poHeader)) {
			//???????????????
			if (AsnBillStateEnum.NOT_RECEIPT.getCode().equals(poHeader.getPoBillState())) {
				poDetailService.remove(Condition.getQueryWrapper(new PoDetail()).lambda()
					.eq(PoDetail::getPoBillId, poHeader.getPoBillId()));
				List<PoDetailDTO> detailList = poHeader.getDetailList();
				for (PoDetailDTO detail : detailList) {
					detail.setPoBillId(poHeader.getPoBillId());
					detail.setPoBillNo(poHeader.getPoBillNo());
					poDetailService.saveOrUpdate(detail);
				}
			}
		}
		return true;
	}

	@Override
	public List<PoHeaderVO> selectList(PoHeaderQueryDTO poHeader) {
		List<PoHeader> list = super.list(this.getQueryWrapper(poHeader));
		return PoHeaderWrapper.build().listVO(list);
	}

	@Override
	public IPage<PoHeaderVO> selectPage(PoHeaderQueryDTO poHeader, Query query) {
		IPage<PoHeader> page = super.page(Condition.getPage(query), this.getQueryWrapper(poHeader));
		return PoHeaderWrapper.build().pageVO(page);
	}

	@Override
	public PoHeaderVO getDetail(Long poBillId) {
		//?????????????????????
		PoHeader poHeader = super.getById(poBillId);
		if (Func.isEmpty(poHeader)) {
			throw new ServiceException("???????????????????????????");
		}
		PoHeaderVO poHeaderVO = PoHeaderWrapper.build().entityVO(poHeader);
		//?????????????????????????????????
		List<PoDetail> poDetailList = poDetailService.list(
			Condition.getQueryWrapper(new PoDetail()).lambda()
				.eq(PoDetail::getPoBillId, poHeader.getPoBillId())
				.orderByAsc(PoDetail::getPoLineNo));
		poHeaderVO.setDetailList(PoDetailWrapper.build().listVO(poDetailList));
		return poHeaderVO;
	}

	@Override
	public boolean createAsn(PoHeaderDTO poHeader) {
		long count = poHeader.getDetailList().stream().filter(u -> {
			return BigDecimalUtil.gt(u.getSurplusQty(), BigDecimal.ZERO);
		}).count();
		if (count == 0L) {
			throw new ServiceException(String.format(
				"?????????[%s] ???????????????????????????????????????! ", poHeader.getPoBillNo()));
		}
		AsnHeaderDTO asnHeader = AsnHeaderWrapper.build().entityDTO(poHeader);

		return asnHeaderService.saveOrUpdate(asnHeader);
	}

	protected LambdaQueryWrapper<PoHeader> getQueryWrapper(PoHeaderQueryDTO query) {
		LambdaQueryWrapper<PoHeader> queryWrapper = Condition.getQueryWrapper(new PoHeader()).lambda();

		if (Func.isNotEmpty(query.getPoBillNo())) {
			queryWrapper.like(PoHeader::getPoBillNo, query.getPoBillNo());
		}
		if (Func.isNotEmpty(query.getOrderNo())) {
			queryWrapper.like(PoHeader::getOrderNo, query.getOrderNo());
		}
		// ?????? ?????????????????? ????????????
		if (Func.isNotEmpty(query.getScheduledArrivalRange())) {
			String[] timeList = JsonUtil.parse(query.getScheduledArrivalRange(), String[].class);
			if (Func.isNotEmpty(timeList) && timeList.length > 0) {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				queryWrapper.ge(PoHeader::getScheduledArrivalDate, LocalDateTime.parse(timeList[0], dateTimeFormatter));
				if (timeList.length > 1) {
					queryWrapper.le(PoHeader::getScheduledArrivalDate, LocalDateTime.parse(timeList[1], dateTimeFormatter));
				}
			}
		}
		// ?????? ?????????????????? ????????????
		if (Func.isNotEmpty(query.getActualArrivalRange())) {
			String[] timeList = JsonUtil.parse(query.getActualArrivalRange(), String[].class);
			if (Func.isNotEmpty(timeList) && timeList.length > 0) {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATETIME);
				queryWrapper.ge(PoHeader::getActualArrivalDate, LocalDateTime.parse(timeList[0], dateTimeFormatter));
				if (timeList.length > 1) {
					queryWrapper.le(PoHeader::getActualArrivalDate, LocalDateTime.parse(timeList[1], dateTimeFormatter));
				}
			}
		}
		// ?????? ?????????????????? ????????????
		if (Func.isNotEmpty(query.getFinishRange())) {
			String[] timeList = JsonUtil.parse(query.getFinishRange(), String[].class);
			if (Func.isNotEmpty(timeList) && timeList.length > 0) {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATETIME);
				queryWrapper.ge(PoHeader::getFinishDate, LocalDateTime.parse(timeList[0], dateTimeFormatter));
				if (timeList.length > 1) {
					queryWrapper.le(PoHeader::getFinishDate, LocalDateTime.parse(timeList[1], dateTimeFormatter));
				}
			}
		}
		queryWrapper.orderByDesc(PoHeader::getPoBillNo);
		return queryWrapper;
	}
}
