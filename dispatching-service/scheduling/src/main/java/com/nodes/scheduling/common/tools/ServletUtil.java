package com.nodes.scheduling.common.tools;

import com.alibaba.fastjson.JSON;
import com.nodes.scheduling.common.constant.ServletConstant;
import com.nodes.scheduling.common.constant.SystemConstant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Servlet 工具类
 */
public class ServletUtil {

    /**
     * 获取 HttpServletRequest 对象
     * Param null
     * Return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取 HttpServletResponse 对象
     * Param null
     * Return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getResponse();
    }

    /**
     * 获取 HttpServletSession 对象
     * Param null
     * Return HttpServletSession
     */
    public static HttpSession getSession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest().getSession();
    }

    /**
     * 判断是否为 Ajax 请求
     * Param null
     * Return HttpServletSession
     */
    public static Boolean isAjax(HttpServletRequest request) {
        String requestType = request.getHeader(ServletConstant.Header.X_REQUESTED_WITH);
        if (ServletConstant.Header.XML_HTTP_REQUEST.equals(requestType)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Response 对象写出数据
     * Param: msg 消息数据
     * Return null
     */
    public static void write(String msg) throws IOException {
        HttpServletResponse response = getResponse();
        response.setHeader(ServletConstant.Header.CONTENT_TYPE, ServletConstant.JSON_UTF8);
        response.setCharacterEncoding(SystemConstant.UTF8);
        response.getWriter().write(msg);
    }

    /**
     * Response 对象写出 JSON 数据
     *
     * @param: object 消息数据
     */
    public static void writeJson(Object data) throws IOException {
        write(JSON.toJSONString(data));
    }


    /**
     * 获取查询参数
     */
    public static String getQueryParam() {
        return getRequest().getQueryString();
    }

    /**
     * 获取请求地址
     */
    public static String getRequestURI() {
        return getRequest().getRequestURI();
    }

    /**
     * 获取客户端地址
     */
    public static String getRemoteHost() {
        String remoteHost = getRequest().getRemoteHost();
        if (ServletConstant.REMOTE_HOST_0.equals(remoteHost)) {
            remoteHost = ServletConstant.REMOTE_HOST_127;
        }
        return remoteHost;
    }

    /**
     * 获取当前请求方法
     */
    public static String getMethod() {
        return getRequest().getMethod();
    }

    /**
     * 获取请求头
     */
    public static String getHeader(String name) {
        return getRequest().getHeader(name);
    }

    /**
     * 获取 UserAgent
     */
    public static String getAgent() {
        return getHeader(ServletConstant.Header.UA);
    }

    /**
     * 获取浏览器类型
     */
    public static String getBrowser() {
        String userAgent = getAgent();
        if (userAgent.contains(ServletConstant.Browser.FIRE_FOX_UA)) {
            return ServletConstant.Browser.FIRE_FOX_NAME;
        } else if (userAgent.contains(ServletConstant.Browser.CHROME_UA)) {
            return ServletConstant.Browser.CHROME_NAME;
        } else if (userAgent.contains(ServletConstant.Browser.IE_UA)) {
            return ServletConstant.Browser.IE_NAME;
        } else {
            return ServletConstant.Browser.UNKNOWN;
        }
    }

    /**
     * 获取浏览器类型
     */
    public static String getSystem() {
        String userAgent = getAgent();
        if (userAgent.toLowerCase().contains(ServletConstant.System.WIN_UA)) {
            return ServletConstant.System.WIN_NAME;
        } else if (userAgent.toLowerCase().contains(ServletConstant.System.MAC_UA)) {
            return ServletConstant.System.MAC_NAME;
        } else if (userAgent.toLowerCase().contains(ServletConstant.System.UNIX_UA)) {
            return ServletConstant.System.UNIX_NAME;
        } else if (userAgent.toLowerCase().contains(ServletConstant.System.ANDROID_UA)) {
            return ServletConstant.System.ANDROID_NAME;
        } else if (userAgent.toLowerCase().contains(ServletConstant.System.IPHONE_UA)) {
            return ServletConstant.System.IPHONE_NAME;
        } else {
            return ServletConstant.System.UNKNOWN + userAgent;
        }
    }

    /**
     * 获取客户端来源
     */
    public static String getIpAddress() {
        HttpServletRequest request = getRequest();
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                try {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        if (ipAddress != null && ipAddress.length() > 15) {
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

}
