package com.irunsin.controller.sys;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

public class Global {
	// 日志打印的TAG名字
	public static final String LOG_TAG = "irunsin";
	public static final String PLAT = "0";

	// 是否打印日志
	public static final boolean isDebug = true;
	public static final int spaceDay = 7;
	public static final String SERIVECE_PHONE = "020-81085510";
	public static final String imageUrl = "http://59.41.9.184:80/hospital";
	public static boolean isLogin = false;

	public static String hospitalId = "";
	public static String hospitalName = "";
	public static String deptId = "";
	public static String deptName = "";
	public static String doctorName = "";
	public static String doctorTitle = "";
	public static String doctorId = "";
	public static String parentId = "";
	public static String hasChild = "";
	public static String desc = "";

	public static String regDate = "";
	public static String regFee = "";
	public static String treatFee = "";
	public static String timeFlag = "";
	// public static String docDesc = "";
	public static String startTime = "";
	public static String endTime = "";
	// public static int countFee = 0;
	public static String isTimeReg = "0";
	public static String deptDesc = "";
	public static String doctorDesc = "";

	public static String userNo = "";
	public static String userIdCard = "";
	public static String userName = "";
	public static String userGender = "";
	public static String userBirthday = "";
	public static String pluginId = "";
	public static String pluginName = "";
	public static String token = "";
	public static String userJKK = "";
	public static String userSMK = "";
	public static String userYBK = "";
	public static String mobile = "";
	public static String callbackId = "";
	public static String dataStr = "";
	public static Bitmap hosBitmap;
	public static Bitmap docBitmap;
	public static String datePickerTime;
	public static String paymentHospitalName;
	public static String paymentHospitalId;
	public static boolean isToday = false;
	public static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT";
	public static final String CHILD_FRAGMENT_TAG = "CHILD_FRAGMENT";
	public static boolean backToMain = false;

	// 图片缓存位置
	public static final String IMG_CACHE_NAME = Environment
			.getExternalStorageDirectory() + "/irunsin/img/";

	public static final String PLUGIN_CACHE_NAME = Environment
			.getExternalStorageDirectory() + "/irunsin/plugins/";

	public static final String TAKE_PIC_CACHE_NAME = Environment
			.getExternalStorageDirectory() + "/irunsin/img0/";

	// 删除指定文件夹下的所有文件
	public static void deleteFilesInFloder(String floderName) {
		File file = new File(floderName);
		if (file == null || !file.exists()) {
			error(floderName + "文件夹不存在");
			return;
		}
		for (File fileInFd : file.listFiles()) {
			if (fileInFd.isFile()) {
				fileInFd.delete();
				Global.debug("删除文件:" + fileInFd.getAbsolutePath());
			} else if (fileInFd.isDirectory()) {
				deleteFilesInFloder(fileInFd.getAbsolutePath());
			}
		}
		Global.debug(floderName + "文件夹下的文件已经删除");
	}

	/**
	 * 退出程序
	 */
	public static void exit() {
		int listSize = BaseActivity.activityList.size();
		for (int i = 0; i < listSize; i++) {
			BaseActivity.activityList.get(i).finish();
		}		
		Process.killProcess(Process.myPid());
		System.exit(0);
	}

	public static void debug(Object info) {
		if (info != null && isDebug) {
			// 分段打印数据
			// for (int i = 200; i < info.toString().length(); i += 200) {
			// int j = i - 200;
			// Log.d(LOG_TAG, info.toString().substring(j, i));
			// }
			Log.d(LOG_TAG, info.toString());
		}
	}

	public static void error(Object msg) {
		debug(msg);
	}

	public static void error(String msg, Exception e) {
		if (msg != null && isDebug) {
			if (e == null) {
				Log.e(LOG_TAG, msg);
			} else {
				Log.e(LOG_TAG, msg, e);
			}
		}
	}

	public static Bitmap createRepeater(int width, Bitmap src) {
		int count = (width + src.getWidth() - 1) / src.getWidth();

		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		for (int idx = 0; idx < count; ++idx) {

			canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
		src.recycle();
		return bitmap;
	}

	/** 日期格式化对象 */
	public static SimpleDateFormat simpleDateFormat;

	/** 格式化时间 */
	public static String formatTime(long time) {
		if (simpleDateFormat == null) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
		return simpleDateFormat.format(new Date(time * 1000));
	}

	/** 格式化日期 */
	public static String formatDate(String time) {
		return getMonthDate.format(new Date(Long.parseLong(time)));
	}

	/** 获取报文日期格式对象 */
	public static SimpleDateFormat getDate = new SimpleDateFormat("yyyy-MM-dd");

	/** 获取月份和日期格式对象 */
	public static SimpleDateFormat getMonthDate = new SimpleDateFormat(
			"yy-MM-dd");

	/** 获取当前系统日期 */
	public static String getTodayDate() {
		String startDate = getDate.format(new java.util.Date());
		return startDate;
	}

	/** 获取明天系统日期 */
	public static String getStartDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		String startDate = getDate.format(new Date(cal.getTimeInMillis()));
		return startDate;
	}

	/** 获取一周后日期 */
	public static String getEndDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, spaceDay);
		String endDate = getDate.format(new Date(cal.getTimeInMillis()));
		return endDate;
	}

	/** 将字符串字符转为数字 */
	public static int convert_num(String val) {

		int return_val = 0;
		char[] char_arr = val.toCharArray();

		for (int i = 0; i < char_arr.length; i++) {
			char c = char_arr[i];
			// 例如： 100 = 10 *10
			int val_v = (int) Math.pow(10, char_arr.length - (i + 1));
			return_val = return_val + (c - 48) * val_v;
		}
		return return_val;
	}

	/** 将字符串字符转为数字 */
	public static int convert_fee(String val) {

		if (val != null && !StreamsUtils.isStrBlank(val)) {
			int return_fee = Integer.parseInt(val);
			return return_fee;
		} else {
			return 0;
		}
	}

}
