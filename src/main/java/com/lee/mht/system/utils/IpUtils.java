package com.lee.mht.system.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FucXing
 * @date 2022/01/08 18:17
 **/
public class IpUtils {
    private static final String[] IP_HEADER_CANDIDATES = {
            "x-real-ip",
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};
    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = "";
        for (String header : IP_HEADER_CANDIDATES) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader(header);
            }
        }
        if (ip != null && ip.length() > 15) { //"***.***.***.***".length() = 15
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
            return ip;
        }
        return ip;
    }
}
