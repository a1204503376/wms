
package org.nodes.wms.core.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.instock.asn.entity.Register;
import org.springframework.context.annotation.Primary;

/**
 * 到货登记 Mapper 接口
 *
 * @author zhonglianshuai
 * @since 2020-04-07
 */
@Primary
public interface RegisterMapper extends BaseMapper<Register> {

}
