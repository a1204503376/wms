package org.nodes.wms.core.instock.asn.wrapper;

import com.sun.xml.bind.v2.model.core.ID;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.wms.core.instock.asn.entity.AsnLpnDetail;
import org.nodes.wms.core.instock.asn.vo.AsnLpnDetailVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

public class AsnLpnDetailWrapper extends BaseEntityWrapper<AsnLpnDetail, AsnLpnDetailVO> {
	public static AsnLpnDetailWrapper build() {
		return new AsnLpnDetailWrapper();
	}

	@Override
	public AsnLpnDetailVO entityVO(AsnLpnDetail entity) {
		AsnLpnDetailVO asnLpnDetailVO = BeanUtil.copy(entity, AsnLpnDetailVO.class);
		asnLpnDetailVO.setLpnStatusDesc(DictCache.getValue("lpn_status", Integer.parseInt(entity.getLpnStatus())));
		return asnLpnDetailVO;
	}
}
