//package com.nodes.scheduling.common.secure.process;
//
//import com.alibaba.fastjson.JSON;
//import com.nodes.scheduling.common.tools.ServletUtil;
//import com.nodes.scheduling.common.web.domain.response.Result;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Security 无权限处理 Handler
// */
//@Component
//public class SecureAccessDeniedHandler implements AccessDeniedHandler {
//
//    @Override
//    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
//        if (ServletUtil.isAjax(httpServletRequest)) {
//            Result<Object> result = Result.failure(403, "暂无权限");
//            ServletUtil.write(JSON.toJSONString(result));
//        } else {
//            httpServletResponse.sendRedirect("/error/403");
//        }
//    }
//}
