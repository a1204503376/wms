package org.nodes.wms.dao.basics.sku.entities;

import lombok.Data;

/**
 * 包装与明细聚合实体类
 **/
@Data
public class SkuPackageAggregate {

	SkuPackage skuPackage;

	SkuPackageDetail[] skuPackageDetails;
}
