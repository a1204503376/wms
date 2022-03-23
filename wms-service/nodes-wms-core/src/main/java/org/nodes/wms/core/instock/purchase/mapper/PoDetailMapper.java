package org.nodes.wms.core.instock.purchase.mapper;

import org.nodes.wms.core.instock.purchase.entity.PoDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Primary;

/**
 * 收货单明细表 Mapper 接口
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Primary
public interface PoDetailMapper extends BaseMapper<PoDetail> {

}
