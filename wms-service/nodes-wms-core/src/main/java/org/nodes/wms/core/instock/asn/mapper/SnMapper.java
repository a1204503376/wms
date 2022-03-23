package org.nodes.wms.core.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.instock.asn.entity.Sn;
import org.springframework.context.annotation.Primary;

/**
 * 入库货品序列号 Mapper 接口
 *
 * @author NodeX
 * @since 2020-01-15
 */
@Primary
public interface SnMapper extends BaseMapper<Sn> {

}
