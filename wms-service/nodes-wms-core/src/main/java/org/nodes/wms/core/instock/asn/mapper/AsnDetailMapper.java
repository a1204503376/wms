package org.nodes.wms.core.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.springframework.context.annotation.Primary;

/**
 * 收货单明细表 Mapper 接口
 *
 * @author zx
 * @since 2019-12-13
 */
@Primary
public interface AsnDetailMapper extends BaseMapper<AsnDetail> {

}
