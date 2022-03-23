package org.nodes.wms.core.outstock.loading.wrapper;

import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.service.IWellenService;
import org.nodes.wms.core.outstock.so.vo.WellenDetailVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

public class SoWellenDetailWrapper extends BaseEntityWrapper<WellenDetail, WellenDetailVO> {

	public static SoWellenDetailWrapper build() {
		return new SoWellenDetailWrapper();
	}

	@Override
	public WellenDetailVO entityVO(WellenDetail entity) {
		WellenDetailVO wellenDetailVO = BeanUtil.copy(entity, WellenDetailVO.class);
		ISoHeaderService soHeaderService = SpringUtil.getBean(ISoHeaderService.class);
		ISoDetailService soDetailService = SpringUtil.getBean(ISoDetailService.class);
		IWellenService wellenService = SpringUtil.getBean(IWellenService.class);
		if (Func.isNotEmpty(wellenDetailVO.getSoBillId())) {
			SoHeader soHeader = soHeaderService.getById(wellenDetailVO.getSoBillId());
			if (Func.isNotEmpty(soHeader)) {
				wellenDetailVO.setSoHeader(soHeader);
			}
			SoDetail soDetail = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
				.lambda()
				.eq(SoDetail::getSoBillId, wellenDetailVO.getSoBillId())).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(soDetail)) {
				wellenDetailVO.setSoDetail(soDetail);
			}
		}
		if (Func.isNotEmpty(wellenDetailVO.getWellenId())){
			Wellen wellen = wellenService.getById(wellenDetailVO.getWellenId());
			if(Func.isNotEmpty(wellen)){
				wellenDetailVO.setWellenNo(wellen.getWellenNo());
			}
		}
		return wellenDetailVO;
	}
}
