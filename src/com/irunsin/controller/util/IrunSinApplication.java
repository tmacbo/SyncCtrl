package com.irunsin.controller.util;

import java.util.List;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;

import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.util.imageloader.CacheConfig;
import com.irunsin.controller.util.imageloader.ImageLoader;
import com.irunsin.syncctrl.R;
import com.taobao.top.android.TopAndroidClient;

public class IrunSinApplication extends Application {
	// 存放当前播放状态
	private int PLAY_STATE; // 0 代表正在播放 1 代表暂停 2表示播放完毕 3代表播放失败 完全标志当前设备的状态信息
							// 通过设备的订阅返回来修改
	// 存放播放模式
	private int PLAY_MODE; // 0 代表单曲播放 1 代表循环播放 2 代表随机播放
	
	// 储存上次播放时间
	private long playtime = 0;
	private List<Mp3Info> listmp3; // 代表当前正在播放的音乐列表
	
	// 当期播放位置 初始播放位置为-1
	private int position = -1;
	
	// 订阅ID
	public static String AVT_SUB_SID = "";
	public static String REC_SUB_SID = "";
	
	//正在播放的音乐的MP3id用以判断来收到stop之后的切换
	private String play_mp3Id;
	//当前播放的歌曲
	private Mp3Info mp3info;

	// 设备占用状态
	private boolean SOFT_STATE = false; // ture 代表当前手机占用 false 代表其他手机占用 默认为false

	//设备信息
	private Device device;
	//upnp控制器
	private ControlPoint controlpoint;
	//缓存的设备对象名称
	private String deviceName;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		TopAndroidClient.registerAndroidClient(getApplicationContext(),
				"23014544", "f216a893c66b622d00320527087de8ee",
				"http://www.irunsin.com/");
			    
        ImageLoader.init(getApplicationContext(),
                new CacheConfig()
                    .setDefRequiredSize(600) // 设置默认的加载图片尺寸（表示宽高任一不超过该值，默认是70px）
                    .setDefaultResId(R.drawable.ic_launcher) // 设置显示的默认图片（默认是0，即空白图片）
                    .setBitmapConfig(Bitmap.Config.ARGB_8888) // 设置图片位图模式（默认是Bitmap.CacheConfig.ARGB_8888）
                    .setMemoryCachelimit(Runtime.getRuntime().maxMemory() / 3) // 设置图片内存缓存大小（默认是Runtime.getRuntime().maxMemory() / 4）
	                    .setFileCachePath(Environment.getExternalStorageDirectory().toString() + "/irunsin/cache") // 设置文件缓存保存目录
                    );
	}
	public int getPLAY_STATE() {
		return PLAY_STATE;
	}
	public void setPLAY_STATE(int pLAY_STATE) {
		PLAY_STATE = pLAY_STATE;
	}
	public int getPLAY_MODE() {
		return PLAY_MODE;
	}
	public void setPLAY_MODE(int pLAY_MODE) {
		PLAY_MODE = pLAY_MODE;
	}
	public long getPlaytime() {
		return playtime;
	}
	public void setPlaytime(long playtime) {
		this.playtime = playtime;
	}
	public List<Mp3Info> getListmp3() {
		return listmp3;
	}
	public void setListmp3(List<Mp3Info> listmp3) {
		this.listmp3 = listmp3;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getPlay_mp3Id() {
		return play_mp3Id;
	}
	public void setPlay_mp3Id(String play_mp3Id) {
		this.play_mp3Id = play_mp3Id;
	}
	public Mp3Info getMp3info() {
		return mp3info;
	}
	public void setMp3info(Mp3Info mp3info) {
		this.mp3info = mp3info;
	}
	public boolean isSOFT_STATE() {
		return SOFT_STATE;
	}
	public void setSOFT_STATE(boolean sOFT_STATE) {
		SOFT_STATE = sOFT_STATE;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public ControlPoint getControlpoint() {
		return controlpoint;
	}
	public void setControlpoint(ControlPoint controlpoint) {
		this.controlpoint = controlpoint;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	
	
}
