package org.nodes.wms.core.stock.transfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.transfer.entity.TransferHeader;
import org.springframework.context.annotation.Primary;

/**
 * 库内移动表头 Mapper 接口
 *
 * @author pengwei
 * @since 2020-08-03
 */
@Primary
public interface TransferHeaderMapper extends BaseMapper<TransferHeader> {
}
