package com.cyboperation.engine;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.cyboperation.inter.IController;
import com.irunsin.controller.sys.Global;
import com.irunsin.controller.util.IrunSinApplication;

/**
 * 实现主控接口中的所有 控制方法
 * @author Administrator
 *
 */
public class MultiPointController implements IController {
	private String TAG = "MultiPointController";
	private static final String AVTransport1 = "urn:schemas-upnp-org:service:AVTransport:1";
	private static final String SetAVTransportURI = "SetAVTransportURI";
	private static final String RenderingControl = "urn:schemas-upnp-org:service:RenderingControl:1";
	private static final String ConnectManager = "urn:schemas-upnp-org:service:ConnectionManager:1";
	private static final String Play = "Play";

	private Context context;
	
	public MultiPointController(Context context){
		this.context = context;
	}
	
	@Override
	public boolean play(Device device, String path,String metaData) {
		
		
		//先stop
		stop(device);
		Service service = device.getService(AVTransport1);
		
		if (service == null) {
			return false;
		}		
		final Action action = service.getAction(SetAVTransportURI);
		if (action == null) {
			return false;
		}

		final Action playAction = service.getAction(Play);
		if (playAction == null) {
			return false;
		}

		if (TextUtils.isEmpty(path)) {
			return false;
		}
		
		action.setArgumentValue("InstanceID", 0);
		action.setArgumentValue("CurrentURI", path);
		action.setArgumentValue("CurrentURIMetaData", metaData);
		if (!action.postControlAction()) {
			//设置播放地址
			Log.i(TAG, "设置播放地址失败");
			return false;
		}

		playAction.setArgumentValue("InstanceID", 0);
		playAction.setArgumentValue("Speed", "1");
		return playAction.postControlAction();
	}

	@Override
	public boolean goon(String pausePosition) {
		Log.i(TAG, "进入重新播放方法");
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		Service localService = device.getService(AVTransport1);
		if (localService == null)
			return false;

		final Action localAction = localService.getAction("Seek");
		if (localAction == null)
			return false;
		localAction.setArgumentValue("InstanceID", "0");
		// if (mUseRelTime) {
		// } else {
		//localAction.setArgumentValue("Unit", "ABS_TIME");
		// }
		// LogUtil.e(tag, "缂傚倷缍�崨顖涙毎闂佺儵鏅濋…鍫ヮ敋椤曪拷绫嶉柛顐ｆ礃閿涚喖鏌ㄥ☉铏+mUseRelTime);
		// 濠电偞娼欓鍫ユ儊椤栨粍鍠嗛柨婵嗘噹閺嬶拷鏌熺紒銏犲箺闁哄倷绶氬鎶藉磼濮橆偆涓嶉梺鍛婅壘鐎涒晛顪冮崒鐐粹拻缂傚牏濮烽悷婵嬫煕閹存繃顥犻柍銉嫹		localAction.setArgumentValue("Unit", "ABS_TIME");
		localAction.setArgumentValue("Target", pausePosition);
		localAction.postControlAction();

		Action playAction = localService.getAction("Play");
		if (playAction == null) {
			return false;
		}

		playAction.setArgumentValue("InstanceID", 0);
		playAction.setArgumentValue("Speed", "1");
		return playAction.postControlAction();
	}

	@Override
	public String getTransportState() {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		if(device == null){
			return null;
		}
		Service localService = device.getService(AVTransport1);
		if (localService == null) {
			return null;
		}

		final Action localAction = localService.getAction("GetTransportInfo");
		if (localAction == null) {
			return null;
		}

		localAction.setArgumentValue("InstanceID", "0");

		if (localAction.postControlAction()) {
			return localAction.getArgumentValue("CurrentTransportState");
		} else {
			return null;
		}
	}

	public String getVolumeDbRange(Device device, String argument) {
		Service localService = device.getService(RenderingControl);
		if (localService == null) {
			return null;
		}
		Action localAction = localService.getAction("GetVolume");
		if (localAction == null) {
			return null;
		}
		localAction.setArgumentValue("InstanceID", "0");
		localAction.setArgumentValue("Channel", "Master");
		if (!localAction.postControlAction()) {
			return null;
		} else {
			return localAction.getArgumentValue(argument);
		}
	}

	@Override
	public int getMinVolumeValue() {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		if(device == null){
			return 0;
		}
		String minValue = getVolumeDbRange(device, "MinValue");
		if (TextUtils.isEmpty(minValue)) {
			return 0;
		}
		return Integer.parseInt(minValue);
	}

	@Override
	public int getMaxVolumeValue() {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		if(device == null){
			return 0;
		}
		String maxValue = getVolumeDbRange(device, "MaxValue");
		if (TextUtils.isEmpty(maxValue)) {
			return 100;
		}
		return Integer.parseInt(maxValue);
	}

