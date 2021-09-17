package io.github.quickmsg.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * IP帮助类
 *
 * @author zhaopeng
 */
public class IPUtils {

    /**
     * 获取IP地址
     *
     * @return {@link String}
     */
    public static String getIP() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip != null && (ip instanceof Inet4Address)) {
                        String retIp = ip.getHostAddress();
                        if (!"127.0.0.1".equals(retIp)) {
                            return retIp;
                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}
