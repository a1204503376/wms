package org.nodes.wms.core.outstock.sales.mapper;

import org.nodes.wms.core.outstock.sales.entity.SalesHeader;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Primary;

/**
 * 销售单主表
 Mapper 接口
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Primary
public interface SalesHeaderMapper extends BaseMapper<SalesHeader> {

}
