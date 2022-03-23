package org.nodes.wms.core.outstock.sales.mapper;

import org.nodes.wms.core.outstock.sales.entity.SalesDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Primary;

/**
 * 发货单明细
 Mapper 接口
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Primary
public interface SalesDetailMapper extends BaseMapper<SalesDetail> {

}