	@Override
	public boolean seek( String targetPosition) {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		Service localService = device.getService(AVTransport1);
		if (localService == null)
			return false;

		Action localAction = localService.getAction("Seek");
		if (localAction == null) {
			return false;
		}
		localAction.setArgumentValue("InstanceID", "0");
		// if (mUseRelTime) {
		// localAction.setArgumentValue("Unit", "REL_TIME");
		// } else {
		localAction.setArgumentValue("Unit", "ABS_TIME");
		// }
		localAction.setArgumentValue("Target", targetPosition);
		boolean postControlAction = localAction.postControlAction();
		if (!postControlAction) {
			localAction.setArgumentValue("Unit", "REL_TIME");
			localAction.setArgumentValue("Target", targetPosition);
			return localAction.postControlAction();
		} else {
			return postControlAction;
		}

	}

	@Override
	public String getPositionInfo() {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		if(device == null){
			return null;
		}
		Service localService = device.getService(AVTransport1);

		if (localService == null)
			return null;

		final Action localAction = localService.getAction("GetPositionInfo");
		if (localAction == null) {
			return null;
		}

		localAction.setArgumentValue("InstanceID", "0");
		System.out.println("multipointcontrol 准备发送 获取设备信息");
		boolean isSuccess = localAction.postControlAction();
		if (isSuccess) {
			return localAction.getArgumentValue("AbsTime");
//			return localAction.getArgumentValue("RelTime");
		} else {
			return null;
		}
	}

	@Override
	public String getMediaDuration() {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		if(device == null){
			return null;
		}
		Service localService = device.getService(AVTransport1);
		if (localService == null) {
			return null;
		}

		final Action localAction = localService.getAction("GetMediaInfo");
		if (localAction == null) {
			return null;
		}

		localAction.setArgumentValue("InstanceID", "0");
		if (localAction.postControlAction()) {
			String d = localAction.getArgumentValue("CurrentURI");			
			return localAction.getArgumentValue("MediaDuration");
		} else {
			return null;
		}

	}
	
	/*
	 * getArgumentList
	 * (non-Javadoc)
	 * @see com.dmc.inter.IController#setMute(org.cybergarage.upnp.Device, java.lang.String)
	 */
	public void getArgumentList(){
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		Service localService = device.getService(AVTransport1);
		final Action localAction = localService.getAction("GetMediaInfo");
		localAction.setArgumentValue("InstanceID", "0");
		if (localAction.postControlAction()) {
			String a = localAction.getArgumentValue("PlayMedium");
			System.out.println("getArgumentList = " +a);
		} else {
			
		}
	}
	
	@Override
	public boolean setMute(Device mediaRenderDevice, String targetValue) {
		Service service = mediaRenderDevice.getService(RenderingControl);
		if (service == null) {
			return false;
		}
		final Action action = service.getAction("SetMute");
		if (action == null) {
			return false;
		}

		action.setArgumentValue("InstanceID", "0");
		action.setArgumentValue("Channel", "Master");
		action.setArgumentValue("DesiredMute", targetValue);
		return action.postControlAction();
	}

	@Override
	public String getMute(Device device) {
		Service service = device.getService(RenderingControl);
		if (service == null) {
			return null;
		}

		final Action getAction = service.getAction("GetMute");
		if (getAction == null) {
			return null;
		}
		getAction.setArgumentValue("InstanceID", "0");
		getAction.setArgumentValue("Channel", "Master");
		getAction.postControlAction();
		return getAction.getArgumentValue("CurrentMute");
	}

	@Override
	public boolean setVoice(int value) {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		if(device == null){
			return false;
		}
		Service service = device.getService(RenderingControl);
		if (service == null) {
			return false;
		}

		final Action action = service.getAction("SetVolume");
		if (action == null) {
			return false;
		}
		
		action.setArgumentValue("InstanceID", "0");
		action.setArgumentValue("Channel", "Master");
		action.setArgumentValue("DesiredVolume", value);
		action.setArgumentValue("RG_DEVICE_FLAG", "6C:8B:2F:A9:9B:9A");
		return action.postControlAction();

	}

	@Override
	public int getVoice() {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		Service service = device.getService(RenderingControl);
		if (service == null) {
			return -1;
		}

		final Action getAction = service.getAction("GetVolume");
		if (getAction == null) {
			return -1;
		}
		getAction.setArgumentValue("InstanceID", "0");
		getAction.setArgumentValue("Channel", "Master");
		if (getAction.postControlAction()) {
			return getAction.getArgumentIntegerValue("CurrentVolume");
		} else {
			return -1;
		}

	}

	@Override
	public boolean stop(Device device) {
		Service service = device.getService(AVTransport1);

		if (service == null) {
			return false;
		}
		final Action stopAction = service.getAction("Stop");
		if (stopAction == null) {
			return false;
		}

		stopAction.setArgumentValue("InstanceID", 0);
		return stopAction.postControlAction();

	}

