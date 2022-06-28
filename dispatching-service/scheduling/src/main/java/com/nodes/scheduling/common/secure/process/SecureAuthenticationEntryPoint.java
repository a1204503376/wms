package com.nodes.scheduling.common.secure.process;

import com.alibaba.fastjson.JSON;
import com.nodes.scheduling.common.tools.ServletUtil;
import com.nodes.scheduling.common.web.domain.response.JsonResult;
import com.nodes.scheduling.common.web.domain.response.ResultCode;
import com.nodes.scheduling.common.web.domain.response.ResultTool;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录，或者登录状态过期失效 Handler
 */
@Component
public class SecureAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        JsonResult<Object> result = ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        ServletUtil.write(JSON.toJSONString(result));
    }
}
