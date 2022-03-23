package org.nodes.wms.core.outstock.loading.wrapper;

import org.nodes.wms.core.outstock.loading.entity.SoTruckDetail;
import org.nodes.wms.core.outstock.loading.vo.SoTruckDetailVO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;
import org.nodes.wms.core.outstock.so.wrapper.SoDetailWrapper;
import org.nodes.wms.core.outstock.so.wrapper.SoHeaderWrapper;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;

/**
 * 装车明细封装
 */
public class SoTruckDetailWrapper extends BaseEntityWrapper<SoTruckDetail, SoTruckDetailVO> {
	public static SoTruckDetailWrapper build() {
		return new SoTruckDetailWrapper();
	}

	@Override
	public SoTruckDetailVO entityVO(SoTruckDetail entity) {
		SoTruckDetailVO soTruckDetailVO = BeanUtil.copy(entity, SoTruckDetailVO.class);
		if (Func.isNotEmpty(soTruckDetailVO.getSoBillId())) {
			ISoHeaderService soHeaderService = SpringUtil.getBean(ISoHeaderService.class);
			List<SoHeader> soHeaderList = soHeaderService.list(Condition.getQueryWrapper(new SoHeader())
				.lambda()
				.eq(SoHeader::getSoBillId, soTruckDetailVO.getSoBillId())
			);
			if (Func.isNotEmpty(soHeaderList)) {
				soTruckDetailVO.setSoHeaderVOList(SoHeaderWrapper.build().listVO(soHeaderList));
			}

			ISoDetailService soDetailService = SpringUtil.getBean(ISoDetailService.class);
			List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
				.lambda()
				.eq(SoDetail::getSoBillId, soTruckDetailVO.getSoBillId())
			);
			if (Func.isNotEmpty(soDetailList)) {
				soTruckDetailVO.setSoDetailVOList(SoDetailWrapper.build().listVO(soDetailList));
			}

		}
		return soTruckDetailVO;
	}

}
