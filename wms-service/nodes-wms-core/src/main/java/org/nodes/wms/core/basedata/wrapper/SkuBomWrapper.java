package org.nodes.wms.core.basedata.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.dao.basics.bom.entites.SkuBom;
import org.nodes.wms.core.basedata.vo.SkuBomVO;


import org.springblade.core.tool.utils.Func;



/**
 * 物料清单包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-05-19
 */
public class SkuBomWrapper extends BaseEntityWrapper<SkuBom, SkuBomVO>  {

	public static SkuBomWrapper build() {
		return new SkuBomWrapper();
 	}

	@Override
	public SkuBomVO entityVO(SkuBom skuBom) {
		SkuBomVO skuBomVO = BeanUtil.copy(skuBom, SkuBomVO.class);
        if (Func.isNotEmpty(skuBomVO)) {

        }
		return skuBomVO;
	}
}
