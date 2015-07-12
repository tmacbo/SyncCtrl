package com.irunsin.activity.syncctrl;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyboperation.engine.MultiPointController;
import com.cyboperation.inter.IController;
import com.irunsin.activity.syncctrl.SlidingUpPanelLayout.PanelSlideListener;
import com.irunsin.controller.fragment.CollectionMusicFragment;
import com.irunsin.controller.fragment.LocalMusicFragment;
import com.irunsin.controller.fragment.MainFragment;
import com.irunsin.controller.fragment.RecentlyListenMusicFragment;
import com.irunsin.controller.fragment.XiamiContentMainFragment;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.sys.BaseActivity;
import com.irunsin.controller.sys.Global;
import com.irunsin.controller.sys.ProcessManager;
import com.irunsin.controller.util.IrunSinApplication;
import com.irunsin.controller.util.Mp3Util;
import com.irunsin.controller.util.imageloader.ImageLoader;
import com.irunsin.syncctrl.R;

public class HomeActivity extends BaseActivity {
	private SlidingUpPanelLayout mLayout;
	private FragmentManager fragmentManager;
	private MainFragment mainfragment;
	public XiamiContentMainFragment xiamicontentFragment;
	private static Boolean isQuit = false;
	private Timer timer;
	private View drayView;
	private View drayClikView;
	private View hideLayout;
	private View visibleLayout;
	private ImageView closeUpPane;
	
	private IrunSinApplication irunsin;
	private IController iControll;
	//迷你播放页面	视图
	private TextView mini_song_name;
	private TextView mini_singer_name;
	private ImageView mini_image;
	private ImageView mini_next;
	private FrameLayout mini_play;
	
	//播放主界面
	private ImageView main_pre_image;
	private ImageView main_next_image;
	private FrameLayout _main_play_image;
	private TextView main_singer_name;
	private TextView main_song_name;
	private ImageView main_rand_image;
	private ImageView main_like_image;
	private ImageView main_image;
	
