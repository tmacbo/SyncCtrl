package com.irunsin.controller.activity;

import java.util.List;

import org.cybergarage.upnp.Device;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.cyboperation.engine.DLNAContainer;
import com.cyboperation.engine.DlnaPackageImp;
import com.cyboperation.service.DLNAService;
import com.irunsin.controller.sys.BaseActivity;
import com.irunsin.controller.sys.Global;
import com.irunsin.controller.util.IrunSinApplication;
import com.irunsin.controller.util.WifiUtil;
import com.irunsin.syncctrl.R;

public class LoadingActivity extends BaseActivity {

	private int search = 0;
	private ImageView imgProgress;
	// private WifiManager mWifiManager;
	private IrunSinApplication irunsin;


	@Override
	public void call(int callID, Object[] objs) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		initData();
		findViews();
		addAction();
	}

	protected void initData() {
		irunsin = (IrunSinApplication) getApplicationContext();
		// 初始化
		boolean wififlag = WifiUtil.isWifi(getApplicationContext());
		if (!wififlag) {
			new AlertDialog.Builder(LoadingActivity.this)
					.setIcon(R.drawable.toast_three_tangle_img)
					.setTitle("提示")
					.setMessage("请先行连接Wifi网络!")
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {								
									Global.exit();
								}
							}).show();
		}
	}

	protected void findViews() {
//		imgProgress = (ImageView) findViewById(R.id.loading_progress);
	}

	/**
	 * 
	 */
	protected void addAction() {

//		Animation animation = AnimationUtils.loadAnimation(
//				LoadingActivity.this, R.anim.rotate);
//		LinearInterpolator lin = new LinearInterpolator();
//		animation.setInterpolator(lin);
//		imgProgress.startAnimation(animation);

		// 开启DLNA设备搜寻服务
		Intent service = new Intent(LoadingActivity.this,DLNAService.class);
		startService(service);

		// 查询库中存放的播放模式PLAY_MODE 无值默认为1
		SharedPreferences myPrefs = getSharedPreferences("isplaymode", MODE_PRIVATE);
		irunsin.setPLAY_MODE(myPrefs.getInt("PLAY_MODE", 1));
		// 存入数据
		Editor editor = myPrefs.edit();
		editor.putInt("PLAY_MODE", irunsin.getPLAY_MODE());
		editor.commit();

		// 开启线程判断当前是否有设备且缓存
		handler.post(updateThread);
	}

	private Handler handler = new Handler();
	Runnable updateThread = new Runnable() {
		public void run() {
			// 调用设备发现方法
			List<Device> list = DLNAContainer.getInstance().getDevices();
			if (list.size() > 0 || search > 6) {
				handler.removeCallbacks(updateThread);

				cancelProgressDialog();
				Intent intent = new Intent();
				intent.setClass(LoadingActivity.this, DeviceSetActivity.class);
				if (("").equals(irunsin.getDeviceName())) {
					intent.putExtra("entrance", 0);
				} else {
					intent.putExtra("entrance", 1);
				}

				startActivity(intent);
				LoadingActivity.this.finish();
			} else {
				search++;
				// 每次延迟1000毫秒再启动线程
				handler.postDelayed(updateThread, 1000);
			}
		}
	};



	protected void onDestroy() {
		 super.onDestroy();
	};

}
