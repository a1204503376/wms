
package org.nodes.wms.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.system.entity.UserRegister;
import org.nodes.wms.core.system.vo.UserRegisterVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author NodeX
 * @since 2020-04-01
 */
@Primary
public interface UserRegisterMapper extends BaseMapper<UserRegister> {
	List<UserRegisterVO> selectUserRegisterPage(IPage page, UserRegisterVO userRegister);

	List<UserRegister> selectUserRegisterToday(Long userId);

	List<UserRegister> selectUserRegisterTomonth(Long userId,String time);
}
