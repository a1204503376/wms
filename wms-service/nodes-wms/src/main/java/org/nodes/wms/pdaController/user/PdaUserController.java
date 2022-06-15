package org.nodes.wms.pdaController.user;

import lombok.AllArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.user.UserBiz;
import org.nodes.wms.dao.User.dto.input.EditUserLoginStatusRequest;
import org.nodes.wms.dao.User.dto.output.UserLoginStatusResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 手持用户模块控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API)
public class PdaUserController {
    private final UserBiz userBiz;
	private final WarehouseBiz warehouseBiz;
	@ApiLog("PDA-人员签到")
	@PostMapping("/editUserLoginStatus")
	public R<UserLoginStatusResponse> editUserLoginStatus(@Valid @RequestBody EditUserLoginStatusRequest editUserLoginStatusRequest, HttpServletRequest request) {
		UserLoginStatusResponse userLoginStatusResponse =  userBiz.EditUserLoginStatus(editUserLoginStatusRequest);
		return R.data(userLoginStatusResponse);
	}
	/**
	 * 获取签到状态
	 */
	@GetMapping("/getLoginStatus")
	public R<UserLoginStatusResponse> getLoginStatus() {
		BladeUser user = AuthUtil.getUser();
		UserLoginStatusResponse userLoginStatusResponse = userBiz.getLoginStatus(user);
		return R.data(userLoginStatusResponse);
	}

	/**
	 * 获取用户库房
	 */
	@GetMapping("getWarehouseList")
	public R<List<Warehouse>> getWarehouseList(){
		BladeUser user = AuthUtil.getUser();
		List<Warehouse> warehouseResponseList =  warehouseBiz.getWarehouseByUserId(user);
		return R.data(warehouseResponseList);
	}

	/**
	 * PDA-切换库房
	 * @param warehouse 库房信息
	 */
	@ApiLog("PDA-切换库房")
	@PostMapping("/warehouseChange")
	public void warehouseChange(@RequestBody Warehouse warehouse){

	}

}
