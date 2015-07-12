package com.irunsin.controller.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.cybergarage.upnp.Device;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.cyboperation.engine.DlnaPackageImp;
import com.cyboperation.engine.MultiPointController;
import com.cyboperation.inter.IController;
import com.irunsin.controller.db.DbHelper;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.notifycation.IrunsinNotifycation;
import com.irunsin.controller.sys.Global;
import com.irunsin.controller.sys.ProcessManager;
import com.irunsin.controller.util.IrunSinApplication;

/**
 * 音乐播放器 播放 services
 * 
 * @author Administrator
 * 
 */
public class MusicPlayService extends Service {

	private String TAG = "MusicPlayService";
	// 定义控制器类
	private IController iControll;

	private IrunSinApplication storageApplication;

	private Device device;
	private IrunsinNotifycation irunsinnotify;
	
	private Notification status;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i("MusicPlayService", "调用音乐播放主服务");
		init();		
		//设置为前台服务
		if(irunsinnotify == null){
			irunsinnotify = new IrunsinNotifycation(getApplicationContext(), storageApplication, (NotificationManager) getSystemService(NOTIFICATION_SERVICE));
		}
		//得到通知栏notifycation
		Notification notification = irunsinnotify.getNotify("享受音乐,享受生活", "LT_MUSIC");		 
        //让该service前台运行，避免手机休眠时系统自动杀掉该服务
        //如果 id 为 0 ，那么状态栏的 notification 将不会显示。
        startForeground(200, notification);
		super.onCreate();
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("MusicPlayService", "音乐播放服务被调用");
		flags = START_NOT_STICKY;
		// 获取请求的状态信息 0 代表播放 1 代表暂停 2代表跃进 3代表下一首 4 代表上一首 5 重新播放 
		int playState;
		try {
			playState = intent.getExtras().getInt("FLAG");
			//判断设备对象
			if(device == null){
				if(storageApplication.getDevice()!=null){
					device = storageApplication.getDevice();
					iControll = new MultiPointController(getApplicationContext());	
				}else{
					Intent intent1 = new Intent();
					intent1.setAction("com.device.exception");
					sendBroadcast(intent1);
					return super.onStartCommand(intent, flags, startId);
				}
			}
			Global.debug("设备信息为 = " +device);
			
		} catch (Exception e) {
			Log.i(TAG, "估计就是进入了内存清理致使数据都为空了才导致此异常");
			return super.onStartCommand(intent, flags, startId);
		}
		switch (playState) {
		case 0:
			play(intent);
			break;
		case 1:			
			pause(device);
			break;
		case 2:
			int durration = intent.getExtras().getInt("current");
			seek(getStringLength(durration));
			break;
		case 3:
			playNext();
			break;
		case 4:
			playPre();
			break;
		case 5:
			restart();
			break;
		case 6:
			
			break;
		default:
			break;
		}

