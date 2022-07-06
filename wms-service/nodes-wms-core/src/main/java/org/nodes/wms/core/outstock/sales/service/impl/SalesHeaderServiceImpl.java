package org.nodes.wms.core.outstock.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.SysCache;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.outstock.sales.dto.SalesDetailDTO;
import org.nodes.wms.core.outstock.sales.dto.SalesHeaderDTO;
import org.nodes.wms.core.outstock.sales.dto.SalesHeaderQueryDTO;
import org.nodes.wms.core.outstock.sales.entity.SalesDetail;
import org.nodes.wms.core.outstock.sales.entity.SalesHeader;
import org.nodes.wms.core.outstock.sales.mapper.SalesHeaderMapper;
import org.nodes.wms.core.outstock.sales.service.ISalesDetailService;
import org.nodes.wms.core.outstock.sales.service.ISalesHeaderService;
import org.nodes.wms.core.outstock.sales.vo.SalesHeaderVO;
import org.nodes.wms.core.outstock.sales.wrapper.SalesDetailWrapper;
import org.nodes.wms.core.outstock.sales.wrapper.SalesHeaderWrapper;
import org.nodes.wms.core.outstock.so.cache.SoCache;
import org.nodes.wms.core.outstock.so.dto.SoHeaderDTO;
import org.nodes.wms.core.outstock.so.enums.ShipStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.core.outstock.so.enums.SyncStateEnum;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.wrapper.SoHeaderWrapper;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.AuthUtil;
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
import java.util.List;

