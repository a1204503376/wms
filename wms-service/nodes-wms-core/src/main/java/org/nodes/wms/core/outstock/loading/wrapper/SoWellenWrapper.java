package org.nodes.wms.core.outstock.loading.wrapper;


import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.service.IWellenService;
import org.nodes.wms.core.outstock.so.vo.WellenVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class SoWellenWrapper extends BaseEntityWrapper<Wellen, WellenVO> {

	IWellenService wellenService;

	public static SoWellenWrapper build() {
		return new SoWellenWrapper();
	}

	@Override
	public WellenVO entityVO(Wellen entity) {
		WellenVO wellenVO = BeanUtil.copy(entity,WellenVO.class);
		return wellenVO;
	}

}
