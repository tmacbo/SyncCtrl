package com.irunsin.controller.sys;

import java.io.File;
import java.util.LinkedList;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.text.ClipboardManager;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity的基本类，建议程序中的Activity实现该类 使用该类时注意：
 * 
 * 程序中使用的整形常量一律采该类的FIRST_VALUE++的方式
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements
		ProcessListener {
	/** Notification管理 */
	public NotificationManager mNotificationManager;
	public static int FIRST_VALUE = 77;

	public static LinkedList<Activity> activityList;

	// 以下是用于发送Message到handler时使用的what值常量，子类中定义的应该写到此处，防止重复
	public static final int CALL_FUNCTION = FIRST_VALUE++;
	public static final int CALL_FUNCTION_CANCEL = FIRST_VALUE++;
	public static final int CALL_ID_CANCEL = FIRST_VALUE++;
	public static final int CALL_ID_ONDONE = FIRST_VALUE++;
	public static final int CALL_RELOAD_CANCEL = FIRST_VALUE++; // 取消重新加载
	public static final int CALL_RELOAD_OK = FIRST_VALUE++; // 选择重新加载
	public static final int CANCEL_PROGRESS_DIALOG = FIRST_VALUE++;
	public static final int CANCEL_PROGRESS_DIALOG2 = FIRST_VALUE++;
	public static final int FINISH_ACTIVITY = FIRST_VALUE++;
	public static final int FLASH = FIRST_VALUE++;
	public static final int LAST_TIME_CHANGED = FIRST_VALUE++;
	public static final int REQ_REG = FIRST_VALUE++;
	public static final int REQ_LOGIN = FIRST_VALUE++;
	protected static boolean globalArgsLoaded = false;
	public static final int GET_MSG_CODE_SUCCESS = FIRST_VALUE++;
	public static final int CLICK_ITEM = FIRST_VALUE++;
	public static final int LOAD_ORDER_LISTS_SUCCESS = FIRST_VALUE++;
	public static final int LOAD_LISTS_SUCCESS = FIRST_VALUE++;
	public static final int LOAD_LISTS_FAILD = FIRST_VALUE++;
	public static final int GET_REG_MESSAGE = FIRST_VALUE++;

	public static boolean isActivityInFront = false;
	public static final int NO_REGINFO = FIRST_VALUE++;
	public static final int MENU_ID_EXIT = FIRST_VALUE++;

	public static final int REQUEST_SEL_PIC = FIRST_VALUE++;
	public static final int GPS_INFO_REFRESH = FIRST_VALUE++;

	public static final int REQUEST_USE_CAMERA = FIRST_VALUE++;

	public static final String RUN_FLAG = "runInGroupFlag";

	public static final String SAVE_SETTING = "SAVE_SETTING";

	public static final String SAVE_USERNAME_AND_PWD = "SAVE_USERNAME_AND_PWD";

	protected static boolean settingLoaded = false;

	public static boolean canUserCache = true;
	public static final int SHOW_BLOG_TOAST = FIRST_VALUE++;
	public static final int SHOW_ERROR_DIALOG = FIRST_VALUE++;
	public static final int SHOW_MESSAGE_DIALOG = FIRST_VALUE++;
	public static final int SHOW_PROGRESS_DIALOG = FIRST_VALUE++;
	public static final int SHOW_PROGRESS_DIALOG_CAN_CANCEL = FIRST_VALUE++;
	public static final int SHOW_TOAST_TEXT = FIRST_VALUE++;
	public static final int STS_LAODING_DEGIN = FIRST_VALUE++;
	public static final int STS_LAODING_FINISH = FIRST_VALUE++;

	public static final int UPDATE_IMG = FIRST_VALUE++;
	public static final int UPDATE_USER_IMG = FIRST_VALUE++;
	public static final int UPDATE_WEIBO_IMG = FIRST_VALUE++;
	public static final int UPDATE_HOSPITAL_LIST = FIRST_VALUE++;
	public static final int UPDATE_DOCTOR_LIST = FIRST_VALUE++;

	public static final int BACK_TO_HOME = FIRST_VALUE++;
	public static final int PAY_SUCCESS = FIRST_VALUE++;
	public static final int SEND_TO_REG = FIRST_VALUE++;
	public static final int LOGIN_CANCEL = FIRST_VALUE++;

	private static void clearInfo(Context context) {
		if (activityList != null) {
			for (int i = 0; i < activityList.size(); i++) {
				Activity activity = activityList.get(i);
				activity.finish();
			}
		}
	}

	public String getSignInfo(String packName) {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					packName, PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			return Md5.MD5(sign.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void call(String phone_no) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse("tel:" + phone_no));
		startActivity(intent);
	}

	public static void exit(Context context) {
		clearInfo(context);
		try {
			Thread.sleep(300);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Process.killProcess(Process.myPid());
	}

	public static LinkedList<Activity> getActivityList() {
		if (activityList == null) {
			activityList = new LinkedList<Activity>();
		}
		return activityList;
	}

	public static void reLogin(Context context, boolean timeout) {

	}

	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String message = "";

			if (msg.what == SHOW_PROGRESS_DIALOG) {
				Activity activity;
				if (!runInGroupFlag) {
					activity = BaseActivity.this;
				} else {
					activity = getParent();
				}

				message = (msg.obj != null) ? (String) msg.obj : "请稍后...";
				progressDialog = ProgressDialog.show(activity, null, message);

			} else if (msg.what == SHOW_PROGRESS_DIALOG_CAN_CANCEL) {
				Activity activity;
				if (!runInGroupFlag) {
					activity = BaseActivity.this;
				} else {
					activity = getParent();
				}
				message = (msg.obj != null) ? (String) msg.obj : "请稍后...";
				final int arg = msg.arg1;

				progressDialog = ProgressDialog.show(activity, null, message,
						true, true, new OnCancelListener() {
							public void onCancel(DialogInterface dialog) {
								call(arg, null);
							}
						});
			} else if (msg.what == CANCEL_PROGRESS_DIALOG) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			} else if (msg.what == SHOW_ERROR_DIALOG) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				message = (String) msg.obj;
				new AlertDialog.Builder(BaseActivity.this).setTitle("错误")
						.setMessage(message).setNegativeButton("确认", null)
						.create().show();
			} else if (msg.what == SHOW_TOAST_TEXT) {
				message = (String) msg.obj;
				Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == SHOW_MESSAGE_DIALOG) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				message = (String) msg.obj;
				new AlertDialog.Builder(BaseActivity.this).setTitle("提示")
						.setMessage(message).setNegativeButton("确认", null)
						.create().show();
			} else if (msg.what == CALL_FUNCTION) {
				if (msg.obj == null) {
					call(msg.arg1, null);
				} else {
					call(msg.arg1, (Object[]) msg.obj);
				}
			} else if (msg.what == FINISH_ACTIVITY) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				BaseActivity.this.finish();
			} else {
				processMessage(msg);
			}
		}
	};

	protected InputMethodManager inputMethodManager;

	// activity中一般只会出现一个progreDialog
	protected ProgressDialog progressDialog = null;

	protected boolean runInGroupFlag = false;

	public TextMsgProcess addProcess(Context context, RequestBean requestBean,
			ProcessListener processListener) {
		return ProcessManager.getInstance().addProcess(context, requestBean,
				processListener);
	}

	public TextMsgProcess addProcess(RequestBean requestBean) {
		return ProcessManager.getInstance().addProcess(this, requestBean, this);
	}

	public abstract void call(int callID, Object[] objs);

	public void cancelProcess(TextMsgProcess process) {
		if (process != null) {
			process.cancel();
		}
	}

	public void cancelProgressDialog() {
		sendMessageToHanler(CANCEL_PROGRESS_DIALOG);
	}

	public void cancelProgressDialog2() {
		sendMessageToHanler(CANCEL_PROGRESS_DIALOG2);
	}

	public void copyText(String str) {
		if (str == null) {
			return;
		}
		ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(str);
	}

	public Handler getHandler() {
		return handler;
	}

	/**
	 * 安全的获取EditText中内容的方法
	 * 
	 * @param editText
	 * @return
	 */
	protected String getTextFromEditText(EditText editText) {
		if (editText == null) {
			return "";
		}
		Editable editable = editText.getText();
		if (editable == null) {
			return "";
		} else {
			return editable.toString();
		}
	}

	public int getWidthPixels() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// BaseActivity.this.getActionBar().hide();
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getActivityList().add(this);
		runInGroupFlag = getIntent().getBooleanExtra(RUN_FLAG, false);
		initGlobalArgs(this);

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// initData();
		// findViews();
		// addAction();
	}

	/**
	 * 清除当前创建的通知栏
	 */
	public void clearNotify(int notifyId) {
		mNotificationManager.cancel(notifyId);// 删除一个特定的通知ID对应的通知
		// mNotification.cancel(getResources().getString(R.string.app_name));
	}

	/**
	 * 清除所有通知栏
	 * */
	public void clearAllNotify() {
		mNotificationManager.cancelAll();// 删除你发的所有通知
	}

	public static void initGlobalArgs(Context context) {
		try {

			canUserCache = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
			Global.debug("内存卡状态=" + Environment.getExternalStorageState()
					+ "canUserCache=" + canUserCache);
			if (!canUserCache) {
				Toast.makeText(context, "您的内存卡可能无法使用，将无法使用图片缓存",
						Toast.LENGTH_SHORT).show();
				return;
			}
			File imageCacheFile = new File(Global.IMG_CACHE_NAME);
			File takePicCacheFile = new File(Global.TAKE_PIC_CACHE_NAME);

			if (!imageCacheFile.exists()) {
				canUserCache = imageCacheFile.mkdirs();
			}
			if (!takePicCacheFile.exists()) {
				canUserCache = takePicCacheFile.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Global.error("图片缓存不能使用", e);
		}

	}

	@Override
	public boolean onDone(ResponseBean responseBean) {
		if (responseBean.getRespCode().equals(ResponseBean.RESP_CODE_NET_ERROR)) {
			showMessageDialog("您的网络可能存在异常，请检查手机的网络功能是否打开");
			return true;
		}
		if (responseBean.getRespCode().equals(
				ResponseBean.RESP_CODE_PARSER_ERROR)) {
			showMessageDialog("解析异常");
			return true;
		}

		return false;
	}

	// /**
	// * 初始化数据操作，由于现在接口做了统一线程池管理，所以可以进行一些接口调用
	// */
	// protected abstract void initData();
	//
	// /**
	// * 初始化页面元素方法
	// */
	// protected abstract void findViews();
	//
	// /**
	// * 添加监听事件的方法
	// */
	// protected abstract void addAction();

	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_ID_EXIT) {
		} else {
			return true;
		}

		return false;
	}

	public void onPause() {
		super.onPause();
		isActivityInFront = false;
	}

	public void onResume() {
		super.onResume();
		isActivityInFront = true;
	}

	public void onSaveInstanceState(Bundle saveInsanceState) {
		super.onSaveInstanceState(saveInsanceState);
	}

	public void openUrlUseBrowser(String url) {
		Uri uri = Uri.parse(url); // url为你要链接的地址
		Intent intent = new Intent(Intent.ACTION_VIEW, uri)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	protected void processMessage(Message msg) {
	}

	// 在UI线程中运行call函数，一般call函数由子类复写，callID将作为call函数的参数
	// public void runCallFunctionInHandler(int callID, Object[] args) {
	// sendMessageToHanler(CALL_FUNCTION, args, callID);
	// }

	// 在UI线程中运行call函数，一般call函数由子类复写，callID将作为call函数的参数
	public void runCallFunctionInHandler(int callID, Object... args) {
		sendMessageToHanler(CALL_FUNCTION, args, callID);
	}

	public void selectPicture() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_SEL_PIC);
	}

	// 消息发送函数
	public void sendMessageToHanler(int what) {
		Message message = handler.obtainMessage();
		message.what = what;
		message.sendToTarget();
	}

	// 消息发送函数
	public void sendMessageToHanler(int what, int arg1) {
		Message message = handler.obtainMessage();
		message.what = what;
		message.arg1 = arg1;
		message.sendToTarget();
	}

	// 消息发送函数
	public void sendMessageToHanler(int what, Object obj) {
		Message message = handler.obtainMessage();
		message.what = what;
		message.obj = obj;
		message.sendToTarget();
	}

	// 消息发送函数
	public void sendMessageToHanler(int what, Object obj, int arg1) {
		Message message = handler.obtainMessage();
		message.what = what;
		message.obj = obj;
		message.arg1 = arg1;
		message.sendToTarget();
	}

	public void sendSMS(String smsBody) {

		Uri smsToUri = Uri.parse("smsto:");

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		startActivity(intent);

	}

	/**
	 * 安全的设置EditText中内容的方法,如果提交的str参数为空，则忽略该操作
	 * 
	 * @param editText
	 * @param str
	 */
	protected void setTextToEditText(EditText editText, String str) {
		if (editText == null) {
			return;
		}
		if (str == null) {
			return;
		}
		editText.setText(str);
	}

	public void showErrorDialog(String message) {
		sendMessageToHanler(SHOW_ERROR_DIALOG, message);
	}

	public void showMessageDialog(String message) {
		sendMessageToHanler(SHOW_MESSAGE_DIALOG, message);
	}

	public void showProgressDialog(String message) {
		sendMessageToHanler(SHOW_PROGRESS_DIALOG, message);
	}

	public void showProgressDialogCanCancel(String message, int callId) {
		sendMessageToHanler(SHOW_PROGRESS_DIALOG_CAN_CANCEL, message, callId);
	}

	public void showToastText(String text) {
		sendMessageToHanler(SHOW_TOAST_TEXT, text);

	}

	
}