	//当前播放的音乐BEAN
	private Mp3Info currmp3info;
	
	
	private Bitmap bitmap;// 播放的音乐的图片
	private Bitmap minibitmap;// miniplay显示的图片
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();
		findView();
		addAction();

	}

	public void initData() {
		irunsin = (IrunSinApplication) getApplicationContext();
		iControll = new MultiPointController(getApplicationContext());
		// 初始化计时器
		timer = new Timer();
		/*
		 * 初始化载入主fragment
		 */
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switchContent(0);
		// 注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mp3.currdata");
		filter.addAction("com.mp3.begin");
		filter.addAction("com.mp3.pause");		
		filter.addAction("com.mp3.volum");
		filter.addAction("com.mp3.false");
		filter.addAction("com.mp3.error");
		filter.addAction("com.mp3.alarmwarn");
		filter.addAction("com.mp3.alarmnotify");
		filter.addAction("com.mp3.exception");
		filter.addAction("com.mp3.refresh");
		filter.addAction("com.mp3.frecontrol");
		filter.addAction("com.mp3.avsub");
		filter.addAction("com.device.exception");
		filter.addAction("com.mp3.netexception");
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(musicExpandReceiver, filter);
	}

	public void findView() {
		mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		closeUpPane = (ImageView) findViewById(R.id.music_close_upane);
		drayClikView = findViewById(R.id.click_to_playing);
		drayView = findViewById(R.id.music_dray_view);

		hideLayout = findViewById(R.id.hide);
		visibleLayout = findViewById(R.id.visible);
		
		mini_song_name = (TextView) findViewById(R.id.song_name);
		mini_singer_name = (TextView) findViewById(R.id.singer_name);
		mini_image = (ImageView) findViewById(R.id.mini_image);
		mini_next = (ImageView) findViewById(R.id.iv_next);
		mini_play = (FrameLayout) findViewById(R.id.audio_player_play);
		
		main_like_image = (ImageView) findViewById(R.id.music_like);
		main_next_image = (ImageView) findViewById(R.id.next_image_view);
		main_pre_image = (ImageView) findViewById(R.id.pre_image_view);
		main_rand_image = (ImageView) findViewById(R.id.music_mode);
		main_singer_name = (TextView) findViewById(R.id.music_player_singer);
		main_song_name = (TextView) findViewById(R.id.music_player_song_name);
		main_image = (ImageView) findViewById(R.id.imgview_cd);
		
		//发起设备订阅		
		ProcessManager.getInstance().addProcess(avserviceSub);
	}

	public void addAction() {

		mLayout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
			}

			@Override
			public void onPanelExpanded(View panel) {
				Global.debug("onPanelExpanded");
				mLayout.setDragView(drayView);
				hideLayout.setVisibility(View.GONE);
				visibleLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPanelCollapsed(View panel) {
				// mDrawerLayout.setEnabled(true);
				Global.debug("onPanelCollapsed");
				mLayout.setDragView(drayClikView);
				hideLayout.setVisibility(View.VISIBLE);
				visibleLayout.setVisibility(View.GONE);
			}

			@Override
			public void onPanelAnchored(View panel) {
				Global.debug("onPanelAnchored");
				hideLayout.setVisibility(View.VISIBLE);
				visibleLayout.setVisibility(View.GONE);

			}
		});

		closeUpPane.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mLayout.collapsePane();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void call(int callID, Object[] objs) {

	}

	// 监听手机上的BACK键
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				Fragment currentFragment = fragmentManager
						.findFragmentByTag(Global.CHILD_FRAGMENT_TAG);
				
				if (currentFragment != null && !Global.backToMain) {					
					if (currentFragment != null) {
						if(currentFragment instanceof LocalMusicFragment
								|| currentFragment instanceof RecentlyListenMusicFragment
								|| currentFragment instanceof CollectionMusicFragment){
							switchContent(currentFragment,mainfragment);
						}else if(currentFragment instanceof XiamiContentMainFragment){
							switchContent(0);
						}
					}
					return false;					
				} else {
					if (isQuit == false) {
						isQuit = true;
						Toast.makeText(getBaseContext(), "再按一次返回键退出主界面",
								Toast.LENGTH_SHORT).show();
						TimerTask task = null;
						task = new TimerTask() {
							@Override
							public void run() {
								isQuit = false;
							}
						};
						timer.schedule(task, 2000);
					} else {
						// finish();
						Intent home = new Intent(Intent.ACTION_MAIN);
						home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						home.addCategory(Intent.CATEGORY_HOME);
						startActivity(home);
					}
					return false;
				}

			}
			
			return super.onKeyDown(keyCode, event);
		}
		// 在原有的队列中删除传递进来的当前要删除的fragment 显示 要显示的fragment
		public void switchContent(Fragment oldfragment, Fragment fragment) {
			
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.remove(oldfragment);
			hideFragments(transaction);
			transaction.show(fragment);
			transaction.commit();
			Global.backToMain = false;
		}
		// 隐藏所有 实例化的fragment 显示传递进来的实例化的fragment
		public void switchFragment(Fragment fragment) {
			
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			hideFragments(transaction);
			transaction
					.add(R.id.main_fragment, fragment, Global.CHILD_FRAGMENT_TAG);			
			transaction.commit();
			Global.backToMain = false; 
		}
		// 根据传递进来的标志 判断显示主fragment 还是虾米网络音乐fragment
		public void switchContent(int index) {
			FragmentTransaction transaction = fragmentManager.beginTransaction();			
			hideFragments(transaction);
			switch (index) {
			case 0:
				if (mainfragment == null) {
					mainfragment = new MainFragment();
					transaction.add(R.id.main_fragment, mainfragment,Global.CHILD_FRAGMENT_TAG);
				} else {
					transaction.show(mainfragment);
				}				
				Global.backToMain = true; //当到主fragment界面时   无法继续返回
				break;
			case 1:
				if (xiamicontentFragment == null) {
					xiamicontentFragment = new XiamiContentMainFragment();
					transaction.add(R.id.main_fragment, xiamicontentFragment,Global.CHILD_FRAGMENT_TAG);
				} else {
					transaction.show(xiamicontentFragment);
				}
				Global.backToMain = false; 
				break;
			}

			transaction.commit();			
		}
		
		private void hideFragments(FragmentTransaction transaction) {
			if (mainfragment != null) {
				transaction.hide(mainfragment);
			}
			if(xiamicontentFragment !=null){
				transaction.hide(xiamicontentFragment);
			}
		}
		/*
		 * 已获取设备 向设备发起订阅
		 */
		Runnable avserviceSub = new Runnable() {

			@Override
			public void run() {
				Global.debug("准备发起订阅");
//				if (buffprogressDialog != null) {
//					buffprogressDialog.cancel();
//				}
				if(irunsin.getDevice()!=null){
					//发起订阅
					iControll.eventSub();
				}
			}
		};
		
		
		protected BroadcastReceiver musicExpandReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// 收到播放的广播的时候 则开启 进度条线程
				if(action.equals("com.mp3.currdata")){
					Mp3Info mp3info = (Mp3Info) intent.getExtras().getSerializable("mp3info");
					String mp3Name = mp3info.getMp3Name();							
					currmp3info = mp3info;//赋值当前收到的音响端正在播放的音乐详情				
					String artist = mp3info.getArtist();
					mp3Name = Mp3Util.toGb2312(mp3Name);
					artist = Mp3Util.toGb2312(artist);
					mini_song_name.setText(mp3Name);
					mini_singer_name.setText(artist);
					main_singer_name.setText(artist);
					main_song_name.setText(mp3Name);
					// 显示图片
					turnpic();				
				}
