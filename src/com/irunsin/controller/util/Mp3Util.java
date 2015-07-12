package com.irunsin.controller.util;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.irunsin.controller.db.DbHelper;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.sys.Global;
import com.irunsin.syncctrl.R;

import davaguine.jmac.decoder.IAPEDecompress;
import davaguine.jmac.info.APEInfo;
import davaguine.jmac.info.APETag;


public class Mp3Util {

	

	public static String toGb2312(String str) {
		if (str == null)
			return null;
		String retStr = str;
		byte b[];
		try {
			b = str.getBytes("ISO8859_1");

			for (int i = 0; i < b.length; i++) {
				byte b1 = b[i];
				if (b1 == 63)
					break; // 1
				else if (b1 > 0)
					continue;// 2
				else if (b1 < 0) { // 不可能为0，0为字符串结束符
					if (judgesc(str)) {

					} else {
						retStr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {

		}
		return retStr;
	}

	/*
	 * 判断如果存在 输入法的 特殊字符 则不
	 */
	public static boolean judgesc(String s) {
		boolean state = false;
		String[] regEx = { "じ", "ò", "び", "é", "É" };
		for (int i = 0; i < regEx.length; i++) {
			if (s.indexOf(regEx[i]) != -1) {
				state = true;
				break;
			}
		}
		return state;
	}

	/**
	 * 把对应的获取到的DLNA设备的当前音乐的播放时间 转换成秒
	 * 
	 * @param length
	 *            00:00:00或者00:00
	 * @return The length in seconds.
	 */
	public static int getIntLength(String length) {
		if (TextUtils.isEmpty(length)) {
			return 0;
		}
		String[] split = length.split(":");
		int count = 0;
		try {
			if (split.length == 3) {
				count += (Integer.parseInt(split[0])) * 60 * 60;
				count += Integer.parseInt(split[1]) * 60;
				count += Integer.parseInt(split[2]);
			} else if (split.length == 2) {
				count += Integer.parseInt(split[0]) * 60;
				count += Integer.parseInt(split[1]);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 由秒转换成对应的时间 xxx to xx:xx:xx 以秒为单位
	 */
	public static String getStringLength(int length) {
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
		if (reconds > 0 && reconds >= 10) {
			r = reconds + "";
		} else {
			r = "0" + reconds;
		}

		String string = m + ":" + r;
//		if (h.equals("00")) {
//			string = m + ":" + r;
//		}
		return string;
	}

	public static void RealdeletSong(Context context, long id, int keyid) {
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { MediaStore.Audio.Media.DATA };
		Cursor cursor = contentResolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, "_id="
						+ id, null, null);

		int count = cursor.getCount();
		if (cursor != null) {
			// 从数据库中删除数据
			contentResolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					"_id=" + id, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String name = cursor.getString(0);

				File file = new File(name);
				try {
					// 判断是否成功从sd卡删除文件
					if (!file.delete()) {
						Global.debug("---不能删除文件----");
					}
					cursor.moveToNext();
				} catch (SecurityException e) {
					// TODO: handle exception
					cursor.moveToNext();
				}
			}
			cursor.close();
		}

		DbHelper.getInstence(context).deleLocalMusic(keyid);
		Toast.makeText(context, "成功删除" + count + "个文件", Toast.LENGTH_SHORT)
				.show();
		contentResolver.notifyChange(Uri.parse("content://media"), null);
	}

	// 修改库中存放的模式值
	public static void updateMode(int mode, Context context,
			SharedPreferences myPrefs) {
		IrunSinApplication irunsin = (IrunSinApplication) context
				.getApplicationContext();
		irunsin.setPLAY_MODE(mode);
		// 存入数据
		Editor editor = myPrefs.edit();
		editor.putInt("PLAY_MODE", mode);
		editor.commit();
	}

	/**
	 * 将MP3里图片读取出来
	 * 
	 * @param context
	 * @param songid
	 * @param albumid
	 * @return
	 */
	public static Bitmap getMusicBitemp(Context context, long songid,
			long albumid) {

		Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.play_info_pic);
		// Uri sArtworkUri = Uri
		// .parse("content://media/external/audio/media/" + songid
		// + "/albumart");

		Uri sArtworkUri = Uri.parse("content://media/external/audio/albums");
		// 判断相关数据
		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}
		try {
			boolean state = true;
			if (songid > 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					state = false;
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}
			}

			if (state) {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}

			}
		} catch (FileNotFoundException ex) {
		}
		return bm;
	}

	public static Bitmap getArtwork(Context context, String filepath) {
		AudioFileIO audiofile = new AudioFileIO();
		File file = new File(filepath);
		AudioFile audio;
		try {
			audio = audiofile.readFile(file);
			Tag tag = audio.getTag();
			Artwork art = tag.getFirstArtwork();
			if(art == null){
				return getDefaultArtwork(context);
			}
			byte[] b =art.getBinaryData();
			Bitmap bit = BitmapFactory.decodeByteArray(b, 0, b.length);
			return bit;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		
	}

	private static Bitmap getArtworkFromFile(Context context, long songid,
			long albumid) {
		Bitmap bm = null;
		byte[] art = null;
		String path = null;
		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}
		try {
			if (albumid < 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}
			} else {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);					
				}
			}
		} catch (FileNotFoundException ex) {

		}
		if (bm != null) {
			mCachedBit = bm;
		}
		return bm;
	}

	public static Bitmap getDefaultArtwork(Context context) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeStream(context.getResources()
				.openRawResource(R.drawable.play_info_pic), null, opts);
	}

	private static final Uri sArtworkUri = Uri
			.parse("content://media/external/audio/albumart");
	private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	private static Bitmap mCachedBit = null;
	
	
	//根据传递进来的url解析音频文件获取 tag信息插入应用库
	public static void addlocalmusic(String url,AudioFileIO afio,Context context){
		//因为ape文件的解析采用的单独的包   故需要判断分开
		if(url.endsWith("ape")){
			davaguine.jmac.tools.File filing;
			try {
				filing = davaguine.jmac.tools.File.createFile(url, "r");
				APEInfo apeinfo = IAPEDecompress.CreateAPEInfo(filing);
				IAPEDecompress iapedec = IAPEDecompress.CreateIAPEDecompressCore(apeinfo, -1, -1);				
				APETag tag = iapedec.getApeInfoTag();
				String artist = tag.GetFieldString("Artist");
				String mp3Name = tag.GetFieldString("Title");				
				String mp3Size = new File(url).length()+"";
				String filepath = url;
				int duration = 0;
				String data[] = url.split("/");
				String display = data[data.length-1];
				if(artist == null){
					artist = display;
				}
				if(mp3Name == null){
					mp3Name = display;
				}
				Mp3Info mp3info = new Mp3Info();
				mp3info.setArtist(artist);
				mp3info.setDisplay_name(display);
				mp3info.setAlbumid(0);
				mp3info.setDuration(0);
				mp3info.setFilepath(url);
				mp3info.setLoOnFlag(0);
				mp3info.setMp3Name(mp3Name);
				mp3info.setMp3Size(mp3Size);
				DbHelper.getInstence(context).updateMusicList(mp3info);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Global.debug("----------------------->当前歌曲为 = " +url);
				return;			
			}
										
		}else{
			File file = new File(url);
			try {
				AudioFile audio = afio.readFile(file);
				AudioHeader audioh = audio.getAudioHeader();
				Tag tag = audio.getTag();
				String artist = null ;
				
				String mp3Name = null;
				String mp3Size = null;
				String filepath = url;	
				int duration = 0;
				try{
					duration = audioh.getTrackLength();
					if(duration<65){//当歌曲时长小于 65时认为是   铃声   则删除
						return;
					}
				}catch(Exception e){
					
				}
				String data[] = url.split("/");
				String display = data[data.length-1];
				if(tag != null){
					artist = tag.getFirst(FieldKey.ARTIST);
					if(artist.equals("")){
						artist = tag.getFirst(FieldKey.ALBUM_ARTIST);
						if(artist.equals("")){
							artist = "<unknow>";
						}
					}
					mp3Name = tag.getFirst(FieldKey.TITLE);
					mp3Size = file.length()+"";
									
				}else{
					artist = display;
					mp3Name = display;
					mp3Size = "12345";
				}
				
				Mp3Info mp3info = new Mp3Info();
				mp3info.setArtist(artist);
				mp3info.setDisplay_name(display);
				mp3info.setAlbumid(0);
				mp3info.setDuration(0);
				mp3info.setFilepath(url);
				mp3info.setLoOnFlag(0);
				mp3info.setMp3Name(mp3Name);
				mp3info.setMp3Size(mp3Size);
				DbHelper.getInstence(context).updateMusicList(mp3info);
			} catch (Exception e) {
				Global.debug("------------------------>当前歌曲为 = " +url);
				return;
			} 
		}
		
	}
	
	/*
	 * 音乐开始播放公共方法
	 */
	public static void play(Context context, int position,List<Mp3Info> list) {		
		IrunSinApplication irunsin = (IrunSinApplication) context.getApplicationContext();		
		//判断订阅的时间
		
		irunsin.setListmp3(list);
		boolean flag = judgetime(irunsin);
		if(flag){						
			Intent intent = new Intent();
			intent.putExtra("position", position);
			intent.putExtra("FLAG", 0);
//			intent.setClass(context, MusicPlayService.class);
			context.startService(intent);
		}else{
			Intent intent = new Intent();
			intent.setAction("com.mp3.frecontrol");
			context.sendBroadcast(intent);
		}		
	}
	
	
	
		
	/**
	 * 图片大小处理类
	 */
	public static Bitmap dealPic(Bitmap bitmap,Context context){
		Bitmap update = null;
		//定义缩放图片的大小
		int[] height = {64,32,16,8};
		
		final float densityMultiplier = context.getResources().getDisplayMetrics().density;        
		for (int i = 0; i < height.length; i++) {
			int newHeight = height[i];
			int h= (int) (newHeight*densityMultiplier);
			int w= (int) (h * bitmap.getWidth()/((double) bitmap.getHeight()));
			update=Bitmap.createScaledBitmap(bitmap, w, h, true);
			if(sizeOf(update)<20000){
				Global.debug("当前显示的图片的大小为 pppppppppppp= "+sizeOf(update));
				return update;
			}
		}				
		return update;
	}
	
	/**
	 * 计算图片大小
	 * @param data
	 * @return
	 */
	public static int sizeOf(Bitmap data) {        
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {           
    		return data.getRowBytes() * data.getHeight();       
    		} else {           
    			return data.getByteCount();       
    			}    
    	}
	
	@SuppressLint("SimpleDateFormat")
	public static boolean judgetime(IrunSinApplication irunsin){		
		long oldtime = irunsin.getPlaytime();
		String nowtimeStr = new SimpleDateFormat("MMddHHmmssSSS")
				.format(new Date());
		long nowtime = Long.parseLong(nowtimeStr);
		if (oldtime == 0 || nowtime > (oldtime + 3000)) {
			return true;
		} else {
			return false;
		}		
	}
}
