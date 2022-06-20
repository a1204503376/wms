package org.nodes.wms.biz.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.system.UpdateVerBiz;
import org.nodes.wms.biz.system.modular.UpdateVerFactory;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierExportResponse;
import org.nodes.wms.dao.system.updateVer.UpdateVerDao;
import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerEditRequest;
import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerPageQuery;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerEditResponse;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerExportResponse;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerPageResponse;
import org.nodes.wms.dao.system.updateVer.entities.UpdateVer;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 *
 **/
@Service
@RequiredArgsConstructor
public class UpdateVerBizImpl implements UpdateVerBiz {

	private final UpdateVerDao updateVerDao;

	private final UpdateVerFactory updateVerFactory;

	@Override
	public Page<UpdateVerPageResponse> page(IPage<?> page, UpdateVerPageQuery query) {
		return updateVerDao.page(page,query);
	}

	@Override
	public UpdateVerEditResponse detailByEdit(Long suvId) {
		UpdateVerEditResponse updateVerEditResponse = new UpdateVerEditResponse();
		UpdateVer updateVer = updateVerDao.getById(suvId);
		Func.copy(updateVer,updateVerEditResponse);
		return updateVerEditResponse;
	}

	@Override
	public UpdateVer edit(UpdateVerEditRequest updateVerEditRequest) {
		UpdateVer updateVer = updateVerFactory.createUpdateVer(updateVerEditRequest);
		updateVerDao.updateUpdateVerById(updateVer);
		return updateVer;
	}

	@Override
	public void export(UpdateVerPageQuery updateVerPageQuery, HttpServletResponse response) {
		List<UpdateVerExportResponse> updateVerList = updateVerDao.listByQuery(updateVerPageQuery);
		ExcelUtil.export(response, "系统版本", "系统版本数据报表", updateVerList, UpdateVerExportResponse.class);
	}
}