//				else if (action.endsWith("com.mp3.begin")) {				
//					showPlay(true);				
//					refreshSeekBar();	
//					setFavoriteClick();
//				} else if (action.equals("com.mp3.volum")) {
//					int volum = intent.getExtras().getInt("volum");
//					sb_voice.setProgress(volum);
//					now_voice = volum;
//				}
//				// 当收到暂停的广播的时候 则取消线程
//				else if (action.endsWith("com.mp3.pause")) {
//					removeHandler();
//					// removePicHandler();
//					showPlay(false);
//				}			
//				// 播放失败的广播
//				else if (action.equals("com.mp3.false")) {
//					Toast.makeText(getBaseContext(), "播放失败,请重新播放！",
//							Toast.LENGTH_SHORT).show();
//				}
				
			}
		};
		
		/**
		 * 图片显示   当为网络音乐时候全部显示图片   当为本地音乐时  只有本机播放的才能显示图片
		 */
		public void turnpic() {
			//显示网络图片
			if(currmp3info!=null && currmp3info.getLoOnFlag() == 1){
				String imageUrl = currmp3info.getLogo();
				ImageLoader.getInstances().displayImage(imageUrl, mini_image);
				ImageLoader.getInstances().displayImage(imageUrl, main_image);
//				animation.setInterpolator(lin);
//				musicPic.startAnimation(animation);
			}
			//显示本地图片
			else if(currmp3info!=null && currmp3info.getLoOnFlag() ==0 && irunsin.getPLAY_STATE()!=4){
				Bitmap bitmapOrg = Mp3Util.getArtwork(getApplicationContext(),
						currmp3info.getFilepath());
				if (bitmapOrg == null) {
					bitmapOrg = Mp3Util.getDefaultArtwork(getApplicationContext());
					minibitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.ic_launcher);
				}				
				bitmap = bitmapOrg;
				minibitmap = bitmapOrg;
				Message msg = Message.obtain();
				msg.obj = true;
				msg.what = 1;
				refreshMp3picHandler.sendMessage(msg);
			}
			//显示默认图片
			else{
				Bitmap bitmapOrg = Mp3Util
						.getDefaultArtwork(getApplicationContext());
				bitmap = bitmapOrg;
				minibitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_launcher);
				Message msg = Message.obtain();
				msg.obj = true;
				msg.what = 1;
				refreshMp3picHandler.sendMessage(msg);
			}
					
		}
		// 修改图片
		private Handler refreshMp3picHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					mini_image.setImageBitmap(minibitmap);				
					// 获取这个图片的宽和高
					int targetWidth = 480;
					int targetHeight = 480;
					Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
							targetHeight, Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(targetBitmap);
					Path path = new Path();
					path.addCircle(
							((float) targetWidth - 1) / 2,
							((float) targetHeight - 1) / 2,
							(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
							Path.Direction.CCW);
					canvas.clipPath(path);

					int a = bitmap.getWidth();
					int b = bitmap.getHeight();
					Global.debug("宽度 = " + a + "   长度 = " + b);
					canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(),
							bitmap.getHeight()), new Rect(0, 0, targetWidth,
							targetHeight), null);
					// 创建新的图片
					Bitmap bitmapPic = Bitmap.createBitmap(targetBitmap, 0, 0, 480,
							480);
					// 
					break;
				
				default:
					break;
				}
			}
		};
}
