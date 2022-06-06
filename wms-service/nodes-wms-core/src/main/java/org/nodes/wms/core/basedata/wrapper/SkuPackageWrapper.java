package org.nodes.wms.core.basedata.wrapper;

import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.dto.SkuPackageDTO;
import org.nodes.wms.core.basedata.dto.SkuPackageDetailDTO;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.excel.SkuPackageExcel;
import org.nodes.wms.core.basedata.vo.SkuPackageDetailVO;
import org.nodes.wms.core.basedata.vo.SkuPackageVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pengwei
 * @program WmsCore
 * @description SkuPackage封装VO类
 * @create 20191211
 */
public class SkuPackageWrapper extends BaseEntityWrapper<SkuPackage, SkuPackageVO> {

	public static SkuPackageWrapper build() {
		return new SkuPackageWrapper();
	}

	@Override
	public SkuPackageVO entityVO(SkuPackage entity) {
		if (Func.isEmpty(entity)) {
			return null;
		}
		SkuPackageVO skuPackageVO = BeanUtil.copy(entity, SkuPackageVO.class);
		if (Func.isNotEmpty(skuPackageVO)) {
			List<SkuPackageDetail> list = SkuPackageDetailCache.listByWspId(skuPackageVO.getWspId());
			if (Func.isNotEmpty(list) && list.size() > 0) {
				List<SkuPackageDetailVO> listVO = list.stream().map(item -> {
					SkuPackageDetailVO skuPackageDetailVO = SkuPackageDetailWrapper.build().entityVO(item);
					return skuPackageDetailVO;
				}).collect(Collectors.toList());
				if (Func.isNotEmpty(listVO) && listVO.size() > 0)
					skuPackageVO.setSkuPackageDetailVOList(listVO);
			}
		}

		return skuPackageVO;
	}
	/**
	 * 实体转导出DTO
	 * @param skuPackage
	 * @return
	 */
	public SkuPackageExcel entityToExportDTO(SkuPackage skuPackage) {
		return BeanUtil.copy(skuPackage, SkuPackageExcel.class);
	}

	public SkuPackageDTO entityDTO(SkuPackageExcel skuPackageExcel) {
		SkuPackageDTO skuPackageDTO = BeanUtil.copy(skuPackageExcel, SkuPackageDTO.class);
		skuPackageDTO.setSkuPackageDetailDTOList(new ArrayList<>());
		if (Func.isNotEmpty(skuPackageDTO)) {
			// 地址
			SkuPackageDetailDTO skuPackageDetailDTO = BeanUtil.copy(skuPackageExcel, SkuPackageDetailDTO.class);
			if (Func.isNotEmpty(skuPackageDetailDTO)) {
				if("基础计量单位".equals(skuPackageExcel.getSkuLevelName())){
					skuPackageDetailDTO.setSkuLevel(SkuLevelEnum.Base.getIndex());
				}else if("内包装".equals(skuPackageExcel.getSkuLevelName())){
					skuPackageDetailDTO.setSkuLevel(SkuLevelEnum.Inner.getIndex());
				}else if("箱".equals(skuPackageExcel.getSkuLevelName())){
					skuPackageDetailDTO.setSkuLevel(SkuLevelEnum.Box.getIndex());
				}else if("托盘".equals(skuPackageExcel.getSkuLevelName())){
					skuPackageDetailDTO.setSkuLevel(SkuLevelEnum.Plate.getIndex());
				}

				skuPackageDetailDTO.setLpnHeight(skuPackageExcel.getLpnHeight1());
				skuPackageDetailDTO.setLpnLength(skuPackageExcel.getLpnLength1());
				skuPackageDetailDTO.setLpnWeight(skuPackageExcel.getLpnWeight1());
				skuPackageDetailDTO.setLpnWidth(skuPackageExcel.getLpnWidth1());
				skuPackageDTO.getSkuPackageDetailDTOList().add(skuPackageDetailDTO);
			}
		}
		return skuPackageDTO;
	}
}
