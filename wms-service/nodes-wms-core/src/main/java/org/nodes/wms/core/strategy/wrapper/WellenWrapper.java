package org.nodes.wms.core.strategy.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.wms.core.strategy.cache.OutstockDetailCache;
import org.nodes.wms.core.strategy.entity.OutstockDetail;
import org.nodes.wms.core.strategy.entity.WellenDetail;
import org.nodes.wms.core.strategy.service.IWellenDetailService;
import org.nodes.wms.core.strategy.vo.WellenDetailVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.strategy.entity.Wellen;
import org.nodes.wms.core.strategy.vo.WellenVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.wms.core.strategy.service.IWellenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 波次策略包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-05-26
 */
public class WellenWrapper extends BaseEntityWrapper<Wellen, WellenVO> {

	public static WellenWrapper build() {
		return new WellenWrapper();
	}

	@Autowired
	private IWellenDetailService wellenDetailService;

	@Override
	public WellenVO entityVO(Wellen wellen) {
		WellenVO wellenVO = BeanUtil.copy(wellen, WellenVO.class);
		if (Func.isNotEmpty(wellenVO)) {
			if (Func.notNull(wellen.getId())) {
//				List<OutstockDetail> outstockDetailList = OutstockDetailCache.list(WellenDetailVO.getSsoId());
//				wellenVO.setWellenDetailVOList(wellenDetailService.list(new LambdaQueryWrapper<WellenDetail>().eq(WellenDetail::getWellenId, wellen.getId())));

			}
		}
		return wellenVO;
	}
}
