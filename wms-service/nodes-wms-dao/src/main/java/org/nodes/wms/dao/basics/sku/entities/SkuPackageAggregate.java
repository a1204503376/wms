package org.nodes.wms.dao.basics.sku.entities;

import lombok.Data;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;

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

	/**
	 * 根据计量单位编码获取包装明细
	 * @param umCode 计量单位编码
	 * @return SkuPackageDetail
	 */
	public SkuPackageDetail findSkuPackageDetail(String umCode){
		if (Func.isEmpty(skuPackageDetailList)){
			throw new ServiceException("没有找到包装信息，调用前先执行根据物料查找包装聚合的方法");
		}
		for (SkuPackageDetail skuPackageDetail : skuPackageDetailList) {
			if (skuPackageDetail.getWsuCode().equals(umCode)) {
				return skuPackageDetail;
			}
		}
		throw new ServiceException("没有找到包装信息，该物料没有计量单位为"+umCode+ "的包装信息");

	}

	/**
	 * 获取基础包装明细
	 * @return SkuPackageDetail
	 */
	public SkuPackageDetail findBaseSkuPackageDetail(){
		for (SkuPackageDetail skuPackageDetail : skuPackageDetailList) {
			if (skuPackageDetail.getSkuLevel()==1) {
				return skuPackageDetail;
			}
		}
		throw new ServiceException("没有找到包装信息，该物料没有基础计量单位的包装信息");
	}
}
