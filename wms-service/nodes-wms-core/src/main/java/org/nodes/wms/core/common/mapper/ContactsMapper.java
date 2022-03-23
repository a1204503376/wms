
package org.nodes.wms.core.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.common.entity.Contacts;
import org.springframework.context.annotation.Primary;

/**
 *  Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Primary
public interface ContactsMapper extends BaseMapper<Contacts> {
}
