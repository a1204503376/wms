package org.nodes.wms.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.system.UpdateVerBiz;
import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerEditRequest;
import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerPageQuery;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerEditResponse;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerPageResponse;
import org.nodes.wms.dao.system.updateVer.entities.UpdateVer;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 系统版本API
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "updateVer")
@Api(value = "系统版本接口", tags = "系统版本接口")
public class UpdateVerController {

	private final UpdateVerBiz updateVerBiz;

	/**
	 * 版本管理：分页查询
	 */
	@ApiOperation(value = "分页")
	@PostMapping("/page")
	public R<Page<UpdateVerPageResponse>> page(Query query , @RequestBody UpdateVerPageQuery updateVerPageQuery){
		return R.data(updateVerBiz.page(Condition.getPage(query), updateVerPageQuery));
	}

	/**
	 * 版本编辑：根据版本id查询版本信息
	 */
	@ApiOperation(value = "编辑时查询")
	@GetMapping("/detailByEdit")
	public R<UpdateVerEditResponse> detailByEdit(@RequestParam Long suvId){
		return R.data(updateVerBiz.detailByEdit(suvId));
	}

	/**
	 * 版本管理：编辑
	 */
	@ApiOperation(value = "编辑")
	@PostMapping("/edit")
	public R<String> edit(@RequestBody UpdateVerEditRequest updateVerEditRequest){
		UpdateVer updateVer = updateVerBiz.edit(updateVerEditRequest);
		return R.success(String.format("编辑成功，版本号数值：%s",updateVer.getVerNum()));
	}

	/**
	 * 版本管理：导出
	 */
	@ApiOperation(value = "导出")
	@PostMapping("/export")
	public void export(@RequestBody UpdateVerPageQuery updateVerPageQuery, HttpServletResponse response){
		updateVerBiz.export(updateVerPageQuery, response);
	}
}
