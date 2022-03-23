
package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.springframework.context.annotation.Primary;

/**
 * 来往企业 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Primary
public interface EnterpriseMapper extends BaseMapper<Enterprise> {
}
