package com.banshi.utils;


import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class WebUtil {

    /**
     * 获取客户端用户IP
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (MyString.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (MyString.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (MyString.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个路由时，取第一个非unknown的ip
        if (MyString.isNotBlank(ip)) {
            final String[] arr = ip.split(",");
            for (final String str : arr) {
                if (!"unknown".equalsIgnoreCase(str)) {
                    ip = str;
                    break;
                }
            }
        }

        return ip;
    }

    /**
     * 获取本地IP
     *
     * @param request
     * @return
     */
    public static String getLocalIp(HttpServletRequest request) {

        Enumeration allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
        }
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    Logger.debug(WebUtil.class, String.format("本机的IP = %s",ip.getHostAddress()));
                }
            }
        }

        return ip.getHostAddress();
    }

    /**
     * 获取客户端用户浏览器信息
     *
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        String agentInfo = MyString.trimToLen(request.getHeader("User-Agent"), 512);
        return agentInfo;
    }
}
