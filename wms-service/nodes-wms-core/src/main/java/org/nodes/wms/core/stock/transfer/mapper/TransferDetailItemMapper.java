package org.nodes.wms.core.stock.transfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.transfer.entity.TransferDetailItem;
import org.springframework.context.annotation.Primary;

/**
 * 库内移动明细关联 Mapper 接口
 *
 * @author NodeX
 * @since 2020-08-03
 */
@Primary
public interface TransferDetailItemMapper extends BaseMapper<TransferDetailItem> {
}
