/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: RenewSubscriber.java
*
*	Revision:
*
*	07/07/04
*		- first revision.
*	
******************************************************************/

package org.cybergarage.upnp.control;

import org.cybergarage.util.*;
import org.cybergarage.upnp.*;

import android.util.Log;

public class RenewSubscriber extends ThreadCore
{
	private String TAG = "RenewSubscriber";
	public final static long INTERVAL = 480;
	private static final CommonLog log = LogFactory.createNewLog("dlna_framework");
	private boolean flag = true;
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public RenewSubscriber(ControlPoint ctrlp)
	{
		setControlPoint(ctrlp);
	}
	
	////////////////////////////////////////////////
	//	Member
	////////////////////////////////////////////////

	private ControlPoint ctrlPoint;

	public void setControlPoint(ControlPoint ctrlp)
	{
		ctrlPoint = ctrlp;
	}
	
	public ControlPoint getControlPoint()
	{
		return ctrlPoint;
	}

	////////////////////////////////////////////////
	//	Thread
	////////////////////////////////////////////////
	
	public void run() 
	{
		ControlPoint ctrlp = getControlPoint();
		long renewInterval = INTERVAL * 1000;
		while (flag) {
			Log.i(TAG, "进入订阅续订线程");
			try {
				Thread.sleep(renewInterval);
			} catch (InterruptedException e1) {
			}
			
			try {
				long time1 = System.currentTimeMillis();
				ctrlp.renewSubscriberService();
				long time2 = System.currentTimeMillis();
	//			log.e("ctrlp.renewSubscriberService() cost time = " + (time2 - time1));
			} catch (Exception e) {
				log.e("catch exception!!!e = " + e.getMessage());
			}
		
		}
	}
	
	public void stopthead(){
		flag =false;
	}
}
