package org.nodes.wms.core.basedata.wrapper;


import org.nodes.wms.core.basedata.dto.SkuLotValDTO;
import org.nodes.wms.core.basedata.entity.Owner;
import org.nodes.wms.core.basedata.entity.SkuLotVal;
import org.nodes.wms.core.basedata.excel.SkuLotValExcel;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.vo.SkuLotValVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * @author zl
 * @program 批属性验证封装类
 * @description 将批属性验证实体封装为Vo
 * @create 20191205
 */
public class SkuLotValWrapper extends BaseEntityWrapper<SkuLotVal, SkuLotValVO> {

	/**
	 * 批属性验证封装
	 */
	public static SkuLotValWrapper build() {
		return new SkuLotValWrapper();
	}

	@Override
	public SkuLotValVO entityVO(SkuLotVal entity) {
		SkuLotValVO skuLotValVo = BeanUtil.copy(entity, SkuLotValVO.class);
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
//		String skuLotMust1 = DictCache.getValue("sku_lot_must", skuLotValVo.getSkuLotMust1());
//		skuLotValVo.setSkuLotMust1();
		if (Func.isNotEmpty(skuLotValVo)){
			Owner owner = ownerService.getById(skuLotValVo.getWoId());
			if(Func.isNotEmpty(owner)) {
				skuLotValVo.setOwnerName(owner.getOwnerName());
			}
			Warehouse warehouse = WarehouseCache.getById(skuLotValVo.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				skuLotValVo.setWhName(warehouse.getWhName());
			}
		}
		return skuLotValVo;
	}

	public SkuLotValDTO entityDTO(SkuLotValExcel skuLotValExcel) {
		SkuLotValDTO skuLotValDTO = BeanUtil.copy(skuLotValExcel, SkuLotValDTO.class);
		return skuLotValDTO;
	}
}
