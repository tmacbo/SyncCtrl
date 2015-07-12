package com.webservice.threadpac;


import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;

/**
 * 
 * @author Administrator
 * 
 */
public class HttpListnerThread extends Thread {


    		

	private ServerSocket serverSocket;	
	private boolean _listenMark = true;
	
	private Context context;
	public HttpListnerThread(ServerSocket serverSocket,Context context) {
		this.serverSocket = serverSocket;		
		this.context = context;
	}

	public void run() {
		System.out.println("WEBSERVICE 的线程被调用了");
		while (_listenMark) {
			try {
				Socket clientSocket = serverSocket.accept();
				SessionThread sthread = new SessionThread(clientSocket,context);
				sthread.setDaemon(true);
				sthread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	
	public void stopThread(){
		_listenMark = false;
	}


}
