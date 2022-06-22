package org.nodes.wms.core.relenishment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.basedata.service.ISkuInstockService;
import org.nodes.wms.core.relenishment.dto.RelDetailDTO;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.relenishment.entity.RelHeader;
import org.nodes.wms.core.relenishment.mapper.RelDetailMapper;
import org.nodes.wms.core.relenishment.service.IRelDetailService;
import org.nodes.wms.core.relenishment.vo.RelDetailVo;
import org.nodes.wms.core.relenishment.wrapper.RelDetailWrapper;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.strategy.entity.Relenishment;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;
import org.nodes.wms.core.strategy.service.IRelenishmentDetailService;
import org.nodes.wms.core.strategy.service.IRelenishmentService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Primary
@Service
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class RelDetailServiceImpl<M extends RelDetailMapper, T extends RelDetail>
	extends BaseServiceImpl<RelDetailMapper, RelDetail>
	implements IRelDetailService {
	@Autowired
	private IStockService stockService;
	@Autowired
	ISkuInstockService skuInstockService;

	@Override
	public IPage<RelDetailVo> selectPage(RelDetailDTO relDetailDTO, Query query) {
		IPage<RelDetail> page = super.page(Condition.getPage(query), this.getQueryWrapper(relDetailDTO));
		IPage<RelDetailVo> data = RelDetailWrapper.build().pageVO(page);
		return data;
	}



	@Override
	public void createRelenishDetail(String stockIds, RelHeader relHeader) {
		IRelenishmentService relenishmentService = SpringUtil.getBean(IRelenishmentService.class);
		IRelenishmentDetailService relenishmentDetailService = SpringUtil.getBean(IRelenishmentDetailService.class);
		List<Long> stockIdList = Func.toLongList(stockIds);
		List<Stock> stocks = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, stockIdList));
		if(Func.isEmpty(stocks)){
			throw new ServiceException("库存不存在！");
		}
		for(Stock stock:stocks){
			SkuInstock skuInstock = skuInstockService.getOne(Wrappers.lambdaQuery(SkuInstock.class)
				.eq(SkuInstock::getWhId, stock.getWhId())
				.eq(SkuInstock::getSkuId, stock.getSkuId())
			);
			//List<Relenishment> relenishments = RelenishmentCache.listByWhId(stocks.get(0).getWhId());
			List<Relenishment> relenishments = relenishmentService.list(Condition.getQueryWrapper(new Relenishment())
			.lambda()
			.eq(Relenishment::getWhId,stocks.get(0).getWhId())
			);
			if(Func.isNotEmpty(relenishments)){
				Relenishment relenishment = relenishments.get(0);
				//List<RelenishmentDetail> relenishmentDetails = RelenishmentDetailCache.list(relenishment.getSsiId());
				List<RelenishmentDetail> relenishmentDetails = relenishmentDetailService.list(Condition.getQueryWrapper(new RelenishmentDetail())
				.lambda()
				.eq(RelenishmentDetail::getSsiId,relenishment.getSsiId())
				);
				List<RelenishmentDetail> collect1 = relenishmentDetails.stream()
					.sorted(Comparator.comparing(RelenishmentDetail::getSsidProcOrder)).collect(Collectors.toList());
				for(RelenishmentDetail relenishmentDetail:collect1){
					if(!stock.getZoneId().equals(relenishmentDetail.getToZoneId())){
						continue;
					}
					List<RelDetail> relDetails = RelenishmentFactory.getInstance().create(relenishmentDetail.getSkuLevel())
						.execute(stock, skuInstock, relenishmentDetail);
					relDetails.forEach(relDetail -> {
						relDetail.setRelBillId(relHeader.getRelBillId());
						relDetail.setRelBillNo(relHeader.getRelBillNo());
					});
					super.saveBatch(relDetails);
				}
			}
		}
	}
	@Override
	public void createRelenishDetail1(String stockIds, RelHeader relHeader) {
		IRelenishmentService relenishmentService = SpringUtil.getBean(IRelenishmentService.class);
		IRelenishmentDetailService relenishmentDetailService = SpringUtil.getBean(IRelenishmentDetailService.class);
		List<Long> stockIdList = Func.toLongList(stockIds);
		List<Stock> stocks = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, stockIdList));
		if(Func.isEmpty(stocks)){
			throw new ServiceException("库存不存在！");
		}
		for(Stock stock:stocks){
			SkuInstock skuInstock = skuInstockService.getOne(Wrappers.lambdaQuery(SkuInstock.class)
				.eq(SkuInstock::getWhId, stock.getWhId())
				.eq(SkuInstock::getSkuId, stock.getSkuId())
			);
			//List<Relenishment> relenishments = RelenishmentCache.listByWhId(stocks.get(0).getWhId());
			List<Relenishment> relenishments = relenishmentService.list(Condition.getQueryWrapper(new Relenishment())
			.lambda()
			.eq(Relenishment::getWhId,stocks.get(0).getWhId())
			);
			if(Func.isNotEmpty(relenishments)){
				Relenishment relenishment = relenishments.get(0);
				//List<RelenishmentDetail> relenishmentDetails = RelenishmentDetailCache.list(relenishment.getSsiId());
				List<RelenishmentDetail> relenishmentDetails = relenishmentDetailService.list(Condition.getQueryWrapper(new RelenishmentDetail())
				.lambda()
				.eq(RelenishmentDetail::getSsiId,relenishment.getSsiId())
				);
				List<RelenishmentDetail> collect1 = relenishmentDetails.stream()
					.filter(relenishmentDetail -> relenishmentDetail.getToZoneId().equals(stock.getZoneId()))
					.sorted(Comparator.comparing(RelenishmentDetail::getSsidProcOrder))
					.collect(Collectors.collectingAndThen(Collectors.toCollection(
						() -> new TreeSet<>(Comparator.comparing(RelenishmentDetail :: getFromZoneId))), ArrayList::new));
				List<RelDetail> relDetails = RelenishmentFactory.getInstance()
					.create1(stock, skuInstock, NodesUtil.toList(collect1, RelenishmentDetail::getFromZoneId));
					relDetails.forEach(relDetail -> {
						relDetail.setRelBillId(relHeader.getRelBillId());
						relDetail.setRelBillNo(relHeader.getRelBillNo());
					});
					super.saveBatch(relDetails);
			}
		}
	}
	private QueryWrapper<RelDetail> getQueryWrapper(RelDetailDTO relDetailDTO) {

		return null;
	}
}
