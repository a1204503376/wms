package org.nodes.wms.dao.basics.sku.entities;

import lombok.Data;

import java.util.List;

/**
 * 包装与明细聚合实体类
 **/
@Data
public class SkuPackageAggregate {

	/**
	 * 映射时唯一id
	 */
	private Long id;

	/**
	 * 包装对象
	 */
	private SkuPackage skuPackage;

	/**
	 * 包装明细对象集合
	 */
	private List<SkuPackageDetail> skuPackageDetailList;
}
