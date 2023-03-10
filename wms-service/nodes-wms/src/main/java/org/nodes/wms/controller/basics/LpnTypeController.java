package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.dao.basics.lpntype.dto.input.*;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeByIdResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypePageResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeSelectResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 容器管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"lpnType")
@Api(value = "容器类别", tags = "容器类别接口")
public class LpnTypeController {

	private final LpnTypeBiz lpnTypeBiz;

	/**
	 * 容器管理分页查询
	 */
	@ApiOperation(value = "分页", notes = "传入lpnTypePageQuery、query")
	@PostMapping("/page")
	public R<IPage<LpnTypePageResponse>> page(@RequestBody LpnTypePageQuery lpnTypePageQuery, Query query) {
		IPage<LpnTypePageResponse> pages = lpnTypeBiz.getPage(query, lpnTypePageQuery);
		return R.data(pages);
	}

	/**
	 * 容器管理新增
	 */
	@ApiLog("容器管理-新增")
	@ApiOperation(value = "新增", notes = "传入newLpnTypeRequest")
	@PostMapping("/newLpnType")
	public R newLpnType(@Valid @RequestBody NewLpnTypeRequest lpnTypeRequest) {
		return R.status(lpnTypeBiz.newLpnType(lpnTypeRequest));
	}

	/**
	 * 容器管理删除
	 */
	@ApiLog("容器管理-逻辑删除")
	@ApiOperation(value = "删除", notes = "传入deleteLpnTypeRequest")
	@PostMapping("/delete")
	public R delete(@RequestBody DeleteLpnTypeRequest deleteLpnTypeRequest) {
		return R.status(lpnTypeBiz.remove(deleteLpnTypeRequest));
	}

	/**
	 * 容器导出
	 */

	@ApiOperation(value = "导出", notes = "传入params")
	@PostMapping("/excel")
	public void excel(@ApiIgnore @RequestBody HashMap<String, Object> params, HttpServletResponse response) {
		lpnTypeBiz.exportExcel(params,response);
	}

	/**
	 * 容器管理修改
	 */
	@ApiLog("容器管理-修改容器")
	@ApiOperation(value = "修改", notes = "传入updateLpnTypeRequest")
	@PostMapping("/updateLpnTypeById")
	public R updateLpnTypeById(@RequestBody UpdateLpnTypeRequest lpnTypeRequest) {
		return R.status(lpnTypeBiz.updateLpnTypeById(lpnTypeRequest));
	}

	/**
	 * 容器管理查找
	 */
	@ApiOperation(value = "根据容器id查找容器", notes = "传入lpnTypByIdeRequest")
	@PostMapping("/getLpnTypeById")
	public R<LpnTypeByIdResponse> getLpnTypeById(@RequestBody LpnTypeByIdRequest lpnTypeRequest) {
		return R.data(lpnTypeBiz.getLpnTypeById(lpnTypeRequest));
	}

	/**
	 * 获取容器下拉组件数据
	 */
	@ApiOperation(value = "容器类别组件数据")
	@GetMapping("/getLpnTypeSelectList")
	public R<List<LpnTypeSelectResponse>> getLpnTypeSelectList(){
		return R.data(lpnTypeBiz.findLpnTypeSelectList());
	}

}
