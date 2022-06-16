package org.nodes.wms.biz.basics.sku.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.SkuDao;
import org.nodes.wms.dao.basics.sku.dto.input.SkuAddOrEditRequest;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageAggregate;
import org.nodes.wms.dao.basics.suppliers.SupplierDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddOrEditSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierImportRequest;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkuFactory {

	private final SkuDao skuDao;

	private final OwnerBiz ownerBiz;

	private final SupplierDao supplierDao;

	public Sku createSku(SkuAddOrEditRequest skuAddOrEditRequest) {
		String isAdd = Func.isEmpty(skuAddOrEditRequest.getSkuId()) ? "新增" : "编辑";
		Sku sku = new Sku();
		Func.copy(skuAddOrEditRequest, sku);
		// 根据包装id获取包装名称
		SkuPackage skuPackage = skuDao.getSkuPackageByWspId(skuAddOrEditRequest.getWspId());
		if (Func.isEmpty(skuPackage)){
			throw new ServiceException(String.format("[%s]失败，包装不存在",isAdd));
		}
		sku.setWspName(skuPackage.getWspName());
		return sku;
	}
}
