package com.cyboperation.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;

import com.cyboperation.SubscribeService;
import com.cyboperation.engine.DLNAContainer;
import com.cyboperation.engine.SearchThread;
import com.cyboperation.util.LogUtil;
import com.irunsin.controller.util.IrunSinApplication;
import com.webservice.threadpac.HttpListnerThread;

 
/**
 * The service to search the DLNA Device in background all the time.
 * 
 * @author CharonChui
 * 
 */
public class DLNAService extends Service {
	private static final String TAG = "DLNAService";
	private ControlPoint mControlPoint;
	private SearchThread mSearchThread;
	private WifiStateReceiver mWifiStateReceiver;

	private ServerSocket serverSocket;
	
	private HttpListnerThread httplistner;
	private Thread subInfo;	
	private SubscribeService subscribeTh;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
		//删除所有已缓存的DEVICE
		deleteDevice();
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unInit();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//判断该线程是否存在   
		if(httplistner == null){
			httplistner = new HttpListnerThread(serverSocket,getApplicationContext());
			httplistner.setDaemon(true);
		}
		//判断线程是否活动
		if(!httplistner.isAlive()){
			httplistner.start();
		}
		
		startThread();
		return super.onStartCommand(intent, flags, startId);
	}

	private void init() {
		mControlPoint = new ControlPoint();
		((IrunSinApplication)getApplicationContext()).setControlpoint(mControlPoint);
		mSearchThread = new SearchThread(mControlPoint);
		registerWifiStateReceiver();
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(9876));
			//serverSocket.setSoTimeout(30000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//开启订阅接收结果线程
		if(subInfo == null){
			subscribeTh = new SubscribeService(getApplicationContext());
			subInfo = new Thread(subscribeTh);
			subInfo.setDaemon(true);
		}
		subInfo.start();
	}

	private void unInit() {
		stopThread();
		unregisterWifiStateReceiver();
	}

	/**
	 * Make the thread start to search devices.
	 */
	private void startThread() {
		if (mSearchThread != null) {
			LogUtil.d(TAG, "thread is not null");
			mSearchThread.setSearcTimes(0);
		} else {
			LogUtil.d(TAG, "thread is null, create a new thread");
			mSearchThread = new SearchThread(mControlPoint);
		}

		if (mSearchThread.isAlive()) {
			LogUtil.d(TAG, "thread is alive");
			mSearchThread.awake();
		} else {
			LogUtil.d(TAG, "start the thread");
			mSearchThread.start();
		}
	}

	private void stopThread() {
		if (mSearchThread != null) {
			mSearchThread.stopThread();
			mControlPoint.stop();
			mSearchThread = null;
			mControlPoint = null;
			LogUtil.w(TAG, "stop dlna service");
		}
		try {
			//关掉监听的socket端口
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//关闭订阅线程
		subscribeTh.stopThread();
		//关闭web服务线程
		httplistner.stopThread();
	}

	private void registerWifiStateReceiver() {
		if (mWifiStateReceiver == null) {
			mWifiStateReceiver = new WifiStateReceiver();
			registerReceiver(mWifiStateReceiver, new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION));
		}
	}

	private void unregisterWifiStateReceiver() {
		if (mWifiStateReceiver != null) {
			unregisterReceiver(mWifiStateReceiver);
			mWifiStateReceiver = null;
		}
	}

	private class WifiStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context c, Intent intent) {
			Bundle bundle = intent.getExtras();
			int statusInt = bundle.getInt("wifi_state");
			switch (statusInt) {
			case WifiManager.WIFI_STATE_UNKNOWN:
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				break;
			case WifiManager.WIFI_STATE_ENABLED:
				LogUtil.e(TAG, "wifi enable");
				startThread();
				break;
			case WifiManager.WIFI_STATE_DISABLING:
				break;
			case WifiManager.WIFI_STATE_DISABLED:
				LogUtil.e(TAG, "wifi disabled");
				break;
			default:
				break;
			}
		}
	}

	public void deleteDevice(){
		//获取所有已存的DEVICE
		List<Device> list = DLNAContainer.getInstance().getDevices();
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				DLNAContainer.getInstance().removeDevice(list.get(i));
			}
		}
	}
}