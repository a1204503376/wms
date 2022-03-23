package org.nodes.wms.core.instock.purchase.mapper;

import org.nodes.wms.core.instock.purchase.entity.PoHeader;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Primary;

/**
 * 采购单头表 Mapper 接口
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Primary
public interface PoHeaderMapper extends BaseMapper<PoHeader> {

}
