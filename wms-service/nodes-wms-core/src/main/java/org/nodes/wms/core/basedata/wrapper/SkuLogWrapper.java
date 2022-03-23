package org.nodes.wms.core.basedata.wrapper;

import org.nodes.wms.core.basedata.entity.SkuLog;
import org.nodes.wms.core.basedata.vo.SkuLogVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;



/**
 * 物品操作记录表包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-06-29
 */
public class SkuLogWrapper extends BaseEntityWrapper<SkuLog, SkuLogVO> {

	public static SkuLogWrapper build() {
		return new SkuLogWrapper();
 	}

	@Override
	public SkuLogVO entityVO(SkuLog skuLog) {
		SkuLogVO skuLogVO = BeanUtil.copy(skuLog, SkuLogVO.class);

		return skuLogVO;
	}
}
