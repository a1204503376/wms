
package org.nodes.wms.core.basedata.wrapper;

import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuUmCache;
import org.nodes.wms.core.basedata.dto.SkuReplaceDTO;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuReplace;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.nodes.wms.core.basedata.vo.SkuReplaceVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 物品替代包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2019-12-23
 */
public class SkuReplaceWrapper extends BaseEntityWrapper<SkuReplace, SkuReplaceVO>  {

	public static SkuReplaceWrapper build() {
		return new SkuReplaceWrapper();
 	}

	@Override
	public SkuReplaceVO entityVO(SkuReplace skuReplace) {
		SkuReplaceVO skuReplaceVO = BeanUtil.copy(skuReplace, SkuReplaceVO.class);

		// 包装名称
		SkuPackage skuPackage = SkuPackageCache.getById(skuReplaceVO.getWspId());
		if (Func.isNotEmpty(skuPackage)) {
			skuReplaceVO.setWspName(skuPackage.getWspName());
		}
		// 计量单位名称
		ISkuUmService skuUmService = SpringUtil.getBean(ISkuUmService.class);
		SkuUm skuUm = skuUmService.getById(skuReplaceVO.getWsuId());
		if (Func.isNotEmpty(skuUm)) {
			skuReplaceVO.setWsuName(skuUm.getWsuName());
		}
		// 替代物品名称
		Sku sku = SkuCache.getById(skuReplaceVO.getWsrepSkuId());
		if (Func.isNotEmpty(sku)) {
			skuReplaceVO.setWsrepSkuName(sku.getSkuName());
		}
		// 替代包装名称
		SkuPackage wsrepSkuPackage = SkuPackageCache.getById(skuReplaceVO.getWsrepWspId());
		if (Func.isNotEmpty(wsrepSkuPackage)) {
			skuReplaceVO.setWsrepWspName(wsrepSkuPackage.getWspName());
		}
		// 替代包装单位名称
		ISkuUmService skuUmService1 = SpringUtil.getBean(ISkuUmService.class);
		SkuUm wsrepSkuUm = skuUmService1.getById(skuReplaceVO.getWsrepWsuId());
		if (Func.isNotEmpty(wsrepSkuUm)){
			skuReplaceVO.setWsrepWsuName(wsrepSkuUm.getWsuName());
		}

		return skuReplaceVO;
	}

	@Override
	public List<SkuReplaceVO> listVO(List<SkuReplace> list) {
		return list.stream().map(skuReplace->{
			return entityVO(skuReplace);
		}).collect(Collectors.toList());
	}
	/**
	 * dto转实体类
	 * @param skuReplaceDTO
	 * @return
	 */
	public SkuReplace dtoToEntity(SkuReplaceDTO skuReplaceDTO) {
		return BeanUtil.copy(skuReplaceDTO, SkuReplaceDTO.class);
	}

	/**
	 * dto列表转实体类
	 * @param list
	 * @return
	 */
	public List<SkuReplace> listDTO(List<SkuReplaceDTO> list) {
		return list.stream().map(skuReplaceDTO -> dtoToEntity(skuReplaceDTO)).collect(Collectors.toList());
	}
}
