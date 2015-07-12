package com.irunsin.controller.notifycation;

import java.io.InputStream;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.irunsin.controller.activity.LoadingActivity;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.service.MusicPlayService;
import com.irunsin.controller.sys.Global;
import com.irunsin.controller.sys.NetworkManager;
import com.irunsin.controller.util.IrunSinApplication;
import com.irunsin.controller.util.Mp3Util;
import com.irunsin.syncctrl.R;

/**
 * 通知栏
 * @author Administrator
 *
 */
public class IrunsinNotifycation {
	
	public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";
	public final static String INTENT_BUTTONID_TAG = "ButtonId";
	/** 上一首 按钮点击 ID */
	public final static int BUTTON_PREV_ID = 1;
	/** 播放/暂停 按钮点击 ID */
	public final static int BUTTON_PALY_ID = 2;
	/** 下一首 按钮点击 ID */
	public final static int BUTTON_NEXT_ID = 3;
	
	private Notification notify;
	public NotificationManager mNotificationManager;

	private Context context;
	private IrunSinApplication irunsin;
	public IrunsinNotifycation(Context context,IrunSinApplication irunsin,NotificationManager mNotificationManager){
		this.context = context;
		this.irunsin = irunsin;
		this.mNotificationManager = mNotificationManager;
		init();
	}
	
	
	/*
	 * 初始化
	 */
	public void init(){
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mp3.notify");
		filter.addAction("com.mp3.begin");
		filter.addAction("com.mp3.notify");
		filter.addAction(ACTION_BUTTON); // 通知栏 点击触发广播
		context.registerReceiver(notifyReceiver, filter);		
	}
	
	public void destory(){
		context.unregisterReceiver(notifyReceiver);
	}
	
	protected BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(notify == null){
				//只为启动music服务
				Intent intent1 = new Intent();
				intent1.putExtra("FLAG", 6);
				intent1.setClass(context, MusicPlayService.class);
				context.startService(intent1);
				return;
			}
			String action = intent.getAction();
			// 收到播放的广播的时候 则开启 进度条线程
			if (action.endsWith("com.mp3.notify")) {
				String mp3name = intent.getExtras().getString("mp3name");
				String singername = intent.getExtras().getString("author");
				//重新创建remoteview				
				if(notify!=null){
					RemoteViews remoteView = createRemote(mp3name, singername);
					notify.contentView = remoteView;
				}else{
					showNotify(mp3name,singername);
				}
				//开启AsyncTask更新图片
				new GetPicTask().execute();
			}else if(action.equals("com.mp3.begin")){
				changeNotify(true);	
			}else if(action.equals("com.mp3.pause")){
				changeNotify(false);
			}
			else if (action.equals(ACTION_BUTTON)) {
				// 通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
				int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
				switch (buttonId) {
				case BUTTON_PALY_ID:// 开始 暂停
					if (irunsin.getPLAY_STATE() == 1) {
						restart(context, irunsin.getPosition());
					} else if (irunsin.getPLAY_STATE() == 0) {
						stop(context);
					} else {
						Toast.makeText(context, "当前无法控制",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case BUTTON_NEXT_ID:// 下一首
					playNext();
					break;
				default:
					break;
				}
			}
		}
	};
	
	//得到notify
	public  Notification getNotify(String mp3name,String singername){
		Notification notification = new Notification();
		NotificationCompat.Builder mBuilder = new Builder(context);
		//创建remoteview
		RemoteViews mRemoteViews = createRemote(mp3name,singername);		
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(context, LoadingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		mBuilder.setContent(mRemoteViews)
				.setWhen(System.currentTimeMillis())				
				.setPriority(Notification.PARCELABLE_WRITE_RETURN_VALUE)
				.setOngoing(true).setSmallIcon(R.drawable.ic_launcher);
		notification = mBuilder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.contentIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);	
		notify = notification;//赋值
		return notification;
	}
	
