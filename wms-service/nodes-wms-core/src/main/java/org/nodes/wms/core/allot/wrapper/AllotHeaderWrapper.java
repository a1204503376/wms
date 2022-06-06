package org.nodes.wms.core.allot.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.allot.entity.AllotHeader;
import org.nodes.wms.core.allot.vo.AllotHeaderVO;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

/**
 * 调拨单头表 封装类
 *
 * @Author Wjw
 * @Date 2020/2/18
 **/
public class AllotHeaderWrapper extends BaseEntityWrapper<AllotHeader, AllotHeaderVO> {
	public static AllotHeaderWrapper build() {
		return new AllotHeaderWrapper();
	}

	/**
	 * 调拨单头表实体类转VO
	 */
	@Override
	public AllotHeaderVO entityVO(AllotHeader entity) {
		AllotHeaderVO allotHeaderVO = BeanUtil.copy(entity, AllotHeaderVO.class);
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		//单据状态名称
		allotHeaderVO.setAllotBillStateName(DictCache.getValue(DictConstant.ALLOT_BILL_STATE, allotHeaderVO.getAllotBillState()));
		//货主名称
		if (Func.isNotEmpty(allotHeaderVO.getWoId())) {
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(allotHeaderVO.getWoId());
			if (Func.isNotEmpty(owner) && Func.isNotEmpty(owner.getOwnerName())) {
				allotHeaderVO.setOwnerName(owner.getOwnerName());
			}
		}
		//调出库房名称
		if (Func.isNotEmpty(allotHeaderVO.getSourceWhId())) {
			Warehouse sourceWarehouse = WarehouseCache.getById(allotHeaderVO.getSourceWhId());
			allotHeaderVO.setSourceWhName(sourceWarehouse.getWhName());
		}
		//调入库房名称
		if (Func.isNotEmpty(allotHeaderVO.getTargetWhId())) {
			Warehouse targetWarehouse = WarehouseCache.getById(allotHeaderVO.getTargetWhId());
			allotHeaderVO.setTargetWhName(targetWarehouse.getWhName());
		}
		//调出库房类型
		if (Func.isNotEmpty(allotHeaderVO.getSourceZoneType())) {
			Zone zone = zoneService.list(Condition.getQueryWrapper(new Zone())
				.lambda()
				.eq(Zone::getZoneType, allotHeaderVO.getSourceZoneType())
			).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(zone)) {
				allotHeaderVO.setZoneName(zone.getZoneName());
			}
		}
		return allotHeaderVO;
	}
}