	@Override
	public boolean pause() {
		Device mediaRenderDevice = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		Service service = mediaRenderDevice.getService(AVTransport1);
		if (service == null) {
			return false;
		}
		final Action pauseAction = service.getAction("Pause");
		if (pauseAction == null) {
			return false;
		}
		pauseAction.setArgumentValue("InstanceID", 0);
		return pauseAction.postControlAction();
	}

	/**
	 * 以下为新增扩展测试功能
	 */
	
	@Override
	public void getinfo() {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
//		Service service = device.getService("urn:schemas-sonos-com:service:Queue:1");
		Service service = device.getService(RenderingControl);
		//Service service = device.getService("urn:schemas-upnp-org:service:ConnectionManager:1");
					
		Action action = service.getAction("RG_GetEqualizer");
		action.setArgumentValue("InstanceID", 0);
//		action.setArgumentValue("CurrentURI", "http://12312321");
//		action.setArgumentValue("CurrentURIMetaData", 0);
		
		if (!action.postControlAction()) {
			String i ="nnnnnnnnnnnnnnnnnn";
		}
//		int a = action.getArgumentIntegerValue("PlayMode");
//		String b = action.getArgumentValue("PlayMode");
	}

	@Override
	public boolean setlist(String path) {
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		Service service = device.getService(AVTransport1);

		if (service == null) {
			return false;
		}

		final Action action = service.getAction(SetAVTransportURI);
		if (action == null) {
			return false;
		}

		action.setArgumentValue("InstanceID", 0);
		action.setArgumentValue("CurrentURI", path);
		action.setArgumentValue("CurrentURIMetaData", 0);
		if (!action.postControlAction()) {
			return false;
		}
		return true;
	}

	
	/**
	 * 设备事件订阅
	 */
	public  void  eventSub(){
		Log.i(TAG, "进入订阅处理方法");
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		if(device == null){
			Global.debug("gggggggggggggggggggggg  当前设备为空 取消订阅");
			return;
		}

		boolean state = ((IrunSinApplication)context.getApplicationContext()).getControlpoint().subscribe(device);	
		if(!state){
		
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 发送订阅失败 广播出去   发送错误广播出去
//			((IrunSinApplication)context.getApplicationContext()).setAvsubscribeState(false);
			}else{
//				((IrunSinApplication)context.getApplicationContext()).setAvsubscribeState(true);
			}
		
		}
	
	public  void  eventSub(Service service){
		boolean state = ((IrunSinApplication)context.getApplicationContext()).getControlpoint().subscribe(service);	
		if(!state){
			Log.i(TAG, "订阅失败 重新发起订阅");
			boolean secState = ((IrunSinApplication)context.getApplicationContext()).getControlpoint().subscribe(service);			
			if(!secState){
				Log.i(TAG, "订阅失败发送广播");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 发送订阅失败 广播出去   发送错误广播出去
//				Intent intent = new Intent();
//				intent.setAction("com.mp3.error");
//				context.sendBroadcast(intent);
				//续订失败 修改储存的状态
//				((IrunSinApplication)context.getApplicationContext()).setAvsubscribeState(false);
			}else{
				//订阅成功后修改储存的状态为成功
//				((IrunSinApplication)context.getApplicationContext()).setRcsubscribeState(true);
			}
		}
	}

	/**
	 *设备事件订阅取消 
	 */
	public void cancelSub(){
		Device device = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		if(device == null){
			Log.i(TAG,"当前设备为空");
			// 发送订阅失败 广播出去   发送错误广播出去
			Intent intent = new Intent();
			intent.setAction("com.mp3.error");
			context.sendBroadcast(intent);
			return;
		}
		//如果当前的订阅ID为空  则代表没有订阅   则推出
		if(((IrunSinApplication)context.getApplicationContext()).AVT_SUB_SID.equals("")){
			return;
		}
		Service service = device.getService(AVTransport1);
		boolean state = ((IrunSinApplication)context.getApplicationContext()).getControlpoint().unsubscribe(service);
		if(!state){
			boolean secState = ((IrunSinApplication)context.getApplicationContext()).getControlpoint().unsubscribe(service);
			if(!secState){
				// 发送订阅失败 广播出去   发送错误广播出去
//				Intent intent = new Intent();
//				intent.setAction("com.mp3.error");
//				context.sendBroadcast(intent);
			}
		}else{
			Service recservice = device.getService(RenderingControl);
			boolean recstate = ((IrunSinApplication)context.getApplicationContext()).getControlpoint().unsubscribe(recservice);
			if(!recstate){
				recstate = ((IrunSinApplication)context.getApplicationContext()).getControlpoint().unsubscribe(recservice);

			}
		}
	}
	
	/**
	 * 无效的SID取消订阅  降低设备资源占用
	 */
	public void cancelSub(String sid){
		
	}
}
