package org.nodes.wms.core.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.instock.asn.entity.ContainerLog;
import org.springframework.context.annotation.Primary;

/**
 * 清点记录 Mapper 接口
 *
 * @author NodeX
 * @since 2020-01-15
 */
@Primary
public interface ContainerLogMapper extends BaseMapper<ContainerLog> {


}
