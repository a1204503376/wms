package com.nodes.project.system.user.controller;

import com.nodes.common.utils.StringUtils;
import com.nodes.framework.shiro.service.RegisterService;
import com.nodes.framework.web.controller.BaseController;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.system.config.service.IConfigService;
import com.nodes.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册验证
 *
 * @author dml
 */
@Controller
public class RegisterController extends BaseController {
    @Autowired
    private RegisterService registerService;

    @Autowired
    private IConfigService configService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public AjaxResult ajaxRegister(User user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
