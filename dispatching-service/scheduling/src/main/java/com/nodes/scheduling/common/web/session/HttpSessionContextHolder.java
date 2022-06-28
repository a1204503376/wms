package com.nodes.scheduling.common.web.session;


import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * HttpSessionContext对象持有者
 */
public class HttpSessionContextHolder {

    public static void removeSession(HttpSession httpSession) {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (null == servletRequestAttributes) {
            return;
        }
        servletRequestAttributes.removeAttribute(httpSession.getId(), RequestAttributes.SCOPE_SESSION);
    }

}
