package com.irunsin.controller.sys;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;

/**
 * 维护一个线程池，其内维护了众多的Process对象
 * 
 * @author liukaifu
 * 
 */
public class ProcessManager
{
	private ProcessManager() {};

	private static ProcessManager instance = new ProcessManager();

	public static ProcessManager getInstance() {
		return instance;
	}

	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(3), new ThreadPoolExecutor.CallerRunsPolicy());

	public void addProcess(Runnable runnable) {
		threadPoolExecutor.execute(runnable);
	}
	

	public TextMsgProcess addProcess(Context context, RequestBean request, ProcessListener processListener) {
		TextMsgProcess run = null;
		try {
			String procssId = request.getRequestKey();
//			String url = request.getRequestUrl();
//			String encoding = request.getEncoding();
			String encoding = "UTF-8";
			String url= null;
			if (url == null){
				url = Address.getUrl(procssId);
			}
			run = new TextMsgProcess(context, procssId, url, request.getRequestStr(), request
					.getMessageParser(),encoding, processListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
		addProcess(run);
		return run;
	}

}