		return super.onStartCommand(intent, flags, startId);
	}

	public void play(Intent intent) {
		// 获取播放列表的位置的歌曲
		int position = intent.getExtras().getInt("position");
		int runn_Attri = 1;
		// 判断播放属性 是本机播放还是DLNA设备播放
		switch (runn_Attri) {
		case 0:
			break;
		case 1:
			DlnaMusicPlay(position);
			break;
		default:
			DlnaMusicPlay(position);
			break;
		}
	}



	/*
	 * 参数初始化方法
	 */
	public void init() {
		storageApplication = (IrunSinApplication) getApplicationContext();
		device = storageApplication.getDevice();
		iControll = new MultiPointController(getApplicationContext());		
	}

	/*
	 * 暂停播放
	 */
	public void pause(Device device) {
		ProcessManager.getInstance().addProcess(pausenable);
	}

	Runnable pausenable = new Runnable() {
		
		@Override
		public void run() {
			final boolean isSuccess = iControll.pause();
			if(isSuccess){
				storageApplication.setPLAY_STATE(1);
			}
		}
	};
	/*
	 * 重新播放
	 */
	public void restart() {
		ProcessManager.getInstance().addProcess(restartnable);
	}
	
	Runnable restartnable = new Runnable() {
		
		@Override
		public void run() {
			//调用开始播放的广播						
			String durration = iControll.getMediaDuration();
			boolean playflag = iControll.goon(durration);
			if(playflag){
				storageApplication.setPLAY_STATE(0);
				
			}else{
				Intent intent = new Intent();
				intent.setAction("com.mp3.false");
				sendBroadcast(intent);
			}
		}
	};
	
	/*
	 * 快进/倒退
	 */
	public void seek(String durration) {

	}

	/*
	 * 下一首
	 */
	public void playNext() {
		// storageApplication.setPLAY_MODE(0);
		// 判断音乐播放模式 0 单曲播放 1代表循环播放 2 代表随机播放
		if (storageApplication.getPLAY_MODE() == 0) {
			DlnaMusicPlay(storageApplication.getPosition());
		} else if (storageApplication.getPLAY_MODE() == 1) {
			// 获取列表总长度
			int totlength = storageApplication.getListmp3().size();
			if (totlength > 1) {
				if (storageApplication.getPosition() < totlength - 1) {
					// 累加对应音乐位置
					storageApplication.setPosition(storageApplication
							.getPosition() + 1);
					DlnaMusicPlay(storageApplication.getPosition());
				} else {
					storageApplication.setPosition(0);
					DlnaMusicPlay(storageApplication.getPosition());
				}
			} else {
				// 总列表只有一首 故只播放当前
				DlnaMusicPlay(storageApplication.getPosition());
			}
		} else if (storageApplication.getPLAY_MODE() == 2) {
			// 获取列表总长度
			int totlength = storageApplication.getListmp3().size();
			// 生成随机数
			int playlength = getRandomNum(totlength);
			storageApplication.setPosition(playlength);
			DlnaMusicPlay(playlength);
		}
	}

	/*
	 * 生成随机数的方法
	 */
	public int getRandomNum(int max) {
		int min = 0;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	/*
	 * 上一首
	 */
	public void playPre() {
		// 判断音乐播放模式 0 单曲播放 1代表循环播放 2 代表随机播放
		if (storageApplication.getPLAY_MODE() == 0) {
			DlnaMusicPlay(storageApplication.getPosition());
		} else if (storageApplication.getPLAY_MODE() == 1) {
			// 获取列表总长度
			int totlength = storageApplication.getListmp3().size();
			if (totlength > 1) {
				if (storageApplication.getPosition() > 1) {
					// 累加对应音乐位置
					storageApplication.setPosition(storageApplication
							.getPosition() - 1);
					DlnaMusicPlay(storageApplication.getPosition());
				} else if (storageApplication.getPosition() == 0) {
					storageApplication.setPosition(totlength);
					DlnaMusicPlay(storageApplication.getPosition());
				} else {
					storageApplication.setPosition(0);
					DlnaMusicPlay(storageApplication.getPosition());
				}
			} else {
				// 总列表只有一首 故只播放当前
				DlnaMusicPlay(storageApplication.getPosition());
			}
		} else if (storageApplication.getPLAY_MODE() == 2) {
			// 获取列表总长度
			int totlength = storageApplication.getListmp3().size();
			// 生成随机数
			int playlength = getRandomNum(totlength);
			storageApplication.setPosition(playlength);
			DlnaMusicPlay(playlength);
		}
	}

	// DLNA设备音乐播放
	public void DlnaMusicPlay(int position) {

		// 判断当前播放频率
		long oldtime = storageApplication.getPlaytime();			
		storageApplication.setPosition(position);
		Mp3Info playingmp3 = storageApplication.getListmp3().get(position);
		storageApplication.setMp3info(storageApplication.getListmp3().get(
				position));
		
		String nowtimeStr = new SimpleDateFormat("MMddHHmmssSSS")
		.format(new Date());
		long nowtime = Long.parseLong(nowtimeStr);
		if (oldtime == 0 || nowtime > (oldtime + 3000)) {
			storageApplication.setPosition(position);
			storageApplication.setMp3info(storageApplication.getListmp3().get(
					position));
			//添加至线程池中播放
			ProcessManager.getInstance().addProcess(dlnaPushMp3);
			
			storageApplication.setPlaytime(nowtime);
			storageApplication.setSOFT_STATE(true);// 修改为当前手机播放
			DbHelper.getInstence(getApplicationContext()).insertrecently(
					playingmp3);
										        
		} else {
			Intent intent = new Intent();
			intent.setAction("com.mp3.frecontrol");
			sendBroadcast(intent);
		}
				
	}

	//开启线程去控制DLNA设备播放音乐
	Runnable dlnaPushMp3 = new Runnable() {		
		@Override
		public void run() {		
			
			Global.debug("musicplay begin");		
			//调用DLNA 设备音乐播放的方法
			Mp3Info mp3 = storageApplication.getListmp3().get(storageApplication.getPosition());
			boolean playflag = DlnaPackageImp.pushMp3(mp3, null, mp3.getLoOnFlag(), getApplicationContext());	
			System.out.println("Music PLAY Service调用播放结果状态如下="+playflag);
			if(playflag){
//					storageApplication.setPLAY_STATE(0);	
				//调用开始播放的广播
				broadBeginFlag();	
			}else{
				Intent intent = new Intent();
				intent.setAction("com.mp3.false");
				sendBroadcast(intent);
			}
			
		}
	};

	/*
	 * 广播已经开始播放音乐
	 */
	public void broadBeginFlag() {

//		Intent intent = new Intent();
//		intent.putExtra("mp3info", storageApplication.getPlayingMp3());
//		intent.setAction("com.mp3.begin");
//		sendBroadcast(intent);

	}

	
	/*
	 * 广播暂停播放
	 */
	public void broadPauseFlag() {

		Intent intent = new Intent();
		intent.setAction("com.mp3.pause");
		sendBroadcast(intent);

	}

	/*
	 * 广播播放失败
	 */
	public void broadFalseFlag() {
		Intent intent = new Intent();
		intent.setAction("com.mp3.false");
		sendBroadcast(intent);
	}

	/**
	 * 由秒转换成对应的时间 xxx to xx:xx:xx 以秒为单位
	 */
	private String getStringLength(int length) {
		if (length <= 0) {
			return "";
		}
		int hour = 0;
		int minute = 0;
		int reconds;
		if (length >= 3600) {
			hour = length / 3600;
		}
		String h;
		if (hour > 0 && hour > 10) {
			h = hour + "";
		} else {
			h = "0" + hour;
		}
		minute = (length - hour * 3600) / 60;
		String m;
		if (minute > 0 && minute > 10) {
			m = minute + "";
		} else {
			m = "0" + minute;
		}
		reconds = length - hour * 3600 - minute * 60;
		String r;
		if (reconds > 0 && reconds > 10) {
			r = reconds + "";
		} else {
			r = "0" + reconds;
		}

		String string = h + ":" + m + ":" + r;
		return string;
	}

	public void onDestroy() {
		Log.i("MusicPlayService", "进入当前服务销毁");
		//取消前台运行服务
		stopForeground(true);
		super.onDestroy();
	};

}
