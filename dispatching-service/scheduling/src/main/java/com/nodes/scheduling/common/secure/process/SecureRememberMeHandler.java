//package com.nodes.scheduling.common.secure.process;
//
//import com.nodes.scheduling.common.secure.session.SecureSessionService;
//import com.nodes.scheduling.service.SysUserService;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class SecureRememberMeHandler implements AuthenticationSuccessHandler {
//
////    @Resource
////    private SysLogService sysLogService;
//
//    @Resource
//    private SysUserService sysUserService;
//
//    @Resource
//    private SessionRegistry sessionRegistry;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        // 记录日志
////        SysLog sysLog = new SysLog();
////        sysLog.setId(SequenceUtil.makeStringId());
////        sysLog.setTitle("Remember Me");
////        sysLog.setDescription("登录成功");
////        sysLog.setBusinessType(BusinessType.OTHER);
////        sysLog.setSuccess(true);
////        sysLog.setLoggingType(LoggingType.LOGIN);
////        sysLogService.save(sysLog);
//
//        // 更新用户
////        SysUser sysUser = new SysUser();
////        // 获取最近登录时间
////        LocalDateTime now = LocalDateTime.now();
////        sysUser.setUserId(((SysUser) UserContext.currentUser()).getUserId());
////        sysUser.setLastTime(now);
////        sysUserService.updateById(sysUser);
////
////        SysUser currentUser = (SysUser) authentication.getPrincipal();
////        currentUser.setLastTime(now);
////        request.getSession().setAttribute("currentUser", currentUser);
//
//        SecureSessionService.expiredSession(request, sessionRegistry);
//
//        // 注册新的SessionInformation
//        sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
//    }
//}
