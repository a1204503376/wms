
package org.nodes.wms.core.stock.transfer.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.vo.TransferRecordVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 移动记录表包装类,返回视图层所需的字段
 *
 * @author wangjw
 * @since 2020-02-27
 */
public class TransferRecordWrapper extends BaseEntityWrapper<TransferRecord, TransferRecordVO> {

	public static TransferRecordWrapper build() {
		return new TransferRecordWrapper();
	}

	@Override
	public TransferRecordVO entityVO(TransferRecord transferRecord) {
		TransferRecordVO transferRecordVO = BeanUtil.copy(transferRecord, TransferRecordVO.class);
		String transTypeName = DictCache.getValue(DictConstant.TRANSFER_TYPE, transferRecord.getTransferType());
		transferRecordVO.setTransferTypeName(transTypeName);

		Sku sku = SkuCache.getById(transferRecord.getSkuId());
		if (Func.isNotEmpty(sku)) {
			transferRecordVO.setSkuCode(sku.getSkuCode());
			transferRecordVO.setSkuName(sku.getSkuName());
		}
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(transferRecord.getWoId());
		if (Func.isNotEmpty(owner)) {
			transferRecordVO.setOwnerName(owner.getOwnerName());
		}

		SkuPackage skuPackage = SkuPackageCache.getById(transferRecord.getWspId());
		if (Func.isNotEmpty(skuPackage)) {
			transferRecordVO.setWspName(skuPackage.getWspName());
		}
		String toSkuLevelName = DictCache.getValue(DictConstant.SKU_LEVEL, transferRecord.getSkuLevel());
		transferRecordVO.setSkuLevelName(toSkuLevelName);

		User user = UserCache.getById(transferRecord.getCreateUser());
		if (Func.isNotEmpty(user)) {
			transferRecordVO.setUserCode(user.getAccount());
			transferRecordVO.setUserName(user.getName());
		}
		return transferRecordVO;
	}

}
