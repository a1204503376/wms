package org.nodes.core.base.wrapper;

import com.sun.xml.bind.v2.model.core.ID;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.ITenantService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.entity.Tenant;
import org.nodes.core.base.vo.ParamVO;
import org.nodes.core.base.cache.*;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

import javax.swing.*;

public class ParamWrapper extends BaseEntityWrapper<Param, ParamVO> {
	public static ParamWrapper build() {
		return new ParamWrapper();
	}

	@Override
	public ParamVO entityVO(Param entity) {
		ParamVO paramVO = BeanUtil.copy(entity, ParamVO.class);
		ITenantService tenantService = SpringUtil.getBean(ITenantService.class);
		if (Func.isNotEmpty(paramVO)) {
			paramVO.setIsVisibleName(DictCache.getValue("is_visible", entity.getIsVisible()));
			Tenant tenant = tenantService.getById(entity.getTenantId());
			if (Func.isNotEmpty(tenant)) {
				paramVO.setTenantName(tenant.getTenantName());
			}
		}
		return paramVO;
	}
}
