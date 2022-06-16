package org.nodes.wms.core.stock.core.wrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.stock.core.entity.StockLog;
import org.nodes.wms.core.stock.core.vo.StockLogVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 库存日志包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-02-14
 */
public class StockLogWrapper extends BaseEntityWrapper<StockLog, StockLogVO> {

	public static StockLogWrapper build() {
		return new StockLogWrapper();
	}

	@Override
	public StockLogVO entityVO(StockLog stockLog) {
		StockLogVO stockLogVO = BeanUtil.copy(stockLog, StockLogVO.class);

		if (ObjectUtil.isNotEmpty(stockLogVO)) {
			// 获取库区
			Location location = LocationCache.getById(stockLog.getLocId());
			if (ObjectUtil.isNotEmpty(location)) {
				IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
				Zone zone = zoneService.getById(location.getZoneId());
				if (ObjectUtil.isNotEmpty(zone)) {
					stockLogVO.setZoneName(zone.getZoneName());
				}
			}
			// 货主
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(stockLog.getWoId());
			if (ObjectUtil.isNotEmpty(owner)) {
				stockLogVO.setOwnerName(owner.getOwnerName());
			}
			// 计量单位
			SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(stockLog.getWspId(), stockLog.getSkuLevel());
			if (ObjectUtil.isNotEmpty(packageDetail)) {
				stockLogVO.setWsuName(packageDetail.getWsuName());
			}
		}

		return stockLogVO;
	}

	/**
	 * Page转VOPage
	 *
	 * @param page
	 * @return
	 */
	public static IPage<StockLogVO> toStockLogVOPages(IPage<StockLog> page) {
		return page.convert(stockLog -> {
			StockLogVO stockLogVO = StockLogWrapper.build().entityVO(stockLog);
			return StockLogWrapper.translateStockLogVO(stockLogVO);
		});
	}

	/**
	 * 编码转文字
	 *
	 * @param stockLogVO
	 * @return
	 */
	public static StockLogVO translateStockLogVO(StockLogVO stockLogVO) {
		Long userId = stockLogVO.getUpdateUser();
		stockLogVO.setProTypeName(DictCache.getValue("pro_type", stockLogVO.getProType()));
		if (ObjectUtil.isNotEmpty(userId)) {
			User user = UserCache.getById(userId);
			if (ObjectUtil.isNotEmpty(user)) {
				stockLogVO.setUpdateUserName(user.getName());
			}
		}
		return stockLogVO;
	}
}
