package org.nodes.wms.core.strategy.instock.impl;

import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.basedata.service.ISkuInstockService;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.putway.entities.Instock;
import org.nodes.wms.dao.putway.entities.InstockDetail;
import org.nodes.wms.core.strategy.instock.FunctionCodeBase;
import org.nodes.wms.core.strategy.instock.IFunctionCode;
import org.nodes.wms.core.strategy.service.IInstockService;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;

/**
 * author: pengwei
 * date: 2021/5/26 10:55
 * description: LocationFunctionCode
 */
@Primary
public class LocationFunctionCode extends FunctionCodeBase implements IFunctionCode {
	@Override
	public void execute(Stock stock, InstockDetail instockDetail, InstockExecuteVO instockExecute) {
		IInstockService instockService = SpringUtil.getBean(IInstockService.class);
		Instock instock = instockService.getById(instockDetail.getSsiId());
		Zone zone = null;
		Location location = null;
		// 找出物品的入库设置
		ISkuInstockService skuInstockService = SpringUtil.getBean(ISkuInstockService.class);
		//SkuInstock skuInstock = SkuInstockCache.getOne(stock.getSkuId(), stock.getWhId());
		SkuInstock skuInstock = skuInstockService.list(Condition.getQueryWrapper(new SkuInstock())
		.lambda()
		.eq(SkuInstock::getSkuId,stock.getSkuId())
		.eq(SkuInstock::getWhId,stock.getWhId())
		).stream().findFirst().orElse(null);
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		if (Func.isEmpty(skuInstock)) {
			if (Func.isEmpty(instockDetail.getToZoneId()) && Func.isEmpty(instockDetail.getToLocId())) {
				throw new ServiceException("上架策略中缺少库区或者库位！");
			}
			zone = zoneService.getById(instockDetail.getToZoneId());
			location = LocationCache.getById(instockDetail.getToLocId());
		} else {
			zone = zoneService.getById(instockDetail.getToZoneId());
			location = LocationCache.getById(instockDetail.getToLocId());
		}
		if (Func.isEmpty(zone) || Func.isEmpty(location)) {
			throw new ServiceException("物品入库设置中缺少库区或者库位！");
		}
		String msg = String.format("上架策略[%s]的第[%s]个策略执行成功,推荐上架到库区[%s]的库位[%s]",
			instock.getSsiName(),
			instockDetail.getSsidProcOrder(),
			Func.isNotEmpty(zone) ? zone.getZoneName() : StringPool.EMPTY,
			Func.isNotEmpty(location) ? location.getLocCode() : StringPool.EMPTY);
		instockExecute.setMsg(msg);
		if (Func.isNotEmpty(zone)) {
			instockExecute.setZoneCode(zone.getZoneCode());
			instockExecute.setZoneName(zone.getZoneName());
		}
		if (Func.isNotEmpty(location)) {
			instockExecute.setTargetLocId(location.getLocId());
			instockExecute.setTargetLocCode(location.getLocCode());
		}
	}
}
