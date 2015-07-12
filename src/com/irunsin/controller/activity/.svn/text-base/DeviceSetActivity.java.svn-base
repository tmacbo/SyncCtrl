package com.irunsin.controller.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.cybergarage.upnp.Device;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyboperation.engine.DLNAContainer;
import com.cyboperation.engine.MultiPointController;
import com.cyboperation.inter.IController;
import com.cyboperation.service.DLNAService;
import com.irunsin.activity.syncctrl.HomeActivity;
import com.irunsin.controller.db.DbHelper;
import com.irunsin.controller.model.RememberDevices;
import com.irunsin.controller.sys.BaseActivity;
import com.irunsin.controller.sys.Global;
import com.irunsin.controller.sys.ProcessManager;
import com.irunsin.controller.util.IrunSinApplication;
import com.irunsin.syncctrl.R;

public class DeviceSetActivity extends BaseActivity {

	private String TAG = "DeviceSetActivity";
	private ListView listView;
	private List<Device> mDevices;

	private int checkpostion = -1;
	private TextView combutton;
	private TextView refeshButton;
	private ImageButton backButton;
	private int search = 0;

	private List<Device> devicechoice;
	private DeviceAdapter mDeviceAdapter;

	private CheckBox rememDevice;
	private boolean rememFlag = false; // 记住设备的标志־
	private boolean addeleteFlag = false; // 添加删除 标志־
	private List<RememberDevices> list;

	private int entrance;// 此值为 标志为入口 0代表用户由登陆进来 其他代表 用户房间选择

	private ImageButton deviceConfig;

	private Thread subInfo;
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

	private IrunSinApplication irunsin;

	private boolean openFlag = false;// 代表是否有开启服务
	private int shutnumber = 0;// 代表关闭服务的次数
	
	private SharedPreferences myPrefs;

	private static Boolean isQuit = false;
	Timer timer = new Timer();
	
	private Device oldDevice;
	
	@Override
	public void call(int callID, Object[] objs) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_set);
