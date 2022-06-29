package com.nodes.scheduling.common.secure.process;

import com.alibaba.fastjson.JSON;
import com.nodes.scheduling.common.tools.ServletUtil;
import com.nodes.scheduling.common.web.domain.response.JsonResult;
import com.nodes.scheduling.common.web.domain.response.ResultTool;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销成功 Handler
 */
@Component
public class SecureLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        JsonResult<Object> result = ResultTool.success();
        ServletUtil.write(JSON.toJSONString(result));
    }
}
