package org.nodes.wms.core.stock.transfer.service.impl;

import org.nodes.wms.core.stock.transfer.entity.TransferDetailItem;
import org.nodes.wms.core.stock.transfer.mapper.TransferDetailItemMapper;
import org.nodes.wms.core.stock.transfer.service.ITransferDetailItemService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库内移动明细关联 服务实现类
 *
 * @author pengwei
 * @since 2020-08-03
 */
@Service
@Primary
@Transactional(propagation= Propagation.NESTED,isolation= Isolation.DEFAULT,rollbackFor=Exception.class)
public class TransferDetailItemServiceImpl<M extends TransferDetailItemMapper, T extends TransferDetailItem>
	extends BaseServiceImpl<TransferDetailItemMapper, TransferDetailItem>
	implements ITransferDetailItemService {

}
