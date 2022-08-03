package org.nodes.wms.controller.basics;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.basics.crudcolumn.CrudColumnBiz;
import org.nodes.wms.dao.basics.crudcolumn.dto.CrudColumnRequest;
import org.nodes.wms.dao.basics.crudcolumn.dto.CrudColumnResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 列显隐表 控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.NODES_CURD_COLUMN_URL)
public class CrudColumnController {

	private final CrudColumnBiz crudColumnBiz;

	/**
	 * 获取当前登录用户的列显隐配置数据
	 */
	@GetMapping("/getCrudColumnResponseList")
	public R<List<CrudColumnResponse>> getCrudColumnResponseList(Long menuId) {
		return R.data(crudColumnBiz.getCrudColumnResponseList(menuId));
	}


	/**
	 * 列显隐表新增或修改
	 */
	@PostMapping("/submit")
	public R<Boolean> submit(@Valid @RequestBody List<CrudColumnRequest> crudColumnRequestList) {
		return R.status(crudColumnBiz.deleteBeforeSaving(crudColumnRequestList));
	}

}
