package org.nodes.wms.biz.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.biz.basics.suppliers.modular.SupplierFactory;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.dao.basics.suppliers.SupplierDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.RemoveRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierExportResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 供应商业务
 *
 * @author 彭永程
 * @date 2022-04-20 10:57
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

	/**
	 * 新增供应商信息
	 * <p>
	 * * @param addSupplierRequest: 供应商新增对象
	 * * @return java.lang.Boolean
	 */
	@Override
	public Boolean newSupplier(AddSupplierRequest addSupplierRequest) {
		boolean isExist = supplierDao.isExistSupplierCode(addSupplierRequest.getCode());
		if (isExist) {
			throw new ServiceException("新增供应商失败,供应商编码["+ addSupplierRequest.getCode()+"]已存在");
		}
		Supplier supplier = supplierFactory.createSupplier(addSupplierRequest);
		return supplierDao.insert(supplier);
	}

	@Override
	public Boolean removeByIds(RemoveRequest removeRequest) {
		return supplierDao.delete(removeRequest.getIds());
	}

	@Override
	public void exportSupplier(SupplierPageQuery supplierPageQuery, HttpServletResponse httpServletResponse) {
		List<SupplierExportResponse> supplierExportResponse = supplierDao.selectByConditions(supplierPageQuery);
		ExcelUtil.export(httpServletResponse, "供应商", "供应商数据报表", supplierExportResponse, SupplierExportResponse.class);
	}
}
