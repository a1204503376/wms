package org.nodes.modules.wms.basedata.service;

import org.nodes.wms.core.basedata.dto.SkuDTO;
import org.nodes.wms.core.basedata.entity.Sku;

public interface ISkuService extends org.nodes.wms.core.basedata.service.ISkuService {
	boolean editValid(Long skuId);
}
