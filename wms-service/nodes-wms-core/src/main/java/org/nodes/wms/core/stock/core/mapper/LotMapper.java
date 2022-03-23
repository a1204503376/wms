package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.core.entity.Lot;
import org.springframework.context.annotation.Primary;

/**
 * 批次号 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-25
 */
@Primary
public interface LotMapper extends BaseMapper<Lot> {

}