/**
 * 销售单主表
 服务实现类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SalesHeaderServiceImpl<M extends SalesHeaderMapper, T extends SalesHeader> extends BaseServiceImpl<SalesHeaderMapper, SalesHeader> implements ISalesHeaderService{

	@Autowired
	ISoHeaderService soHeaderService;
	@Autowired
	ISoDetailService soDetailService;
	@Autowired
	ISalesDetailService salesDetailService;

	@Override
	public List<SalesHeaderVO> selectList(SalesHeaderQueryDTO salesHeaderQuery) {
		List<SalesHeader> list = super.list(this.getQueryWrapper(salesHeaderQuery));
		return SalesHeaderWrapper.build().listVO(list);
	}

	@Override
	public IPage<SalesHeaderVO> selectPage(SalesHeaderQueryDTO salesHeaderQuery, Query query) {
		IPage<SalesHeader> page = super.page(Condition.getPage(query), this.getQueryWrapper(salesHeaderQuery));
		return SalesHeaderWrapper.build().pageVO(page);
	}

	@Override
	public boolean saveOrUpdate(SalesHeaderDTO salesHeader) {
		if (Func.isEmpty(salesHeader.getDetailList())) {
			throw new ServiceException("请创建销售订单明细");
		}
		//库房编码
		Warehouse warehouse = WarehouseCache.getById(salesHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在或已经删除");
		}
		salesHeader.setWhCode(warehouse.getWhCode());
		//货主编码
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(salesHeader.getWoId());
		if (Func.isEmpty(owner)) {
			throw new ServiceException("货主不存在或已经删除");
		}
		salesHeader.setOwnerCode(owner.getOwnerCode());
		//上位系统单据唯一标识
		if (Func.isEmpty(salesHeader.getBillKey())) {
			salesHeader.setBillKey(salesHeader.getSoBillNo());
		}
		//创建人姓名
		salesHeader.setBillCreator(AuthUtil.getNickName());
		//部门
		if (Func.isNotEmpty(salesHeader.getDeptId())) {
			Dept dept = SysCache.getDept(Long.valueOf(salesHeader.getDeptId()));
			if (Func.isEmpty(dept)) {
				throw new ServiceException("机构不存在或已经删除");
			}
			salesHeader.setDeptCode(dept.getDeptCode());//机构编码
			salesHeader.setDeptName(dept.getDeptName());//机构名称
		}
		if (Func.isEmpty(salesHeader.getSoBillNo())) {
			salesHeader.setSoBillNo(SoCache.getSalesBillNo());
		}
		if (Func.isEmpty(salesHeader.getSoBillState())) {
			salesHeader.setSoBillState(SoBillStateEnum.CREATE.getIndex());
		}
		if (Func.isEmpty(salesHeader.getTransportCode())) {
			salesHeader.setTransportCode(701);
		}
		if (Func.isEmpty(salesHeader.getOutstockType())) {
			salesHeader.setOutstockType(40);
		}
		salesHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		salesHeader.setShipState(ShipStateEnum.DEFAULT.getIndex());
		boolean result = super.saveOrUpdate(salesHeader);
		//无单据出库
		if (Func.isEmpty(salesHeader.getBillKey())) {
			//上位系统单据唯一标识
			salesHeader.setBillKey(Func.toStr(salesHeader.getSoBillId()));
			//上位系统最后更新时间
			salesHeader.setLastUpdateDate(LocalDateTime.now());
			//上位系统订单创建时间
			salesHeader.setPreCreateDate(LocalDateTime.now());
			//更新上位系统默认信息
			result = super.updateById(salesHeader);
		}
		// 保存明细表
		salesDetailService.remove(Condition.getQueryWrapper(new SalesDetail()).lambda()
			.eq(SalesDetail::getSoBillId, salesHeader.getSoBillId()));
		List<SalesDetailDTO> details = salesHeader.getDetailList();
		for (SalesDetailDTO detail : details) {
			detail.setSoBillId(salesHeader.getSoBillId());
			detail.setSoBillNo(salesHeader.getSoBillNo());
			detail.setBillTypeCd(salesHeader.getBillTypeCd());
			salesDetailService.saveOrUpdate(detail);
		}
		return result;
	}

	@Override
	public SalesHeaderVO getDetail(Long soBillId) {
		//查询销售单详情
		SalesHeader salesHeader = super.getById(soBillId);
		if (Func.isEmpty(salesHeader)) {
			throw new ServiceException("订单已删除或不存在");
		}
		SalesHeaderVO salesHeaderVO = SalesHeaderWrapper.build().entityVO(salesHeader);
		//查询销售单明细分页列表
		List<SalesDetail> salesDetailList = salesDetailService.list(
			Condition.getQueryWrapper(new SalesDetail()).lambda()
				.eq(SalesDetail::getSoBillId, salesHeader.getSoBillId())
				.orderByAsc(SalesDetail::getSoLineNo));
		salesHeaderVO.setDetailList(SalesDetailWrapper.build().listVO(salesDetailList));
		return salesHeaderVO;
	}

	@Override
	public boolean createSo(SalesHeaderDTO salesHeader) {
		long count = salesHeader.getDetailList().stream().filter(u -> {
			return BigDecimalUtil.gt(u.getSurplusQty(), BigDecimal.ZERO);
		}).count();
		if (count == 0L) {
			throw new ServiceException(String.format(
				"销售单[%s] 已收满，不允许再生成出库单! ", salesHeader.getSoBillNo()));
		}
		//头表
		SoHeaderDTO soHeaderDTO = SoHeaderWrapper.build().entityDTO(salesHeader);
		return soHeaderService.saveOrUpdate(soHeaderDTO);
//		//明细
//		List<SalesDetailDTO> detailList = salesHeader.getDetailList();
//		//待保存的明细集合
//		List<SoDetail> soDetailList = new ArrayList<>();
//		for (SalesDetailDTO detailDTO : detailList) {
//			SoDetail soDetail = SoDetailWrapper.build().entityDTO(detailDTO);
//			soDetail.setSoBillId(soHeaderDTO.getSoBillId());
//			soDetailList.add(soDetail);
//		}
//		soDetailService.saveBatch(soDetailList,soDetailList.size());
//		return true;
	}

	protected QueryWrapper<SalesHeader> getQueryWrapper(SalesHeaderQueryDTO salesHeaderQuery) {
		QueryWrapper<SalesHeader> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
			.func(sql -> {
				if (Func.isNotEmpty(salesHeaderQuery.getSoBillNo())) {
					sql.like(SalesHeader::getSoBillNo, salesHeaderQuery.getSoBillNo());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getOrderNo())) {
					sql.like(SalesHeader::getOrderNo, salesHeaderQuery.getOrderNo());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getCName())) {
					sql.and(c_keyword -> {
						c_keyword
							.or().like(SalesHeader::getCCode, salesHeaderQuery.getCName())
							.or().like(SalesHeader::getCName, salesHeaderQuery.getCName());
					});
				}
				if (Func.isNotEmpty(salesHeaderQuery.getBillTypeCd())) {
					sql.eq(SalesHeader::getBillTypeCd, salesHeaderQuery.getBillTypeCd());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getSoBillState())) {
					sql.eq(SalesHeader::getSoBillState, salesHeaderQuery.getSoBillState());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getSyncState())) {
					sql.eq(SalesHeader::getSyncState, salesHeaderQuery.getSyncState());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getPostState())) {
					sql.eq(SalesHeader::getPostState, salesHeaderQuery.getPostState());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getPostTime())) {
					sql.apply("DATE_FORMAT(post_time, '%Y-%m-%d') = DATE_FORMAT({0}, '%Y-%m-%d')",
						salesHeaderQuery.getPostTime());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getPostUser())) {
					sql.eq(SalesHeader::getPostUser, salesHeaderQuery.getPostUser());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getWoId())) {
					sql.eq(SalesHeader::getWoId, salesHeaderQuery.getWoId());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getWhId())) {
					sql.eq(SalesHeader::getWhId, salesHeaderQuery.getWhId());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getDeptId())) {
					sql.eq(SalesHeader::getDeptId, salesHeaderQuery.getDeptId());
				}
				if (Func.isNotEmpty(salesHeaderQuery.getExpressName())) {
					sql.like(SalesHeader::getExpressName, salesHeaderQuery.getExpressName());
				}
			});
		return queryWrapper;
	}
}
