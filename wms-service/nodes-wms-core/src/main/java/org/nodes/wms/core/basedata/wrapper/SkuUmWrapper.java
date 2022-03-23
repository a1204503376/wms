package org.nodes.wms.core.basedata.wrapper;


import org.nodes.wms.core.basedata.dto.SkuUmDTO;
import org.nodes.wms.core.basedata.entity.SkuUm;
import org.nodes.wms.core.basedata.excel.SkuUmExcel;
import org.nodes.wms.core.basedata.vo.SkuUmVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * @author zhongls
 * @program计量单位封装类
 * @description将计量单位实体封装为VO
 * @create 20191205
 */
public class SkuUmWrapper extends BaseEntityWrapper<SkuUm, SkuUmVO> {
	public static SkuUmWrapper build() {
		return new SkuUmWrapper();
	}

	@Override
	public SkuUmVO entityVO(SkuUm skuUm) {
		SkuUmVO skuUmVO = BeanUtil.copy(skuUm, SkuUmVO.class);
		return skuUmVO;
	}
	public SkuUmDTO entityDTO(SkuUmExcel skuUmExcel) {
		SkuUmDTO skuUmDTO = BeanUtil.copy(skuUmExcel, SkuUmDTO.class);
		return skuUmDTO;
	}
}
