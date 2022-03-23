package org.nodes.wms.core.relenishment.service;

import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;

import java.util.List;

public interface IFunctionCode {

	List<RelDetail> execute(Stock stock, SkuInstock skuInstock, RelenishmentDetail relenishmentDetail);
}
