
package org.nodes.wms.core.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.system.entity.UserRegister;
import org.nodes.wms.core.system.mapper.UserRegisterMapper;
import org.nodes.wms.core.system.service.IUserRegisterService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author NodeX
 * @since 2020-04-01
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class UserRegisterServiceImpl<M extends UserRegisterMapper, T extends UserRegister>
	extends BaseServiceImpl<UserRegisterMapper, UserRegister>
	implements IUserRegisterService {

}
