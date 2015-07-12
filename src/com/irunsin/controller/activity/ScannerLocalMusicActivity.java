package com.irunsin.controller.activity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.AudioFileIO;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.irunsin.controller.db.DbHelper;
import com.irunsin.controller.fragment.LocalMusicFragment;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.sys.BaseActivity;
import com.irunsin.controller.util.Mp3Util;
import com.irunsin.syncctrl.R;

public class ScannerLocalMusicActivity extends BaseActivity {

	private String[] filePaths = null;

	private Button scanButton;
	private ImageView scanImage;
	private IntentFilter intentfilter;
	private AnimationDrawable anima;
	private ScanSdReceiver scanSdReceiver;
	private TextView scan_over_show;

	private int scanState = 0; // 标志扫描按钮的状态 0开始扫描 1 取消扫描 2返回本地音乐

	public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

	private final int LINK_FINSHED = 2;

	private String TAG = "ScannerLocalMusic";

	private File filePath = null;
	private String fileType = null;


	private int times;
	private int t;

	private int oldcount;
	private ImageButton scannerBack;

	@Override
	public void call(int callID, Object[] objs) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner_local);
		initData();
		findView();
		addAction();
	}

	public void initData() {

		intentfilter = new IntentFilter();
		intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentfilter.addDataScheme("file");
		scanSdReceiver = new ScanSdReceiver();

		
		times = 0;
		
		//注册一个广播来接收更新速度数据  更新速度通过文件夹的扫描率来计算
		IntentFilter intentfil = new IntentFilter();
		intentfil.addAction("com.scann.speed");
		this.registerReceiver(scannSpeedReciver, intentfil);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(scannSpeedReciver);
	}
	
	public void findView() {
		scan_over_show = (TextView) findViewById(R.id.scan_over_text);
		scanButton = (Button) findViewById(R.id.scanner_button);
		scanImage = (ImageView) findViewById(R.id.scanner_image);
		scanImage.setBackgroundResource(R.drawable.scan_local);
		anima = (AnimationDrawable) scanImage.getBackground();
		anima.setOneShot(false);
		scannerBack = (ImageButton) findViewById(R.id.scanner_set_back);
	}

	public void addAction() {
		scanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (scanState == 0) {

					scanfile(new File(Environment.getExternalStorageDirectory()
							.getAbsolutePath()));
					// 赋值刷新以前的音乐数量
					oldcount = DbHelper.getInstence(getApplicationContext())
							.getLocalMusicList().size();
					anima.start();
					scanButton.setText("停止扫描");
					scanState = 1;

				} else if (scanState == 1) {
					anima.stop();
					scanButton.setText("开始扫描");
					scanState = 0;
				} else if (scanState == 2) {
					Intent intent = new Intent(ScannerLocalMusicActivity.this,LocalMusicFragment.class);
					setResult(12,intent);
					ScannerLocalMusicActivity.this.finish();
				}

			}
		});

		scannerBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ScannerLocalMusicActivity.this.finish();
			}
		});

	}

	private Handler refreshShow = new Handler() {
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				// 查询更新后系统库中的音乐列表
				List<Mp3Info> list = DbHelper.getInstence(getApplicationContext()).getLocalMusicList();
				int newcount = list.size();
				int updatecount = 0;
				String addString = "";
				if (newcount > oldcount) {
					updatecount = newcount - oldcount;
					addString = "添加了" + updatecount + "首新增歌曲";
				} else if (newcount < oldcount) {
					updatecount = oldcount - newcount;
					addString = "去除了" + updatecount + "首废弃歌曲";
				}
				String showString = "当前工扫描到" + newcount + "首歌曲         "
						+ addString;
				// 更新扫描到的歌曲至于本地库中
				
				scan_over_show.setText(showString);
				scanButton.setText("返回本地音乐");
				scanState = 2;
				anima.stop();

				break;
			default:
				break;
			}
		}
	};

	public class ScanSdReceiver extends BroadcastReceiver {

		private int count2;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
				Cursor c2 = context.getContentResolver().query(
						MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Audio.Media.TITLE,
								MediaStore.Audio.Media.DURATION,
								MediaStore.Audio.Media.ARTIST,
								MediaStore.Audio.Media._ID,
								MediaStore.Audio.Media.DISPLAY_NAME }, null,
						null, null);
				count2 = c2.getCount();
				String showString = "当前工扫描到" + count2 + "首歌曲";
				scan_over_show.setText(showString);
				scanButton.setText("返回本地音乐");
				scanState = 2;
				anima.stop();
			}
		}
	}

	class MusicSannerClient implements
			MediaScannerConnection.MediaScannerConnectionClient {

		public void onMediaScannerConnected() {
			Log.e("---------", "media service connected");

			if (filePath != null) {
				new Thread(refreshLocalMusic).start();
			}

		}

		public void onScanCompleted(String path, Uri uri) {
			// TODO Auto-generated method stub
			Log.i(TAG, "进入结束连接处理的方法");
		}

	}

	public void scannfile(File file) {
		AudioFileIO afio = new AudioFileIO();
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						scannfile(files[i]);
						Log.i(TAG, "进入文件夹处理");
					} else {
						times++;						
						if (files[i].toString().endsWith("mp3") 
								|| files[i].toString().endsWith("ape")
								|| files[i].toString().endsWith("flac")
								|| files[i].toString().endsWith("wav")) {
							Log.i(TAG,
									"oooooooooooooooo = "
											+ files[i].getAbsolutePath());
							Mp3Util.addlocalmusic(files[i].toString(), afio, getApplicationContext());
						}
					}
				}
			}
		}
	}

	//查找所有音频文件 然后添加到应用库
	private void scanfile(File f) {
		//查找当前有音频文件的目录		
		new Thread(refreshLocalMusic).start();
	}

	Runnable refreshLocalMusic = new Runnable() {
		@Override
		public void run() {
			AudioFileIO afio = new AudioFileIO();
			List<String> urllist = searchByDirectory();
			int urlsize = urllist.size();//所有文件夹的数量
			for (int j = 0; j < urllist.size(); j++) {
				float size = (float)j/urlsize;
			    DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
			    String filesize = df.format(size);//返回的是String类型的
			    Intent intent = new Intent();
				intent.putExtra("speed", filesize);
				intent.setAction("com.scann.speed");
				sendBroadcast(intent);
				File file = new File(urllist.get(j));
				if (file.isDirectory()) {
					File[] files = file.listFiles();
					if (files != null) {
						for (int i = 0; i < files.length; i++) {
							if (files[i].isDirectory()) {
								scannfile(files[i]);
								Log.i(TAG, "进入文件夹处理");
							} else {
																
								if (files[i].toString().endsWith(".mp3")
										|| files[i].toString().endsWith("ape")
										|| files[i].toString().endsWith("flac")
										|| files[i].toString().endsWith("wav")) {
									Log.i(TAG,
											"oooooooooooooooo = "
													+ files[i]
															.getAbsolutePath());
									Mp3Util.addlocalmusic(files[i].toString(), afio, getApplicationContext());
								}
							}
						}
					}
				}
			}
			Message msg = Message.obtain();
			msg.obj = true;
			msg.what = 1;

			refreshShow.sendMessage(msg);
			
		}
	};

	/**
	 * 查询音乐媒体库所有目录
	 * */
	public List<String> searchByDirectory() {
		List<String> list = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		String[] projection = { MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DATA };
		Cursor cr = this.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Audio.Media.DISPLAY_NAME);
		String displayName = null;
		String data = null;
		while (cr.moveToNext()) {
			displayName = cr.getString(cr
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			data = cr.getString(cr.getColumnIndex(MediaStore.Audio.Media.DATA));
			data = data.replace(displayName, "").toLowerCase();
			if (!sb.toString().contains(data)) {
				;
				list.add(data);
				sb.append(data);
			}
		}
		cr.close();
		return list;
	}
	
	
	protected BroadcastReceiver scannSpeedReciver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();			
			if(action.equals("com.scann.speed")){
				String speed = intent.getStringExtra("speed");
				scan_over_show.setText("当前扫描进度....."+speed);
			}			
		};
	};
	
}