//		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
//				FLAG_HOMEKEY_DISPATCHED);// 关键代码
		initData();
		findViews();
		addAction();
	}

	protected void initData() {
		// 获取sharepreferences的对象
		myPrefs = getSharedPreferences("ssdpstring", MODE_PRIVATE);
		
		mDevices = DLNAContainer.getInstance().getDevices();
		irunsin = (IrunSinApplication) getApplicationContext();
		mDeviceAdapter = new DeviceAdapter(DeviceSetActivity.this);
		// 获取储存的设备信息
		list = DbHelper.getInstence(getApplicationContext()).getRemDevice();
		// 判断查询出来的库中的值 赋值是否已经记录 记住的设备至库中
		if (list.size() > 0) {
			addeleteFlag = true;
		}
		// 如果储存的设备中存在记住的设备 且当前列表中有该设备 则直接跳转 默认选择该设备 则直接跳转
		// 判断是登陆进来还是 由用户直接点击房间设置进来 9代表用户点击的房间设置
		entrance = getIntent().getExtras().getInt("entrance", 9);
		if (entrance == 0) {
			if (addeleteFlag) {
				for (int i = 0; i < mDevices.size(); i++) {
					if (mDevices.get(i).getFriendlyName()
							.equals(list.get(0).getUuid())) {
						irunsin.setDevice(mDevices.get(i));
						// 直接跳转
						
						Intent intent = new Intent();
						intent.putExtra("subflag", 0);// 标志从哪里进入
						// 0代表为从选择设备界面进入
						// 1代表从闹钟设置进入 2
						// 代表从闹钟接收进入
						// 此字段用于控制是否订阅 以及
						// 是否判断当前设备是否为空
						// 0 代表既要做订阅 又要做判断设备是否为空 1代表需要做订阅需要判断设备是否为空
						// 2代表不需要判断设备知否为空 不需要做订阅
						
						//存入选择的设备的ssdp
						Editor editor = myPrefs.edit();
						editor.putString("ssdp", mDevices.get(i).getSSDPPacket().toString());
						editor.commit();
						intent.setClass(DeviceSetActivity.this,
								HomeActivity.class);
						startActivity(intent);
						DeviceSetActivity.this.finish();
					}
				}
			}
		} else if (entrance == 8) {
			// 代表强制刷新
			// 开启DLNA设备搜寻服务
			DLNAContainer.getInstance().clear();		
			mDevices = new ArrayList<Device>();
			refresh();
			showProgressDialog("正在搜寻设备,请稍后");
			search = 0;			
			// 开启线程判断当前是否有设备且缓存
			handler.post(refresh);
		}else if(entrance == 1){
			String deviceName = irunsin.getDeviceName();
			for (int i = 0; i < mDevices.size(); i++) {
				if (mDevices.get(i).getFriendlyName()
						.equals(deviceName)) {
					// 直接跳转	
					irunsin.setDevice(mDevices.get(i));
					
					Intent intent = new Intent();
					intent.putExtra("subflag", 2);// 标志从哪里进入
					// 0代表为从选择设备界面进入
					// 1代表从闹钟设置进入
					// 2代表从闹钟接收进入
					// 此字段用于控制是否订阅 以及
					// 是否判断当前设备是否为空
					//记录当前选择设备的ssdp数据包
					Editor editor = myPrefs.edit();
					editor.putString("ssdp", mDevices.get(i).getSSDPPacket().toString());
					editor.commit();
					
					intent.setClass(DeviceSetActivity.this,
							HomeActivity.class);
					startActivity(intent);
					DeviceSetActivity.this.finish();
				}
			}			
		}
		// 初始化储存设备的集合
		if (devicechoice == null) {
			devicechoice = new ArrayList<Device>();
		}

	}

	protected void findViews() {
		listView = (ListView) findViewById(R.id.device_list);
		listView.setAdapter(mDeviceAdapter);
		combutton = (TextView) findViewById(R.id.comfire_textview);
		refeshButton = (TextView) findViewById(R.id.refreshdevice);
		backButton = (ImageButton) findViewById(R.id.device_set_back);
		rememDevice = (CheckBox) findViewById(R.id.rem_device_checkbox);
		deviceConfig = (ImageButton) findViewById(R.id.device_config_first);
	}

	protected void addAction() {
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DeviceSetActivity.this.finish();
			}
		});

		combutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (devicechoice.size() == 0) {
					showToastText("请选择配置的设备，谢谢！！");
				} else {
					if(entrance ==2){
						oldDevice = irunsin.getDevice();
					}
					// 选择的设备
					Device device = devicechoice.get(0);
					irunsin.setDevice(device);
					String uuid = device.getFriendlyName();
					// // 如果用户选择记住设备 则 添加到对应库中
					if (rememFlag) {
						boolean rem;

						rem = DbHelper.getInstence(getApplicationContext())
								.insertRemedevice("", uuid, addeleteFlag);
						if (!rem) {
							showToastText("记住该设备失败!");
						}

					}
					
					//在跳转以前先遍历是否存在mainactivity 如果栈中有  则删除
					List<Activity> list  = getActivityList();
					for (int i = 0; i < list.size(); i++) {
						if(list.get(i) instanceof HomeActivity){							
							if(entrance ==2){								
								DeviceSetActivity.this.finish();
								//记录当前选择设备的ssdp数据包
								Editor editor = myPrefs.edit();
								editor.putString("ssdp", device.getSSDPPacket().toString());
								editor.commit();
								//重新发起订阅
								ProcessManager.getInstance().addProcess(avserviceSub);
								return;
							}else{
								list.get(i).finish();	
							}
						}
					}
					
					Intent intent = new Intent();
					intent.putExtra("subflag", 0);// 标志从哪里进入 0代表为从选择设备界面进入
													// 1代表从闹钟设置进入 2 代表从闹钟接收进入
													// 此字段用于控制是否订阅 以及
													// 是否判断当前设备是否为空
					//记录当前选择设备的ssdp数据包
					Editor editor = myPrefs.edit();
					editor.putString("ssdp", device.getSSDPPacket().toString());
					editor.commit();
					
					intent.setClass(DeviceSetActivity.this,
							HomeActivity.class);
					startActivity(intent);	
					DeviceSetActivity.this.finish();
					
				}
			}
		});

		refeshButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openFlag = true;
				String dialogString = "正在搜寻设备,请稍后!";
				//刷新搜索
				Intent intent = new Intent();
				intent.setClass(DeviceSetActivity.this, DLNAService.class);
				startService(intent);
				search = 0;
				showProgressDialog(dialogString);
				// 开启线程判断当前是否有设备且缓存
				handler.post(refresh);				
			}
		});

		rememDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (cb.isChecked()) {
					if (devicechoice.size() == 0) {
						cb.setChecked(false);
						showToastText("请选择配置的设备，谢谢！！");
					} else {
						rememFlag = true;
						showToastText("记住此设备后下次登录将直接使用此设备!");
					}
				} else {
					rememFlag = false;
				}
			}
		});

		deviceConfig.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(DeviceSetActivity.this,
//						DeviceSetPromptActivity.class);
//				startActivity(intent);
			}
		});
	}
	
	// 监听手机上的BACK键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(entrance==2){
				DeviceSetActivity.this.finish();
			}else{
				if (isQuit == false) {
					isQuit = true;
					Toast.makeText(getBaseContext(), "再按一次退出应用",
							Toast.LENGTH_SHORT).show();
					TimerTask task = null;
					task = new TimerTask() {
						@Override
						public void run() {
							isQuit = false;
						}
					};
					timer.schedule(task, 2000);
					return false;
				} else {
					Intent intent = new Intent(getApplicationContext(),DLNAService.class);
					 stopService(intent);
//					 Intent intent1 = new Intent(getApplicationContext(),MusicPlayService.class);
//					 stopService(intent1);	
					Global.exit();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private Handler handler = new Handler();
	Runnable refresh = new Runnable() {
		@Override
		public void run() {
			if (search > 5) {
				handler.removeCallbacks(refresh);
				// 调用设备发现方法
				List<Device> list = DLNAContainer.getInstance().getDevices();
				mDevices = list;
				refresh();
				if (list.size() > 0) {
					cancelProgressDialog();
				} else {
					cancelProgressDialog();
					new AlertDialog.Builder(DeviceSetActivity.this)
							.setIcon(R.drawable.toast_three_tangle_img)
							.setTitle("提示")
							.setMessage("当前未发现润听设备,请检查你的音响设备是否开启且配置！")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									})
							.setNeutralButton("添加新设备",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
//											Intent intent = new Intent();
//											intent.setClass(
//													DeviceSetActivity.this,
//													DeviceSetPromptActivity.class);
//											startActivity(intent);
										}
									}).show();
				}
			} else {
				search++;
				// 每次延迟1000毫秒再启动线程
				handler.postDelayed(refresh, 1000);
			}
		}
	};




	private class DeviceAdapter extends BaseAdapter {

		private DeviceSetActivity device;

		public DeviceAdapter(DeviceSetActivity deviceSet) {
			this.device = deviceSet;
		}

		@Override
		public int getCount() {
			return mDevices.size();
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.activity_device_lst, null);
				holder = new ViewHolder();
				holder.tv_name_item = (TextView) convertView
						.findViewById(R.id.tv_name_item);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.device_choice);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_name_item.setText(mDevices.get(position).getFriendlyName());
			holder.checkBox.setId(position);
			holder.checkBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					if (cb.isChecked()) {
						addChoiceDevice(mDevices.get(cb.getId()));
						if (checkpostion != -1) {
							CheckBox tempCheckBox = (CheckBox) device
									.findViewById(checkpostion);
							if (tempCheckBox != null) {
								tempCheckBox.setChecked(false);
							}
							deleteChoiceDevice(mDevices.get(tempCheckBox
									.getId()));
						}
						checkpostion = cb.getId();// 保存当前选中的checkbox的id值ֵ
					} else {
						deleteChoiceDevice(mDevices.get(cb.getId()));
						checkpostion = -1;
					}
				}
			});
			return convertView;
		}
	}

	// 添加选择的 device
	public void addChoiceDevice(Device device) {
		devicechoice.add(device);
	}

	// 删除选择的 device
	public void deleteChoiceDevice(Device device) {
		for (int i = 0; i < devicechoice.size(); i++) {
			if (devicechoice.get(i) == device) {
				devicechoice.remove(i);
			}
		}
	}

	static class ViewHolder {
		TextView tv_name_item;
		CheckBox checkBox;
	}

	private void refresh() {
		if (mDeviceAdapter != null) {
			mDeviceAdapter.notifyDataSetChanged();
		}
	}


	protected void onDestroy() {

		super.onDestroy();
	};
	
	
	/*
	 * 已获取设备 向设备发起订阅
	 */
	Runnable avserviceSub = new Runnable() {

		@Override
		public void run() {
			Global.debug("准备发起订阅");
			irunsin.setDevice(irunsin.getDevice());// 赋值设备为选择设备
			IController control = new MultiPointController(getApplicationContext());			
			//发起订阅
			control.stop(oldDevice);
			control.cancelSub();
			control.eventSub();
		}
	};
}
