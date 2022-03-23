package org.nodes.wms.core.stock.transfer.service.impl;

import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.stock.core.dto.StockOccupyDTO;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.stock.transfer.entity.TransferDetailItem;
import org.nodes.wms.core.stock.transfer.entity.TransferHeader;
import org.nodes.wms.core.stock.transfer.service.ITransferDetailItemService;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.wms.core.stock.transfer.entity.TransferDetail;
import org.nodes.wms.core.stock.transfer.mapper.TransferDetailMapper;
import org.nodes.wms.core.stock.transfer.service.ITransferDetailService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库内移动明细 服务实现类
 *
 * @author pengwei
 * @since 2020-08-03
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TransferDetailServiceImpl<M extends TransferDetailMapper, T extends TransferDetail>
	extends BaseServiceImpl<TransferDetailMapper, TransferDetail>
	implements ITransferDetailService {

	@Autowired
	IStockOccupyService stockOccupyService;
	@Autowired
	ITransferDetailItemService transferDetailItemService;

	@Override
	public boolean saveOrUpdate(
		TransferHeader transferHeader,
		Stock stock,
		SoDetail soDetail,
		BigDecimal qty,
		Long systemProcId) {

		Sku sku = SkuCache.getById(stock.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("指定物品ID不存在(ID:" + stock.getSkuId() + ")!");
		}
		TransferDetail transferDetail = super.getOne(Condition.getQueryWrapper(new TransferDetail()).lambda()
			.eq(TransferDetail::getTransferBillId, transferHeader.getTransferBillId())
			.eq(TransferDetail::getSkuId, stock.getSkuId())
			.eq(TransferDetail::getWspId, stock.getWspId())
			.eq(TransferDetail::getSkuLevel, stock.getSkuLevel())
			.eq(TransferDetail::getSourceLocId, stock.getLocId())
			.last("limit 1"));
		if (Func.isEmpty(transferDetail)) {
			transferDetail = new TransferDetail();
			transferDetail.setTransferBillId(transferHeader.getTransferBillId());
			transferDetail.setStockId(stock.getStockId());
			transferDetail.setWoId(sku.getWoId());
			transferDetail.setSkuId(stock.getSkuId());
			transferDetail.setSkuCode(sku.getSkuCode());
			transferDetail.setSkuName(sku.getSkuName());
			transferDetail.setWspId(stock.getWspId());
			transferDetail.setSkuLevel(stock.getSkuLevel());
			transferDetail.setSourceLocId(stock.getLocId());
			transferDetail.setPlanQty(qty);
			// 获取该包装的包装单位
			SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(stock.getWspId(), stock.getSkuLevel());
			if (Func.isNotEmpty(skuPackageDetail)) {
				transferDetail.setWsuCode(skuPackageDetail.getWsuCode());
				transferDetail.setWsuName(skuPackageDetail.getWsuName());
			}
		} else {
			transferDetail.setPlanQty(transferDetail.getPlanQty().add(qty));
		}
		Boolean result = super.saveOrUpdate(transferDetail);

		TransferDetailItem transferDetailItem = new TransferDetailItem();
		transferDetailItem.setTransferHeaderId(transferHeader.getTransferBillId());
		transferDetailItem.setTransferDetailId(transferDetail.getTransferDetailId());
		transferDetailItem.setBillId(soDetail.getSoBillId());
		transferDetailItem.setDetailId(soDetail.getSoDetailId());
		transferDetailItem.setQty(qty);
		transferDetailItemService.save(transferDetailItem);

		// 创建库存占用
		StockOccupyDTO stockOccupyDTO = new StockOccupyDTO();
		stockOccupyDTO.setTransId(transferHeader.getTransferBillId());
		stockOccupyDTO.setOccupyType(StockOccupyTypeEnum.Replenish.getIndex());
		stockOccupyDTO.setWhId(transferHeader.getWhId());
		stockOccupyDTO.setStockId(stock.getStockId());
		stockOccupyDTO.setSkuId(sku.getSkuId());
		stockOccupyDTO.setSkuCode(sku.getSkuCode());
		stockOccupyDTO.setSkuName(sku.getSkuName());
		stockOccupyDTO.setSystemProcId(systemProcId);
		stockOccupyDTO.setOccupyTime(LocalDateTime.now());
		stockOccupyDTO.setOccupyQty(qty);
		stockOccupyDTO.setSoBillId(transferHeader.getTransferBillId());
		stockOccupyDTO.setSoBillNo(String.valueOf(transferHeader.getTransferBillId()));
		stockOccupyDTO.setSoDetailId(transferDetail.getTransferDetailId());
		stockOccupyDTO.setCreateUser(transferHeader.getCreateUser());
		stockOccupyDTO.setUpdateUser(transferHeader.getUpdateUser());
		stockOccupyService.add(stockOccupyDTO);

		return result;
	}
}
