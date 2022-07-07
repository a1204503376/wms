package org.nodes.wms.core.stock.core.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.stock.core.entity.LogStockPack;
import org.nodes.wms.core.stock.core.vo.LogStockPackVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * @description 尾箱打包日志封装类
 *
 * @author pengwei
 * @since 2020-07-13
 */
public class LogStockPackWrapper extends BaseEntityWrapper<LogStockPack, LogStockPackVO> {

	public static LogStockPackWrapper build(){
		return new LogStockPackWrapper();
	}

	@Override
	public LogStockPackVO entityVO(LogStockPack entity) {
		LogStockPackVO logStockPack = BeanUtil.copy(entity, LogStockPackVO.class);
		if (Func.isNotEmpty(logStockPack)) {
			//库房
			Warehouse warehouse = WarehouseCache.getById(logStockPack.getWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				logStockPack.setWhName(warehouse.getWhName());
			}
            //库区
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone zone = zoneService.getById(logStockPack.getZoneId());
			if (ObjectUtil.isNotEmpty(zone)) {
				logStockPack.setZoneName(zone.getZoneName());
			}
            //库位
			Location location = LocationCache.getById(logStockPack.getLocId());
			if (ObjectUtil.isNotEmpty(location)) {
				logStockPack.setLocCode(location.getLocCode());
			}
			//计量单位名称
			SkuPackageDetail skuPackageDetail= SkuPackageDetailCache.getOne(
				logStockPack.getWspId(), logStockPack.getSkuLevel());
			if (ObjectUtil.isNotEmpty(skuPackageDetail)){
				logStockPack.setWsuName(skuPackageDetail.getWsuName());
			}
            //包装名称
			SkuPackage skuPackage= SkuPackageCache.getById(logStockPack.getWspId());
			if (ObjectUtil.isNotEmpty(skuPackage)){
				logStockPack.setWspName(skuPackage.getWspName());
			}
			// 操作人员
			User user = UserCache.getById(entity.getProcUser());
			if (Func.isNotEmpty(user)) {
				logStockPack.setProcUserName(user.getRealName());
			}
			logStockPack.setProcTypeDesc(DictCache.getValue(DictCodeConstant.STOCK_PACK_STATE,logStockPack.getProcType()));
		}
		return logStockPack;
	}
}
