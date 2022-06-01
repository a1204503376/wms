package org.nodes.wms.biz.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.StatusEnum;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.ValidationUtil;
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
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
    public boolean importExcel(List<AddSupplierRequest> addSupplierList) {
		List<Supplier> supplierList = supplierFactory.createSupplierListForUpload(addSupplierList);
		return supplierDao.importExcel(supplierList);
	}

	@Override
	public List<DataVerify> validExcel(List<SupplierImportExcelRequest> importExcelList) {

		List<DataVerify> dataVerifyList = new ArrayList<>();
		int index = 1;
		for (SupplierImportExcelRequest excelData : importExcelList
			 ) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(excelData);
			// TODO 根据供应商编码查询供应商信息 模块之间的引用如何处理
//			if (Func.isNotEmpty(addSupplier.getOwnerCode())){
//				Owner owner = ownerBiz.findByCode(addSupplier.getOwnerCode());
//				if(Func.isEmpty(owner)){
//					throw new ServiceException("导入失败，不存在货主编码："+addSupplier.getOwnerCode());
//				} else {
//					supplier.setWoId(owner.getWoId());
//				}
//			}
			boolean isExist = supplierDao.isExistSupplierCode(excelData.getCode());
			if (isExist) {
				validResult.addError("code","导入失败，供应商编码["+ excelData.getCode()+"]已存在");
			}
			if (!excelData.getStatus().equals(StatusEnum.ON.getIndex())
				&& !excelData.getStatus().equals(StatusEnum.OFF.getIndex())){
				throw new ServiceException("导入失败，启用状态只能为1(启用)或者0(禁用)");
			}

			if(validResult.hasErrors()){
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			}
			dataVerifyList.add(dataVerify);

			index++;
		}
		return dataVerifyList;
	}
}
