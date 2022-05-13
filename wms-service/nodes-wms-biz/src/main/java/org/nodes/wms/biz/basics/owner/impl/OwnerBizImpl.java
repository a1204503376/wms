package org.nodes.wms.biz.basics.owner.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.dao.basics.owner.OwnerDao;
import org.nodes.wms.dao.basics.owner.dto.OwnerSelectResponse;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 货主 业务接口实现类
 **/
@Service
@RequiredArgsConstructor
public class OwnerBizImpl implements OwnerBiz {

	private final OwnerDao ownerDao;
	@Override
	public List<OwnerSelectResponse> getOwnerSelectResponseList() {
		//List<Supplier> supplierList = supplierDao.listBySupplierPageQuery(supplierPageQuery);
		//List<SupplierExportResponse> supplierExportResponseList = Func.copy(supplierList, SupplierExportResponse.class);
		List<Owner> ownerList = ownerDao.selectOwnerSelectResponseList();
		return Func.copy(ownerList, OwnerSelectResponse.class);
	}

    @Override
    public Owner findById(Long woId) {
        return ownerDao.getById(woId);
    }
}
