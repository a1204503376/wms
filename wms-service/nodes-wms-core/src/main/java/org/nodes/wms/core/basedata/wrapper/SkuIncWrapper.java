
package org.nodes.wms.core.basedata.wrapper;

import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.service.IEnterpriseService;
import org.nodes.wms.core.basedata.vo.SkuIncVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 物品供应商关联包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2019-12-23
 */
public class SkuIncWrapper extends BaseEntityWrapper<SkuInc, SkuIncVO> {

	public static SkuIncWrapper build() {
		return new SkuIncWrapper();
	}

	@Override
	public SkuIncVO entityVO(SkuInc skuInc) {
		SkuIncVO skuIncVO = BeanUtil.copy(skuInc, SkuIncVO.class);
		IEnterpriseService enterpriseService = SpringUtil.getBean(IEnterpriseService.class);
		Enterprise enterprise = enterpriseService.getById(skuInc.getPeId());
		skuIncVO.setEnterpriseName(Func.isNotEmpty(enterprise) ? enterprise.getEnterpriseName() : "");
		SkuPackage skuPackage = SkuPackageCache.getById(skuInc.getWspId());
		skuIncVO.setWspName(Func.isNotEmpty(skuPackage) ? skuPackage.getWspName() : "");
		return skuIncVO;
	}
//	/**
//	 * dto转实体类
//	 * @param skuExportDTO
//	 * @return
//	 */
//	public SkuInc dtoToEntity(SkuIncDTO skuIncDTO) {
//		return BeanUtil.copy(skuIncDTO, SkuIncDTO.class);
//	}
//
//	/**
//	 * dto列表转实体类
//	 * @param list
//	 * @return
//	 */
//	public List<SkuInc> listDTO(List<SkuIncDTO> list) {
//		return list.stream().map(skuIncDTO -> dtoToEntity(skuIncDTO)).collect(Collectors.toList());
//	}
}
