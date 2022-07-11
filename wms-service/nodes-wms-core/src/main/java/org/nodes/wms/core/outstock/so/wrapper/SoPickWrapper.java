package org.nodes.wms.core.outstock.so.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
import org.nodes.wms.core.outstock.so.dto.PickPlanDTO;
import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.nodes.wms.core.outstock.so.vo.PickLogVO;
import org.nodes.wms.core.outstock.so.vo.SoPickVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.Map;

/**
 * 拣货记录封装类
 *
 * @Author zx
 * @Date 2020/3/9
 **/
public class SoPickWrapper extends BaseEntityWrapper<SoPick, SoPickVO> {

	public static SoPickWrapper build() {
		return new SoPickWrapper();
	}

	@Override
	public SoPickVO entityVO(SoPick soPick) {
		SoPickVO soPickVO = BeanUtil.copy(soPick, SoPickVO.class);

		Warehouse warehouse = WarehouseCache.getById(soPick.getWhId());
		if (warehouse != null) {
			soPickVO.setWhName(warehouse.getWhName());
		}

		Long wspId = soPick.getWspId();
		if (wspId != null) {
			SkuPackage skuPackage = SkuPackageCache.getById(wspId);
			if (skuPackage != null) {
				soPickVO.setWspName(skuPackage.getWspName());
			}
		}
		soPickVO.setSkuLevelName(DictCache.getValue(DictCodeConstant.SKU_LEVEL, soPick.getSkuLevel()));

		return soPickVO;
	}

	/**
	 * 转拣货日志VO列表
	 */
	public static PickLogVO soPickList2PickLogVO(SoPick soPick, PickPlanDTO pickPlanDTO) {
		PickLogVO pickLogVO = new PickLogVO();
		pickLogVO.setPickPlanId(soPick.getPickPlanId());
		pickLogVO.setPlanCountQty(pickPlanDTO.getPickPlanQty());
		pickLogVO.setRealCountQty(soPick.getPickRealQty());
		pickLogVO.setSkuName(soPick.getSkuName());
		pickLogVO.setSkuCode(soPick.getSkuCode());
		try {
			ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
			pickLogVO.setSkuLotValue(skuLotService.getById(SkuCache.getById(soPick.getSkuId()).getWslId()));
			Map<String, Object> skuLotMap = SkuLotWrapper.skuLotToMapWithStock(soPick);
			SkuLotBaseEntity skuLotDTO = new SkuLotBaseEntity();
			if (Func.isNotEmpty(skuLotMap)) {
				for (String s : skuLotMap.keySet()) {
					String t = s.substring(s.lastIndexOf("t") + 1, s.length());
					skuLotDTO.skuLotSet(Integer.parseInt(t), skuLotMap.get(s).toString());
				}
			}
			pickLogVO.setSkuLot(skuLotDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pickLogVO.setSourceLocCode(soPick.getLocCode());
		pickLogVO.setSourceLpnCode(soPick.getLpnCode());

		pickLogVO.setIsSn(SkuCache.getById(soPick.getSkuId()).getIsSn());
//			pickLogVO.setSerialList();
//		pickLogVO.setUserName(soPick.getUserName());

		return pickLogVO;
	}
}
