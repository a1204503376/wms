package org.nodes.wms.core.instock.asn.wrapper;

import com.sun.xml.bind.v2.model.core.ID;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.nodes.wms.core.instock.asn.entity.ContainerLog;
import org.nodes.wms.core.instock.asn.vo.ContainerLogVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

public class ContainerLogWrapper extends BaseEntityWrapper<ContainerLog, ContainerLogVO> {
	public static ContainerLogWrapper build() {
		return new ContainerLogWrapper();
	}

	@Override
	public ContainerLogVO entityVO(ContainerLog entity) {
		ContainerLogVO containerLogVO = BeanUtil.copy(entity, ContainerLogVO.class);
		if (Func.isNotEmpty(containerLogVO)) {
			containerLogVO.setSkuLevelDesc(DictCache.getValue(DictConstant.SKU_LEVEL, containerLogVO.getSkuLevel()));
			containerLogVO.setAclStatusDesc(DictCache.getValue(DictConstant.ACL_STATE, containerLogVO.getAclStatus()));
			SkuPackage skuPackage = SkuPackageCache.getById(entity.getWspId());
			if (Func.isNotEmpty(skuPackage)) {
				containerLogVO.setWspName(skuPackage.getWspName());
			}

//			SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(entity.getWspId(), entity.getSkuLevel());
			SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(entity.getWspId(), 1);
			if (Func.isNotEmpty(skuPackageDetail)) {
				containerLogVO.setWsuName(skuPackageDetail.getWsuName());
			}

			User user = UserCache.getById(entity.getCreateUser());
			if (Func.isNotEmpty(user)) {
				containerLogVO.setUserName(user.getName());
			}
		}
		return containerLogVO;
	}
}
