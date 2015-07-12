package com.cyboperation;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


import android.content.Context;
import android.util.Log;

public class SubscribeService implements Runnable{

	private static final String TAG = "SUBSCRIBESERVICE";
	private Context context;
	private boolean flag = true;
	public SubscribeService(Context context){
		this.context = context;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.i(TAG, "进入订阅接收结果处理线程");
		// TODO Auto-generated method stub
		ServerSocket serversocket = null;
		try {
			serversocket = new ServerSocket();
			serversocket.setReuseAddress(true);
			serversocket.bind(new InetSocketAddress(9743));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(flag){
			Socket clientSocket;
			
			try {
				clientSocket = serversocket.accept();
				//获得输入流
				//订阅信息解析的线程
				SubscribeSynThread thread = new SubscribeSynThread(context, clientSocket);
				thread.setDaemon(true);
				thread.start();				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public void stopThread(){
		flag = false;
	}
}
