
package org.nodes.wms.core.stock.transfer.service.impl;

import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.mapper.TransferRecordMapper;
import org.nodes.wms.core.stock.transfer.service.ITransferRecordService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 移动记录表 服务实现类
 *
 * @author wangjw
 * @since 2020-02-27
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TransferRecordServiceImpl<M extends TransferRecordMapper, T extends TransferRecord>
	extends BaseServiceImpl<TransferRecordMapper, TransferRecord>
	implements ITransferRecordService {


}