	// 显示常驻的通知栏
		public void showNotify(String mp3name, String singername) {
			if (notify == null) {
				Global.debug("进入通知处理的方法");
				NotificationCompat.Builder mBuilder = new Builder(context);
				//创建remoteview
				RemoteViews mRemoteViews = createRemote(mp3name,singername);		
				
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setClass(context, LoadingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				mBuilder.setContent(mRemoteViews)
						.setWhen(System.currentTimeMillis())
						.setPriority(Notification.PARCELABLE_WRITE_RETURN_VALUE)
						.setOngoing(true).setSmallIcon(R.drawable.ic_launcher);
				notify = mBuilder.build();
				notify.flags = Notification.FLAG_ONGOING_EVENT;
				notify.contentIntent = PendingIntent.getActivity(context, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);			
				mNotificationManager.notify(200, notify);
			} else {
				changeNotify(mp3name, singername);
			}
		}
		
		//创建remoteview
		public RemoteViews createRemote(String mp3name,String singername){
			
			RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(),
					R.layout.view_notify_item);
			mRemoteViews.setTextViewText(R.id.view_song_name, mp3name);
			mRemoteViews.setTextViewText(R.id.view_singer_name, singername);

			// 点击的事件处理
			Intent buttonIntent = new Intent(ACTION_BUTTON);
			/* 播放/暂停 按钮 */
			buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
			PendingIntent intent_paly = PendingIntent.getBroadcast(context, 2,
					buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mRemoteViews
					.setOnClickPendingIntent(R.id.view_iv_play, intent_paly);
			/* 下一首 按钮 */
			buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
			PendingIntent intent_next = PendingIntent.getBroadcast(context, 3,
					buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mRemoteViews.setOnClickPendingIntent(R.id.view_play_next,
					intent_next);

			return mRemoteViews;
		}
		
	// 更改通知栏中的图片
		public void changeNotifyPic(Bitmap bitmap) {
			try {			
				Global.debug("tttttttttttttt准备去更新通知栏图片");
				Bitmap updatebit = Mp3Util.dealPic(bitmap, context);						
				if(updatebit == null){
					notify.contentView.setImageViewResource(R.id.view_song_icon, R.drawable.ic_launcher); 
				}else{
					notify.contentView.setImageViewBitmap(R.id.view_song_icon, updatebit);
				}			
				Global.debug("notify的字节数为 = " +notify.toString().getBytes().length);
				mNotificationManager.notify(200, notify);									
			} catch (Exception e) {
				Global.debug("tttttttttttttt更改通知栏图片异常");				
				e.printStackTrace();
			}
		}
		// 更改通知栏中的歌曲和歌手
		public void changeNotify(String songname, String singername) {
			try {
				Global.debug("tttttttttttttt准备去更新通知栏名称");
				notify.contentView.setTextViewText(R.id.view_song_name, songname);
				notify.contentView.setTextViewText(R.id.view_singer_name,
						singername);
				mNotificationManager.notify(200, notify);
			} catch (Exception e) {
				Global.debug("tttttttttttttt更改通知栏名称异常");				
				e.printStackTrace();
			}
		}

		// 更改通知栏目中 暂停和播放
		public void changeNotify(boolean playstate) {
			if (notify != null) {
				if (playstate) {// 代表播放
					notify.contentView.setImageViewResource(R.id.view_iv_play,
							R.drawable.play);
				} else {// 代表暂停
					notify.contentView.setImageViewResource(R.id.view_iv_play,
							R.drawable.suspend);
				}
				mNotificationManager.notify(200, notify);
			}
		}
	/*
	 * 开始播放
	 */
	public void restart(Context context, int position) {

		Intent intent = new Intent();
		intent.putExtra("position", position);
		intent.putExtra("FLAG", 5);
		intent.setClass(context, MusicPlayService.class);
		context.startService(intent);

	}

	/*
	 * 暂停播放
	 */
	public void stop(Context context) {
		Intent intent = new Intent();
		intent.putExtra("FLAG", 1);
		intent.setClass(context, MusicPlayService.class);
		context.startService(intent);
	}

	// 播放主界面中调用的方法
	/*
	 * 下一首
	 */
	public void playNext() {
		if (irunsin.getListmp3() == null) {
			Toast.makeText(context, "当前播放列表为空,请先去选择播放列表,谢谢！",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent();
			intent.putExtra("FLAG", 3);
			intent.setClass(context, MusicPlayService.class);
			context.startService(intent);
		}
	}
	
	
	private class GetPicTask extends AsyncTask<Void,Void,Bitmap>{

		@Override
		protected Bitmap doInBackground(Void... params) {
			Mp3Info mp3 = irunsin.getMp3info();
			Bitmap update = null;
			if(mp3!=null){
				if (mp3.getLoOnFlag() == 1 && !mp3.getLogo().equals("")) {
					// 通过网络去获取图片
					
					InputStream bitmapIs = NetworkManager.getStreamFromURL(mp3
							.getLogo());
					update = BitmapFactory.decodeStream(bitmapIs);
		
				}else if(mp3.getLoOnFlag() == 0 && mp3.getFilepath()!=null){
					update = Mp3Util.getArtwork(context,
							mp3.getFilepath());
					if (update == null) {
						update = Mp3Util.getDefaultArtwork(context);					
					}
				}
			}
						
			return update;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			if(result != null){
				changeNotifyPic(result);
			}
			super.onPostExecute(result);
		}
	}
}
