package org.nodes.wms.biz.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.biz.basics.suppliers.modular.SupplierFactory;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.dao.basics.suppliers.SupplierDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.*;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierExportResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 供应商业务
 *
 * @author 彭永程
 **/
@Service
@RequiredArgsConstructor
public class SupplierBizImpl implements SupplierBiz {

	private final SupplierDao supplierDao;

	private final SupplierFactory supplierFactory;

	private final LogBiz logBiz;

	@Override
	public Page<SupplierPageResponse> getPage(IPage<?> page, SupplierPageQuery supplierPageQuery) {
		return supplierDao.selectPage(page, supplierPageQuery);
	}

	@Override
	public Supplier newSupplier(AddSupplierRequest addSupplierRequest) {
		boolean isExist = supplierDao.isExistSupplierCode(addSupplierRequest.getCode());
		if (isExist) {
			throw new ServiceException("新增供应商失败,供应商编码["+ addSupplierRequest.getCode()+"]已存在");
		}
		Supplier supplier = supplierFactory.createSupplier(addSupplierRequest);
		supplierDao.insert(supplier);
		return supplier;
	}

	@Override
	public boolean removeByIds(RemoveRequest removeRequest) {
		return supplierDao.delete(removeRequest.getIds());
	}

	@Override
	public void exportSupplier(SupplierPageQuery supplierPageQuery, HttpServletResponse response) {
		List<SupplierExportResponse> supplierList = supplierDao.listBySupplierPageQuery(supplierPageQuery);
		ExcelUtil.export(response, "供应商", "供应商数据报表", supplierList, SupplierExportResponse.class);
	}

    @Override
    public List<SupplierSelectResponse> getSupplierSelectResponseTop10List(SupplierSelectQuery supplierSelectQuery) {
		return supplierDao.listTop10ByCodeName(supplierSelectQuery.getKey(),supplierSelectQuery.getKey());
    }

    @Override
    public Supplier findById(Long id) {
        return supplierDao.getById(id);
    }

    @Override
    public Supplier findByCode(String code) {
		return supplierDao.getByCode(code);
    }

    @Override
    public boolean importExcel(List<SupplierImportRequest> importDataList) {
		if(Func.isEmpty(importDataList)){
			throw new ServiceException("导入失败，没有可导入的数据");
		}
		List<Supplier> supplierList = supplierFactory.createSupplierListForImport(importDataList);
		return supplierDao.importExcel(supplierList);
	}
}
