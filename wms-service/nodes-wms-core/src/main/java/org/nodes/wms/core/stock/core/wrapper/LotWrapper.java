
package org.nodes.wms.core.stock.core.wrapper;

import org.nodes.core.base.cache.ParamCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.stock.core.dto.LotDTO;
import org.nodes.wms.core.stock.core.dto.StockAddDTO;
import org.nodes.wms.core.stock.core.entity.Lot;
import org.nodes.wms.core.stock.core.vo.LotVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;

/**
 * 批次号包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2019-12-25
 */
public class LotWrapper extends BaseEntityWrapper<Lot, LotVO> {

	public static LotWrapper build() {
		return new LotWrapper();
	}

	@Override
	public LotVO entityVO(Lot lot) {
		LotVO lotVO = BeanUtil.copy(lot, LotVO.class);

		if (Func.isNotEmpty(lotVO)) {
			Warehouse warehouse = WarehouseCache.getById(lot.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				lotVO.setWhName(warehouse.getWhName());
			}
		}

		return lotVO;
	}

	/**
	 * 将 SkuLot, Sku, lotNumber 对象转换为 LotDTO
	 *
	 * @param sku       	物品信息
	 * @param stockAdd 	请求参数
	 * @return 批次号对象
	 */
	public LotDTO entityDTO(Sku sku, StockAddDTO stockAdd) {
		LotDTO lotDTO = new LotDTO();
		Location location = LocationCache.getById(stockAdd.getLocId());
		if (Func.isNotEmpty(location)) {
			lotDTO.setWhId(location.getWhId());
		}
		lotDTO.setLotNumber(stockAdd.getLotNumber());
		lotDTO.setCreateUser(AuthUtil.getUserId());
		lotDTO.setSkuId(sku.getSkuId());
		lotDTO.setSkuCode(sku.getSkuCode());
		lotDTO.setSkuName(sku.getSkuName());
		lotDTO.setSystemProcId(stockAdd.getSystemProcId());

		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (!stockAdd.skuLotChk(i)) {
				lotDTO.skuLotSet(i, StringPool.SPACE);
			} else {
				lotDTO.skuLotSet(i, stockAdd.skuLotGet(i));
			}
		}
		return lotDTO;
	}

	/**
	 * 将 Lot 转换为 LotDTO
	 * @param lot 待转换的 Lot 对象
	 * @return 转换后得 LotDTO 对象
	 */
	public LotDTO entityDTO(Lot lot) {
		return BeanUtil.copy(lot, LotDTO.class);
	}
}
