package com.air.airkeeper.ditributeworker.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class LocalIpUtils {

    private static Logger logger = LoggerFactory.getLogger(LocalIpUtils.class);
    private static String localIp=null;
    //ip后四位
    private static String ipSubfix = null;

    public static  String getLocalIp() {
        if(localIp==null){
            localIp=getIp();
        }
        return  localIp;
    }

    public static  String getIpSubfix() {
        if( ipSubfix != null &&  ipSubfix!=""  ){
            return ipSubfix;
        }

        if(localIp==null){
            localIp=getIp();
        }

        if(StringUtils.isBlank(localIp)){
            ipSubfix = getDefaultIpSubfix();
            return ipSubfix;
        }

        try{
            String[] iArray = localIp.split("\\.");

            StringBuilder stringBuilder = new StringBuilder();

            String i = iArray[2];
            fillIp(i.length(),3,i,stringBuilder);

            i = iArray[3];
            fillIp(i.length(),3,i,stringBuilder);

            ipSubfix = stringBuilder.substring(2);
        }catch (Exception e){
            logger.error("getIpSubfix error",e);
            ipSubfix = getDefaultIpSubfix();
        }
        return  ipSubfix;
    }

    /**
     * 获取默认ip后缀
     * @return
     */
    private static String getDefaultIpSubfix(){
        String random = String.valueOf(System.currentTimeMillis());
        ipSubfix = random.substring(random.length()-7,random.length()-3);
        return ipSubfix;
    }

    /**
     * 填充内容，比如某个网段为1  填充为001
     * @param size
     * @param expectSize
     * @param ip
     * @param stringBuilder
     */
    private static  void fillIp( int size,int expectSize,String ip,StringBuilder stringBuilder){
        int forNum = expectSize - size;
        for (int i = 0; i < forNum; i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(ip);
    }

    /**
     * 根据网卡取本机配置的IP 如果是双网卡的，则取出外网IP
     *
     * @return
     */
    private static  String getIp() {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;// 是否找到外网IP
            while (netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
//                    logger.debug(ni.getName() + ";" + ip.getHostAddress()
//                            + ";ip.isSiteLocalAddress()=" + ip.isSiteLocalAddress() + ";ip.isLoopbackAddress()=" + ip.isLoopbackAddress());
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                        netip = ip.getHostAddress();
//                        logger.debug("===========" + netip);
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                        localip = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }
}