package org.nodes.wms.core.count.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.count.dto.CountRecordDTO;
import org.nodes.wms.core.count.entity.CountRecord;
import org.nodes.wms.core.count.vo.CountRecordVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pengwei
 * @program WmsCore
 * @description 盘点记录封装类
 * @create 20200228
 */
public class CountRecordWrapper extends BaseEntityWrapper<CountRecord, CountRecordVO> {

	public static CountRecordWrapper build() {
		return new CountRecordWrapper();
	}

	public CountRecordDTO entityDTO(CountRecord countRecord) {
		return BeanUtil.copy(countRecord, CountRecordDTO.class);
	}

	public List<CountRecordVO> listVO(List<CountRecord> list) {
		return list.stream().map(countRecord -> {
			return entityVO(countRecord);
		}).collect(Collectors.toList());
	}


	@Override
	public CountRecordVO entityVO(CountRecord entity) {
		CountRecordVO countRecordVO = BeanUtil.copy(entity, CountRecordVO.class);
		if (ObjectUtil.isNotEmpty(countRecordVO)) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			countRecordVO.setProcTimeDesc(dateTimeFormatter.format(countRecordVO.getProcTime()));
		}
		countRecordVO.setSkuLevelDesc(DictCache.getValue(DictConstant.SKU_LEVEL, entity.getSkuLevel()));
		countRecordVO.setRecordStateDesc(DictCache.getValue("count_record_status", countRecordVO.getRecordState()));

		if (Func.isNotEmpty(countRecordVO.getWhId())) {
			Warehouse warehouse = WarehouseCache.getById(countRecordVO.getWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				countRecordVO.setWhName(warehouse.getWhName());
			}
		}
		SkuPackage skuPackage = SkuPackageCache.getById(countRecordVO.getWspId());
		if (Func.isNotEmpty(skuPackage)) {
			countRecordVO.setWspName(skuPackage.getWspName());

		}
		return countRecordVO;
	}
}
