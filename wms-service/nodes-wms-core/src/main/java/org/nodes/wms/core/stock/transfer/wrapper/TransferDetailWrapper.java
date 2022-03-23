package org.nodes.wms.core.stock.transfer.wrapper;

import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.stock.transfer.entity.TransferDetail;
import org.nodes.wms.core.stock.transfer.vo.TransferDetailVO;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;


/**
 * 库内移动明细包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-08-03
 */
public class TransferDetailWrapper extends BaseEntityWrapper<TransferDetail, TransferDetailVO>  {

	public static TransferDetailWrapper build() {
		return new TransferDetailWrapper();
 	}

	@Override
	public TransferDetailVO entityVO(TransferDetail transferDetail) {
		TransferDetailVO transferDetailVO = BeanUtil.copy(transferDetail, TransferDetailVO.class);

		if (Func.isNotEmpty(transferDetailVO)) {
			Sku sku = SkuCache.getById(transferDetail.getSkuId());
			if (Func.isNotEmpty(sku)) {
				transferDetailVO.setSkuCode(sku.getSkuCode());
				transferDetailVO.setSkuName(sku.getSkuName());
			}
			SkuPackage skuPackage = SkuPackageCache.getById(transferDetail.getWspId());
			if (Func.isNotEmpty(skuPackage)) {
				transferDetailVO.setWspName(skuPackage.getWspName());
			}
			Location sourceLocation = LocationCache.getById(transferDetail.getSourceLocId());
			if (Func.isNotEmpty(sourceLocation)) {
				transferDetailVO.setSourceLocCode(sourceLocation.getLocCode());
			}
			Location targetLocation = LocationCache.getById(transferDetail.getTargetLocId());
			if (Func.isNotEmpty(targetLocation)) {
				transferDetailVO.setTargetLocCode(targetLocation.getLocCode());
			}
		}
		return transferDetailVO;
	}
}
