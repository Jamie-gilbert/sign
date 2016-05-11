package chek_ins.com.sign.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class WifiUtil {
    private WifiManager wifiManager;
    private List<ScanResult> scanResultList;
    private List<WifiConfiguration> wifiConfigurationList;
    private WifiInfo wifiInfo;
    private DhcpInfo wifiDhcpInfo;

    /**
     * 初始化wifimanager
     *
     * @param context
     */
    public WifiUtil(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    /**
     * 获取手机连接过的wifi的信息
     * @return
     */
    public List<WifiConfiguration> getWifiConfigurationList() {
        wifiConfigurationList = wifiManager.getConfiguredNetworks();
        return wifiConfigurationList;
    }

    /**
     * 信息
     * 获取wifi的
     *
     * @return
     */
    public WifiInfo getWifiInfo() {
        wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo;
    }

    /**
     * 开始扫描wifi热点
     */
    public void scanWifi() {
        wifiManager.startScan();
    }

    /**
     * 获取扫描到的wifi
     *
     * @return
     */
    public List<ScanResult> getScanResultList() {

        scanResultList = wifiManager.getScanResults();
        return scanResultList;
    }

    /**
     * 获取android设备的IP地址，网关
     *
     * @return
     */
    public DhcpInfo getWifiDhcpInfo() {
        wifiDhcpInfo = wifiManager.getDhcpInfo();
        return wifiDhcpInfo;
    }

    /**
     * 开启热点作为服务端的配置
     *
     * @param ssid
     * @param password
     * @param type
     * @return
     */
    private WifiConfiguration getWifiAPConfiguration(String ssid, String password, int type) {
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.allowedAuthAlgorithms.clear();
        configuration.allowedGroupCiphers.clear();
        configuration.allowedKeyManagement.clear();
        configuration.allowedPairwiseCiphers.clear();
        configuration.allowedProtocols.clear();
        configuration.SSID = ssid;

        if (type == 1) //nopass
        {
            configuration.wepKeys[0] = "";
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        } else if (type == 2) //wep\
        {
            configuration.hiddenSSID = true;
            configuration.wepKeys[0] = password;
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            configuration.wepTxKeyIndex = 0;
        } else if (type == 3) //wpa
        {
            configuration.preSharedKey = password;
            configuration.hiddenSSID = true;
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            configuration.status = WifiConfiguration.Status.ENABLED;
        } else if (type == 4) //WPA2psk
        {
            configuration.preSharedKey = password;
            configuration.hiddenSSID = true;
            configuration.status = WifiConfiguration.Status.ENABLED;
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            configuration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);


        }

        return configuration;

    }

    /**
     * 客户端添加配置，作为连接认证配置
     * ssid、passwd 配置前后必须加上双引号“
     *
     * @param ssid
     * @param passwd
     * @param type
     * @return
     */
    public WifiConfiguration ConnectWifiConfiguration(String ssid, String passwd, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        //双引号必须
        config.SSID = "\"" + ssid + "\"";
        if (type == 1) // WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 2) // WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + passwd + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 3) // WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + passwd + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        if (type == 4) // WPA2psk test
        {
            config.preSharedKey = "\"" + passwd + "\"";
            config.hiddenSSID = true;

            config.status = WifiConfiguration.Status.ENABLED;
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

        }
        return config;

    }

    /**
     * 创建wifi热点
     *
     * @param ssid
     * @param password
     * @param type
     */
    public boolean openWifiAP(String ssid, String password, int type) {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
       return stratWifiAp(ssid, password, type);
    }

    /**
     * 启动热点
     *
     * @param ssid
     * @param password
     * @param type
     * @return
     */

    private boolean stratWifiAp(String ssid, String password, int type) {
        boolean flag = false;
        Method method = null;
        try {
            method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration netconfig = getWifiAPConfiguration(ssid, password, type);
            method.invoke(wifiManager, netconfig, true);
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    private boolean isWifiApEnabled() {
        boolean flag = false;
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);//类中的成员变量为private,故必须进行此操作

            flag = (boolean) method.invoke(wifiManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 关闭热点
     */
    public void closeWifiAp() {
        if (isWifiApEnabled()) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
                Method method1 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method1.invoke(wifiManager, configuration, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 添加网络
     *
     * @param configuration
     * @return
     */
    public boolean addNetWork(WifiConfiguration configuration) {
        int wcg = wifiManager.addNetwork(configuration);
        boolean b = wifiManager.enableNetwork(wcg, true);
        return b;
    }

    /**
     * 连接端 开启wifi
     */
    public void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 连接端 关闭wifi
     */
    public void closeWifi() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }
    /**
     * 将 int型的ip转换成 String类型
     *
     * @return
     */
    public String int2IP(int ipInt) {
        StringBuffer serverIp = new StringBuffer();
        serverIp.append(ipInt & 0xff);
        serverIp.append('.');
        serverIp.append((ipInt >> 8) & 0xff);
        serverIp.append('.');
        serverIp.append((ipInt >> 16) & 0xff);
        serverIp.append('.');
        serverIp.append((ipInt >> 24) & 0xff);
        return serverIp.toString();
    }
}
