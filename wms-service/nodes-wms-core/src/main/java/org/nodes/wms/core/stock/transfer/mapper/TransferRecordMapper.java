
package org.nodes.wms.core.stock.transfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.springframework.context.annotation.Primary;

/**
 * 移动记录表 Mapper 接口
 *
 * @author wangjw
 * @since 2020-02-27
 */
@Primary
public interface TransferRecordMapper extends BaseMapper<TransferRecord> {
}
