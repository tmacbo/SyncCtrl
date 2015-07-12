package com.cyboperation.util;

import org.cybergarage.upnp.Device;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.irunsin.controller.model.Mp3Info;

public class DLNAUtil {
	private static final String MEDIARENDER = "urn:schemas-upnp-org:device:MediaRenderer:1";
	
	private static final String SONOSRENDER = "urn:schemas-upnp-org:device:ZonePlayer:1";
	private static String httpFront = "http://";
	

	/**
	 * Check if the device is a media render device
	 * 
	 * @param device
	 * @return
	 */
	public static boolean isMediaRenderDevice(Device device) {
		if (device != null
				&& (MEDIARENDER.equalsIgnoreCase(device.getDeviceType())||SONOSRENDER.equalsIgnoreCase(device.getDeviceType()))) {
			return true;
		}

		return false;
	}

	// 拼接传递出去的DLNA设备访问本机的路径    拼接规则为   path=访问路径&mp3name=mp3name&author=author
	public static String getMp3FilePath(Mp3Info mp3info,Context context) {
		String httpUrl = "";
		
		//拼接访问 MP3路径
//		String mp3Visit = "path=" + mp3info.getFilepath() +"&mp3name=" +mp3info.getMp3Name() +"&author="+mp3info.getArtist();
		String mp3Visit = mp3info.getMp3id();
		String ip = getIp(context);
		if(ip!=null){
			httpUrl = httpFront + ip + ":" + "9876" + "/" + mp3Visit;
		}
		return httpUrl;
	}

	// 获取当前的内网IP地址
	public static String getIp(Context context) {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
//			wifiManager.setWifiEnabled(true);
			//当网络连接设置为关闭时返回空
			return null;
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp(ipAddress);
		System.out.println("ipAddress = " + ip);
		return ip;
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
	
}
