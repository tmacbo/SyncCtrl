package com.irunsin.controller.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiUtil {
	  private static  String TAG = "WifiUtil";
	
	  public static WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
	        Log.i("WifiUtil", "SSID:" + SSID + ",password:" + Password);
	        WifiConfiguration config = new WifiConfiguration();
	        config.allowedAuthAlgorithms.clear();
	        config.allowedGroupCiphers.clear();
	        config.allowedKeyManagement.clear();
	        config.allowedPairwiseCiphers.clear();
	        config.allowedProtocols.clear();
	        config.SSID = "\"" + SSID + "\"";	        
	        if (Type == 1) // WIFICIPHER_NOPASS
	        {
	            Log.i(TAG, "Type =1.");
//	            config.wepKeys[0] = "\""+"\"";
//	            config.hiddenSSID = true;
	            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//	            config.wepTxKeyIndex = 0;
	        }
	        if (Type == 2) // WIFICIPHER_WEP
	        {
	            Log.i(TAG, "Type =2.");
	            config.hiddenSSID = true;
	            config.wepKeys[0] = "\"" + Password + "\"";
	            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
	            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	            config.wepTxKeyIndex = 0;
	        }
	        if (Type == 3) // WIFICIPHER_WPA
	        {
	 
	            Log.i(TAG, "Type =3.");
	            config.preSharedKey = "\"" + Password + "\"";
	 
	            config.hiddenSSID = true;
	            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	            config.status = WifiConfiguration.Status.ENABLED;
	        }
	        return config;
	    }
	  
	     //查看以前是否也配置过这个网络  
	     public static WifiConfiguration IsExsits(String SSID,WifiManager wifiManager)  
	     {  
	         List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();  
	            for (WifiConfiguration existingConfig : existingConfigs)   
	            {  
	              String ssid = existingConfig.SSID;
	              if (existingConfig.SSID.equals(SSID))  
	              {  
	                  return existingConfig;  
	              }  
	            }  
	         return null;   
	     }  
	     
	     
	     /**
	 	 * 打开wifi  幷获取对应uuid开头的信号源头
	 	 */
	 	public static List<ScanResult> getDeviceList(WifiManager mWifiManager){
	 		// 打开WLAN
	 		if (!mWifiManager.isWifiEnabled()) {
	 			mWifiManager.setWifiEnabled(true);
	 		}
	 		// 扫描热点	 		
	 		List<ScanResult> tempList = new ArrayList<ScanResult>();
	 		mWifiManager.startScan();
	 		tempList = mWifiManager.getScanResults();
	 		
	 		return tempList;
	 	}
	 	
	    /** 
	     * make true current connect service is wifi 
	     * @param mContext 
	     * @return 
	     */  
	    public static boolean isWifi(Context mContext) {  
	        ConnectivityManager connectivityManager = (ConnectivityManager) mContext  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
	        if (activeNetInfo != null  
	                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
	            return true;  
	        }  
	        return false;  
	    } 	 
	    
	    public static boolean connectChoicWifi(WifiConfiguration mWifiConfiguration,WifiManager mWifiManager){
	    	WifiConfiguration tempConfig = WifiUtil.IsExsits(mWifiConfiguration.SSID, mWifiManager);  
			
			if(tempConfig != null)  
	        {  
				mWifiManager.removeNetwork(tempConfig.networkId);  
	        }  
			int netId = mWifiManager.addNetwork(mWifiConfiguration);
			boolean b = mWifiManager.enableNetwork(netId, true);
			
			return b;
	    }
	    
	    //获取本机IP地址
	    public static String getLocalIpAddress() {  
	         String ipaddress="";
	        
		     try {  
		         for (Enumeration<NetworkInterface> en = NetworkInterface  
		                 .getNetworkInterfaces(); en.hasMoreElements();) {  
		             NetworkInterface intf = en.nextElement();  
		             for (Enumeration<InetAddress> enumIpAddr = intf  
		                     .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
		                 InetAddress inetAddress = enumIpAddr.nextElement();  
		                 if (!inetAddress.isLoopbackAddress()) {  
		                         ipaddress=inetAddress.getHostAddress().toString();  
		                 }  
		             }  
		         }  
		     } catch (SocketException ex) {  
		         Log.e("WifiPreference IpAddress", ex.toString());  
		     }  
		     return ipaddress;
	     }
}
