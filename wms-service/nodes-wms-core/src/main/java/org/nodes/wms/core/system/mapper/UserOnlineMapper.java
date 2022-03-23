
package org.nodes.wms.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.system.entity.UserOnline;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 在线用户 Mapper 接口
 *
 * @author pengwei
 * @since 2020-04-01
 */
@Primary
public interface UserOnlineMapper extends BaseMapper<UserOnline> {


}
