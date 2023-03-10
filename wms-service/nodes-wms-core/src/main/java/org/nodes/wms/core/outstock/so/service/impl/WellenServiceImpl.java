
package org.nodes.wms.core.outstock.so.service.impl;

import org.nodes.wms.core.outstock.loading.wrapper.SoWellenDetailWrapper;
import org.nodes.wms.core.outstock.loading.wrapper.SoWellenWrapper;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.stock.core.dto.StockOccupySubtractDTO;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.core.outstock.so.enums.WellenStateEnum;
import org.nodes.wms.core.outstock.so.mapper.WellenMapper;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.service.IWellenService;
import org.nodes.wms.core.outstock.so.vo.WellenDetailVO;
import org.nodes.wms.core.outstock.so.vo.WellenVO;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ??????????????? ???????????????
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class WellenServiceImpl<M extends WellenMapper, T extends Wellen>
	extends BaseServiceImpl<WellenMapper, Wellen>
	implements IWellenService {

	@Autowired
	IWellenDetailService wellenDetailService;
	@Autowired
	ISoDetailService soDetailService;
	@Autowired
	IStockOccupyService stockOccupyService;
	@Autowired
	IStockService stockService;
	@Autowired
	ITaskService taskService;

	@Override
	public List<WellenVO> create(List<SoHeader> soHeaderList, List<Long> soDetailList) {
		if (ObjectUtil.isEmpty(soHeaderList)) {
			throw new ServiceException("??????????????????????????????");
		}
		List<WellenVO> wellenList = new ArrayList<>();
		// ??????????????????ID??????????????????????????????
		soHeaderList.stream().collect(Collectors.groupingBy(SoHeader::getWhId)).forEach((whId, soList) -> {
			// ??????????????????
			WellenVO wellen = new WellenVO();
			wellen.setWellenNo(SerialNoCache.getWellenNo());
			while (super.count(Condition.getQueryWrapper(wellen)) > 0) {
				wellen.setWellenNo(SerialNoCache.getWellenNo());
			}
			wellen.setWellenState(WellenStateEnum.Create.getIndex());
			wellen.setWhId(whId);
			wellen.setBillMultiple(StringPool.Y.toUpperCase());
			// ??????????????????
			super.save(wellen);
			// ??????????????????
			for (SoHeader soHeader : soList) {
				// ??????????????????
				SoDetail soDetailQuery = new SoDetail();
				soDetailQuery.setSoBillId(soHeader.getSoBillId());
				// ???????????????????????????????????????????????????
				List<SoDetail> billDetails = soDetailService.list(Condition.getQueryWrapper(new SoDetail()).lambda()
					.eq(SoDetail::getSoBillId, soHeader.getSoBillId())
					.func(sql -> {
						if (Func.isNotEmpty(soDetailList)) {
							sql.in(SoDetail::getSoDetailId, soDetailList);
						}
					}))
					.stream()
					.filter(soDetail -> {
						return !soDetail.getBillDetailState().equals(SoDetailStateEnum.UnAlloc);
					}).collect(Collectors.toList());
				if (ObjectUtil.isEmpty(billDetails)) {
					throw new ServiceException("???????????????????????????");
				}
				// ????????????????????????
				for (SoDetail billDetail : billDetails) {
					WellenDetailVO detail = wellenDetailService.create(wellen, billDetail);
					detail.setSoHeader(soHeader);
					wellen.getDetailList().add(detail);
				}
			}
			wellenList.add(wellen);
		});

		return wellenList;
	}

	@Override
	public WellenVO create(SoHeader soHeader, List<Long> soDetailList) {
		// ??????????????????
		WellenVO wellen = new WellenVO();
		wellen.setWellenNo(SerialNoCache.getWellenNo());
		while (super.count(Condition.getQueryWrapper(wellen)) > 0) {
			wellen.setWellenNo(SerialNoCache.getWellenNo());
		}
		wellen.setWellenState(WellenStateEnum.Create.getIndex());
		wellen.setWhId(soHeader.getWhId());
		wellen.setBillMultiple(StringPool.N.toUpperCase());
		// ??????????????????
		super.save(wellen);
		// ???????????????????????????????????????????????????
		List<SoDetail> billDetails = soDetailService.list(Condition.getQueryWrapper(new SoDetail()).lambda()
			.eq(SoDetail::getSoBillId, soHeader.getSoBillId())
			.func(sql -> {
				if (Func.isNotEmpty(soDetailList)) {
					sql.in(SoDetail::getSoDetailId, soDetailList);
				}
			}))
			.stream()
			.filter(soDetail -> {
				return !soDetail.getBillDetailState().equals(SoDetailStateEnum.UnAlloc);
			}).collect(Collectors.toList());
		if (ObjectUtil.isEmpty(billDetails)) {
			throw new ServiceException("???????????????????????????");
		}
		// ????????????????????????
		for (SoDetail billDetail : billDetails) {
			WellenDetailVO detail = wellenDetailService.create(wellen, billDetail);
			detail.setSoHeader(soHeader);
			wellen.getDetailList().add(detail);
		}
		return wellen;
	}


	@Override
	public synchronized boolean updateState(Long wellenId, WellenStateEnum wellenState) {

		Wellen wellen = super.getById(wellenId);
		if (ObjectUtil.isEmpty(wellen)) {
			throw new ServiceException("????????????????????????ID???" + wellenId + "??????");
		}
		if (wellenState.equals(WellenStateEnum.AllocComplated) &&
			!wellen.getWellenState().equals(WellenStateEnum.Create.getIndex())) {

			throw new ServiceException(String.format("??????????????????????????????ID???%s??????????????????%s??????",
				wellenId, WellenStateEnum.getName(wellen.getWellenState())));
		}
		wellen.setWellenState(wellenState.getIndex());
		return super.updateById(wellen);
	}

	@Override
	public List<StockOccupy> rollback(Long billId) {
		List<StockOccupy> occupyList = new ArrayList<>();

		List<WellenDetail> wellenDetailList = wellenDetailService.listByBillId(billId);
		if (ObjectUtil.isEmpty(wellenDetailList)) {
			return null;
		}
		List<Wellen> wellenList = super.listByIds(NodesUtil.toList(wellenDetailList, WellenDetail::getWellenId));
		// ????????????????????????
		List<SoDetail> soDetailList = soDetailService.listByIds(
			NodesUtil.toList(wellenDetailList, WellenDetail::getSoDetailId));

		// ????????????ID???????????????????????????
		wellenDetailList.stream()
			.collect(Collectors.groupingBy(WellenDetail::getWellenId))
			.forEach((wellenId, detailList) -> {
				// ??????????????????
				Wellen wellen = wellenList.stream().filter(u -> {
					return u.getWellenId().equals(wellenId);
				}).findFirst().orElse(null);
				if (ObjectUtil.isEmpty(wellen)) {
					return;
				}
				// ????????????????????????????????????
				if (wellen.getWellenState() == WellenStateEnum.Begin.getIndex()) {
					throw new ServiceException("??????????????????????????????????????????????????????" + wellen.getWellenNo() + "??????");
				}
				// ??????????????????
				for (WellenDetail wellenDetail : detailList) {
					SoDetail soDetail = soDetailList.stream().filter(u -> {
						return u.getSoDetailId().equals(wellenDetail.getSoDetailId());
					}).findFirst().orElse(null);
					if (ObjectUtil.isEmpty(soDetail)) {
						continue;
					}
					// ???????????????????????? = ???????????????
					soDetailService.updateState(soDetail.getSoDetailId(), SoDetailStateEnum.UnAlloc);
					// ????????????
					wellenDetailService.removeById(wellenDetail);
					// ??????????????????
					StockOccupySubtractDTO stockOccupyReduceDTO = new StockOccupySubtractDTO();
					stockOccupyReduceDTO.setTransId(wellenId);
					stockOccupyReduceDTO.setOccupyType(StockOccupyTypeEnum.PickPlan.getIndex());
					stockOccupyReduceDTO.setSoBillId(billId);
					stockOccupyReduceDTO.setSoDetailId(wellenDetail.getSoDetailId());
					List<StockOccupy> stockOccupyList = stockOccupyService.subtract(stockOccupyReduceDTO);
					occupyList.addAll(stockOccupyList);
				}
				// ??????????????????????????????
				List<WellenDetail> curDetailList = wellenDetailService.list(
					Condition.getQueryWrapper(new WellenDetail()).lambda()
						.eq(WellenDetail::getWellenId, wellenId));
				if (curDetailList.size() == 0) {
					// ??????????????????
					super.removeById(wellenId);
					// ????????????
					taskService.delete(wellenId, TaskTypeEnum.Pick);
				} else {
					// ?????????????????? = ????????????
					this.updateState(wellenId, WellenStateEnum.Create);
				}
			});
		return occupyList;
	}

	@Override
	public WellenVO detail(Wellen wellen) {
		Wellen wellenGet = super.getById(wellen.getWellenId());
		if (Func.isEmpty(wellenGet)){
			throw new ServiceException("??????[ID:" + wellen.getWellenId() + "]?????????! ");
		}
		WellenVO wellenVO = SoWellenWrapper.build().entityVO(wellenGet);
		List<WellenDetail> wellenDetailList = wellenDetailService.list(Condition.getQueryWrapper(new WellenDetail())
			.lambda()
			.eq(WellenDetail::getWellenId, wellen.getWellenId()));
		if(Func.isNotEmpty(wellenVO)){
			wellenVO.setDetailList(SoWellenDetailWrapper.build().listVO(wellenDetailList));
		}
		return wellenVO;
	}
}
