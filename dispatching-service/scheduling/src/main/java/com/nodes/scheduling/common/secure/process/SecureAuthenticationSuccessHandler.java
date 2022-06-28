package com.nodes.scheduling.common.secure.process;

import com.alibaba.fastjson.JSON;
import com.nodes.scheduling.common.tools.DateUtils;
import com.nodes.scheduling.common.tools.ServletUtil;
import com.nodes.scheduling.common.web.domain.response.JsonResult;
import com.nodes.scheduling.common.web.domain.response.ResultTool;
import com.nodes.scheduling.modules.system.entity.SysUser;
import com.nodes.scheduling.modules.system.service.SysUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功 Handler
 */
@Component
public class SecureAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private SysUserService sysUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        User userDetails = (User) authentication.getPrincipal();

        SysUser sysUser = sysUserService.selectByName(userDetails.getUsername());
        sysUser.setLastLoginTime(DateUtils.getNowDate());
        sysUser.setUpdateTime(DateUtils.getNowDate());
        sysUser.setUpdateUser(sysUser.getId());
        sysUserService.updateById(sysUser);

        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展

        JsonResult<Object> result = ResultTool.success();
        ServletUtil.write(JSON.toJSONString(result));
    }
}
