package org.nodes.wms.core.outstock.so.service.impl;

import org.springblade.core.log.exception.ServiceException;
import org.nodes.wms.core.warehouse.entity.PlatformInfo;
import org.nodes.wms.core.warehouse.service.IPlatformInfoService;
import org.nodes.wms.core.outstock.so.entity.SoRegister;
import org.nodes.wms.core.outstock.so.mapper.SoRegisterMapper;
import org.nodes.wms.core.outstock.so.service.ISoRegisterService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 装车登记 服务实现类
 *
 * @author zhongls
 * @since 2020-05-07
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SoRegisterServiceImpl<M extends SoRegisterMapper, T extends SoRegister>
	extends BaseServiceImpl<SoRegisterMapper, SoRegister>
	implements ISoRegisterService {

	@Autowired
	IPlatformInfoService platformInfoService;

	/**
	 * 装车登记保存或者修改
	 *
	 * @param soRegister
	 * @return
	 */
	@Override
	public boolean saveOrUpdate(SoRegister soRegister) {
		if (Func.isNotEmpty(soRegister.getPlatformCode())) {
			PlatformInfo platformInfo = platformInfoService.getOne(Condition.getQueryWrapper(new PlatformInfo()).lambda()
				.eq(PlatformInfo::getPlatformCode, soRegister.getPlatformCode()));
			if (ObjectUtil.isNotEmpty(platformInfo))
				soRegister.setPlatformName(platformInfo.getPlatformName());
		}
		if (Func.isEmpty(soRegister.getTrrId())) {
			if (baseMapper.selectList(Condition.getQueryWrapper(new SoRegister()).lambda()
				.eq(SoRegister::getLoadId, soRegister.getLoadId())).size() > 0)
				throw new ServiceException("装车牌重复！");
			return super.save(soRegister);
		} else {
			if (baseMapper.selectList(Condition.getQueryWrapper(new SoRegister()).lambda()
				.eq(SoRegister::getLoadId, soRegister.getLoadId())
				.ne(SoRegister::getTrrId, soRegister.getTrrId())).size() > 0)
				throw new ServiceException("装车牌重复！");
			return super.updateById(soRegister);
		}
	}
}
