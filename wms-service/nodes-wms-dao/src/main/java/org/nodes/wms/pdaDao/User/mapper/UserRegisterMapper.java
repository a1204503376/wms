package org.nodes.wms.pdaDao.User.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.pdaDao.User.dto.output.UserLoginStatusResponse;
import org.nodes.wms.pdaDao.User.entites.UserRegister;
import org.springframework.stereotype.Repository;

@Repository("PdaUserRegisterMapper")
public interface UserRegisterMapper  extends BaseMapper<UserRegister> {
	UserLoginStatusResponse selectLoginStatusByUserId(Long userId);
}
