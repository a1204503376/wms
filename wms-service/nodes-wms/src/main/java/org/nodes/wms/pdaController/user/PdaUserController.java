package org.nodes.wms.pdaController.user;

import lombok.AllArgsConstructor;
import org.nodes.wms.biz.user.UserBiz;
import org.nodes.wms.dao.User.dto.input.EditUserLoginStatusRequest;
import org.nodes.wms.dao.User.dto.output.UserLoginStatusResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 手持用户模块控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA")
public class PdaUserController {
    private final UserBiz userBiz;
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


}
