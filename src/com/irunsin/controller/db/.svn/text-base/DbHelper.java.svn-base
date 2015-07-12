package com.irunsin.controller.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.irunsin.controller.model.AlarmMode;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.model.MusicServiceModel;
import com.irunsin.controller.model.RememberDevices;
import com.irunsin.controller.model.SongListInfo;
import com.irunsin.controller.util.GB2Alpha;
import com.irunsin.controller.util.SortMusicUtil;

public class DbHelper {

	private String TAG = "DbHelper";
	private Context context;
	private static SQLiteDatabase sqlDb;

	private static DbHelper db = new DbHelper();

	private DbHelper() {

	}

	public static DbHelper getInstence(Context context) {
		if (sqlDb == null) {
			sqlDb = new DatabaseHelper(context.getApplicationContext(),
					"irunsin", 1).getWritableDatabase();
		}
		return db;
	}

	/*
	 * 创建数据库
	 */
	public void creatDB(Context context) {
		this.context = context;
		DatabaseHelper dbhelp = new DatabaseHelper(context, "irunsin");
		dbhelp.getReadableDatabase();
	}

	/*
	 * 新建歌单 list_name 歌单名称 list_label 歌单标签 list_describe 歌单描述
	 */
	public boolean creatSongList(String list_name, String list_label,
			String list_describe) {
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			sqlDb.execSQL("INSERT INTO playlist VALUES(" + null + ",'" + 1
					+ "', '" + list_name + "', '" + list_label + "', '"
					+ list_describe + "')");
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}

	/*
	 * 查询歌单列表
	 */
	public List<SongListInfo> getSongList() {
		List<SongListInfo> list = new ArrayList<SongListInfo>();
		Cursor c = sqlDb.rawQuery("SELECT * FROM playlist where style = '1'",
				null);
		while (c.moveToNext()) {
			int s = c.getInt(c.getColumnIndex("id"));
			String listname = c.getString(c.getColumnIndex("list_name"));
			String listlabel = c.getString(c.getColumnIndex("list_label"));
			String listdescrib = c.getString(c.getColumnIndex("list_describe"));
			SongListInfo songlist = new SongListInfo();
			songlist.setId(s);
			songlist.setList_name(listname);
			songlist.setList_describe(listdescrib);
			songlist.setList_label(listlabel);
			list.add(songlist);
		}
		c.close();
		return list;
	}

	/*
	 * 查询歌单 对应歌曲详细信息
	 */
	public List<String> getSongDetail(int list_id) {
		List<String> list = new ArrayList<String>();
		Cursor c = sqlDb.rawQuery("SELECT * FROM songdetail where list_id = '"
				+ list_id + "'", null);
		while (c.moveToNext()) {
			String s = c.getString(c.getColumnIndex("mp3_id"));
			list.add(s);
		}
		c.close();
		return list;
	}

	/*
	 * 添加数据到歌单列表
	 */
	public int insertMp3ToList(List<Mp3Info> list, int list_id) {
		int insertState = 0; // 0代表成功 1代表失败 2代表没有添加新的歌曲
		sqlDb.beginTransaction(); // 开始事务
		// 先查询出该ID对应的所有的数据
		List<String> listold = new ArrayList<String>();
		Cursor c = sqlDb.rawQuery("SELECT * FROM songdetail where list_id = '"
				+ list_id + "'", null);
		while (c.moveToNext()) {
			String s = c.getString(c.getColumnIndex("mp3_id"));
			listold.add(s);
		}
		c.close();
		// 过滤掉重复的数据
		int length = list.size();
		int flag = 0;
		int j = 0;
		while (flag < length) {
			boolean state = false; // true代表有删除 false代表没有
			for (int i = 0; i < listold.size(); i++) {
				if (list.get(j).getMp3id().equals(listold.get(i))) {
					list.remove(j);
					state = true;
					break;
				}
			}
			if (state) {
				flag++;
			} else {
				flag++;
				j++;
			}
		}
		if (list.size() == 0) {
			return 2;
		}

		try {
			for (Mp3Info mp3info : list) {
				sqlDb.execSQL("INSERT INTO songdetail VALUES(null, ?, ?)",
						new Object[] { mp3info.getMp3id(), list_id });
			}
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			insertState = 1;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		return insertState;
	}

	/*
	 * 删除歌单
	 */
	public boolean deletSongList(int list_id) {
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from playlist where id = '" + list_id + "'";
			sqlDb.execSQL(sql);
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}

	/*
	 * 删除歌曲
	 */
	public boolean deleteMusic(String mp3_id, int listid) {
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from songdetail where mp3_id = '" + mp3_id
					+ "' and list_id = " + listid + "";
			sqlDb.execSQL(sql);
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}

	/**
	 * 获取记住设备表中的详细信息
	 */
	public List<RememberDevices> getRemDevice() {
		List<RememberDevices> list = new ArrayList<RememberDevices>();
		Cursor c = sqlDb.rawQuery("SELECT * FROM remdevice", null);
		while (c.moveToNext()) {
			int id = c.getInt(c.getColumnIndex("id"));
			String ssdpString = c.getString(c.getColumnIndex("ssdp_pac"));
			String uuid = c.getString(c.getColumnIndex("uuid"));
			RememberDevices rd = new RememberDevices();
			rd.setId(id);
			rd.setSsdpString(ssdpString);
			rd.setUuid(uuid);
			list.add(rd);
		}
		c.close();
		return list;
	}

	/**
	 * 存入对应数据至于库中g
	 */
	public boolean insertRemedevice(String ssdpstring, String uuid,
			boolean oldflag) {
		boolean insertState = true;
		// oldflag字段标志着 是否库中已经记录的该值 若已有记录 则先删除
		if (oldflag) {
			sqlDb.beginTransaction(); // 开始事务
			try {
				sqlDb.execSQL("delete from remdevice");
				sqlDb.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				sqlDb.endTransaction();
			}
		}

		try {
			sqlDb.beginTransaction();
			sqlDb.execSQL("INSERT INTO remdevice VALUES(" + null + ", '"
					+ ssdpstring + "', '" + uuid + "')");
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			insertState = false;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		return insertState;
	}

	/**
	 * 删除存入对应数据中的device
	 */
	public boolean deleteDevice(int id) {
		sqlDb.beginTransaction();
		try {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlDb.endTransaction();
		}
		return false;
	}

	/**
	 * 添加音乐到喜欢的音乐中去 存在即删除 首先判断是否存在 不存在则添加 存在则删除
	 */
	public boolean addMuiscToFavorite(Mp3Info mp3info) {
		boolean insertState = true;
		sqlDb.beginTransaction();

		try {
			sqlDb.execSQL(
					"INSERT INTO favoritemusic VALUES(null, ?, ?,?,?,?,?,?,?)",
					new Object[] { mp3info.getMp3id(), mp3info.getMp3Name(),
							mp3info.getMp3Size(), mp3info.getFilepath(),
							mp3info.getArtist(), mp3info.getDuration(),
							mp3info.getDisplay_name(), mp3info.getLoOnFlag() });
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			insertState = false;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		return insertState;
	}

	/**
	 * 查询喜欢的音乐
	 */
	public List<Mp3Info> getFavoriteMusic() {
		List<Mp3Info> list = new ArrayList<Mp3Info>();
		Cursor c = sqlDb.rawQuery("SELECT * FROM favoritemusic", null);
		while (c.moveToNext()) {
			int keyid = c.getInt(c.getColumnIndex("id"));
			String mp3id = c.getString(c.getColumnIndex("mp3_id"));
			String mp3name = c.getString(c.getColumnIndex("mp3name"));
			String mp3size = c.getString(c.getColumnIndex("mp3size"));
			String filepath = c.getString(c.getColumnIndex("filepath"));
			String artist = c.getString(c.getColumnIndex("artist"));
			int durration = c.getInt(c.getColumnIndex("duration"));
			String display_name = c.getString(c.getColumnIndex("display_name"));
			int loconflag = c.getInt(c.getColumnIndex("type"));
			Mp3Info mp3info = new Mp3Info();
			mp3info.setArtist(artist);
			mp3info.setDisplay_name(display_name);
			mp3info.setDuration(durration);
			mp3info.setFilepath(filepath);
			mp3info.setMp3id(mp3id);
			mp3info.setKeyId(keyid);
			mp3info.setMp3Name(mp3name);
			mp3info.setMp3Size(mp3size);
			mp3info.setLoOnFlag(loconflag);
			list.add(mp3info);
		}
		c.close();

		return list;
	}
		
	/**
	 * 删除喜欢的音乐
	 */
	public boolean deleteFavoriteMusic(int keyid) {
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from favoritemusic where id = " + keyid + " ";
			sqlDb.execSQL(sql);
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}

	/**
	 * 添加新的闹钟 包括 新增一个 闹钟设置 新增对应闹钟设置对应的列表音乐
	 * 
	 */
	public boolean addAlarm(List<Mp3Info> list, String alarmId, String weeks,
			String starttime, String deviceName, int type) {

		// 生成对应的 闹钟ID
		// String alarmId = new SimpleDateFormat("yyyyMMddhhmm").format(new
		// Date());
		boolean insertState = true;
		sqlDb.beginTransaction();
		try {
			// 新增一个闹钟信息
			sqlDb.execSQL("INSERT INTO alarm VALUES(" + null + ", '" + alarmId
					+ "', '" + weeks + "', '" + starttime + "', " + type
					+ ", '" + deviceName + "')");
			// 新增闹钟对应的音乐信息
			for (int i = 0; i < list.size(); i++) {
				Mp3Info mp3info = list.get(i);											
				sqlDb.execSQL(
						"INSERT INTO alarmmusic VALUES(?,?,?,?,?,?,?,?)",
						new Object[] {
								mp3info.getMp3id(),
								mp3info.getMp3Name(),
								mp3info.getMp3Size(),
								mp3info.getFilepath(),
								mp3info.getArtist(),
								mp3info.getDuration(),
								mp3info.getDisplay_name(),
								alarmId});
			}
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			insertState = false;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		return insertState;
	}

	/**
	 * 查询对应闹钟 ID的 详情
	 */
	public AlarmMode getAlarmInfo(String alarmId) {
		AlarmMode alarmMode = new AlarmMode();
		List<Mp3Info> list = new ArrayList<Mp3Info>();
		Cursor c = sqlDb.rawQuery("SELECT * FROM alarm where alarm_id = '"
				+ alarmId + "'", null);
		while (c.moveToNext()) {
			String weeks = c.getString(c.getColumnIndex("weeks"));
			String time = c.getString(c.getColumnIndex("time"));
			int type = c.getInt(c.getColumnIndex("type"));
			String deviceName = c.getString(c.getColumnIndex("device"));
			alarmMode.setStartTime(time);
			alarmMode.setWeeks(weeks);
			alarmMode.setAlarmtype(type);
			alarmMode.setDeviceName(deviceName);
		}
		c.close();

		Cursor c1 = sqlDb.rawQuery(
				"SELECT * FROM alarmmusic where alarm_id = '" + alarmId + "'",
				null);
		while (c1.moveToNext()) {
			String mp3id = c1.getString(c1.getColumnIndex("mp3_id"));
			String mp3name = c1.getString(c1.getColumnIndex("mp3name"));
			String mp3size = c1.getString(c1.getColumnIndex("mp3size"));
			String filepath = c1.getString(c1.getColumnIndex("filepath"));
			String artist = c1.getString(c1.getColumnIndex("artist"));
			int durration = c1.getInt(c1.getColumnIndex("duration"));
			String display_name = c1.getString(c1
					.getColumnIndex("display_name"));
			Mp3Info mp3info = new Mp3Info();
			mp3info.setArtist(artist);
			mp3info.setDisplay_name(display_name);
			mp3info.setDuration(durration);
			mp3info.setFilepath(filepath);
			mp3info.setMp3id(mp3id);
			mp3info.setMp3Name(mp3name);
			mp3info.setMp3Size(mp3size);
			list.add(mp3info);
		}
		c1.close();
		alarmMode.setList(list);

		return alarmMode;
	}

	/**
	 * 查询获取所有的闹钟信息
	 */
	public List<AlarmMode> getAllAlarm() {
		List<AlarmMode> list = new ArrayList<AlarmMode>();

		Cursor c = sqlDb.rawQuery("SELECT * FROM alarm", null);
		while (c.moveToNext()) {
			AlarmMode alarmMode = new AlarmMode();
			String weeks = c.getString(c.getColumnIndex("weeks"));
			String time = c.getString(c.getColumnIndex("time"));
			int type = c.getInt(c.getColumnIndex("type"));
			String alarmId = c.getString(c.getColumnIndex("alarm_id"));
			alarmMode.setAlarmId(alarmId);
			alarmMode.setStartTime(time);
			alarmMode.setWeeks(weeks);
			alarmMode.setAlarmtype(type);
			list.add(alarmMode);
		}
		c.close();
		return list;
	}

	/**
	 * 修改闹钟信息
	 */
	public boolean updateAlarm(List<Mp3Info> list, String alarmId,
			String weeks, String starttime, String deviceName, int type) {

		// 修改闹钟主表的内容
		boolean insertState = true;
		sqlDb.beginTransaction();
		try {
			Object[] object = new Object[] { weeks, starttime, type,
					deviceName, alarmId };
			// 修改一个闹钟信息
			sqlDb.execSQL(
					"update alarm set weeks=?,time = ?,type = ?,device  = ? where alarm_id =?",
					object);
			// 删除对应的 闹钟ID对应的歌曲信息
			sqlDb.execSQL("delete from alarmmusic where alarm_id = '" + alarmId
					+ "'");
			// 新增闹钟对应的音乐信息
			// 新增闹钟对应的音乐信息
			for (int i = 0; i < list.size(); i++) {
				Mp3Info mp3info = list.get(i);
				sqlDb.execSQL(
						"INSERT INTO alarmmusic VALUES(?,?,?,?,?,?,?,?)",
						new Object[] {
								mp3info.getMp3id(),
								mp3info.getMp3Name(),
								mp3info.getMp3Size(),
								mp3info.getFilepath(),
								mp3info.getArtist(),
								mp3info.getDuration(),
								mp3info.getDisplay_name(),
								alarmId});				
			}
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			insertState = false;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		return insertState;
	}

	/**
	 * 删除闹钟
	 */
	public boolean deleteAlarm(String alarmId) {
		boolean deleteState = true;
		sqlDb.beginTransaction();
		try {
			// 删除闹钟主表中信息
			String sql = "delete from alarm where alarm_id = '" + alarmId + "'";
			sqlDb.execSQL(sql);
			// 删除闹钟对应音乐详情中的信息
			sqlDb.execSQL("delete from alarmmusic where alarm_id = '" + alarmId
					+ "'");
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			deleteState = false;
		} finally {
			sqlDb.endTransaction();
		}
		return deleteState;
	}

	/**
	 * 关闭闹钟
	 */
	public boolean shutdownClock(String alarm_id,int type){
		// 修改闹钟主表的内容
		boolean updateState = true;
		sqlDb.beginTransaction();
		try {				
			// 修改一个闹钟信息
			sqlDb.execSQL("update alarm set type="+type+" where alarm_id ='"+alarm_id+"'");
			
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			updateState = false;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		return updateState;
	}
	
	/**
	 * 刷新乐库时 查询本地音乐文件至于本地音乐列表中
	 */
	public boolean insertLocalMusic(List<Mp3Info> list) {
		boolean insertState = true;
		sqlDb.beginTransaction();
		GB2Alpha gb = new GB2Alpha();
		try {
			// 新增本地音乐信息
			for (int i = 0; i < list.size(); i++) {
				Mp3Info mp3info = list.get(i);
				String mp3name = mp3info.getMp3Name().replaceAll("\\|", "");
				sqlDb.execSQL(
						"INSERT INTO locallist VALUES(null, ?, ?,?,?,?,?,?,?,?)",
						new Object[] {
								mp3info.getMp3id(),
								mp3name,
								mp3info.getAlbumid(),
								mp3info.getMp3Size(),
								mp3info.getFilepath(),
								mp3info.getArtist(),
								mp3info.getDuration(),
								mp3info.getDisplay_name(),
								SortMusicUtil.getAlpha(gb.String2Alpha(mp3info
										.getDisplay_name())) });

				// sqlDb.execSQL("INSERT INTO locallist VALUES("+null+",'"+mp3info.getMp3id()+"', '"+mp3info.getMp3Name()+"', '"+mp3info.getMp3Size()+"', '"+mp3info.getFilepath()+"', '"+mp3info.getArtist()+"', "+mp3info.getDuration()+", '"+mp3info.getDisplay_name()+"', '"+SortMusicUtil.getAlpha(gb.String2Alpha(mp3info.getDisplay_name()))+"')");
			}
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			insertState = false;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		return insertState;
	}

	/**
	 * 判断本地音乐中是否存在此歌曲  如果存在  则不管  否则就添加至系统库
	 */
	public void updateMusicList(Mp3Info mp3info){
		
		Cursor c1 = sqlDb.rawQuery(
				"SELECT * FROM locallist where display_name = '" + mp3info.getDisplay_name() +"'", null);
		List<Mp3Info> list = new ArrayList<Mp3Info>();
		while (c1.moveToNext()) {
			String mp3id = c1.getString(c1.getColumnIndex("mp3_id"));
			Mp3Info m = new Mp3Info();
			mp3info.setMp3id(mp3id);
			list.add(m);
		}
		c1.close();
		//当列表中的数据为空时
		if(list.size() == 0){
			//生成MP3id
			String id = new SimpleDateFormat("mmssSSS").format(new Date());
			String mp3id =  id;
			mp3info.setMp3id(mp3id);
			list.add(mp3info);
			insertLocalMusic(list);
		}
	}
	
	/**
	 * 查询本地音乐列表
	 */
	public List<Mp3Info> getLocalMusicList() {
		List<Mp3Info> list = new ArrayList<Mp3Info>();
		Cursor c1 = sqlDb.rawQuery(
				"SELECT * FROM locallist order by firstletter", null);
		while (c1.moveToNext()) {
			String mp3id = c1.getString(c1.getColumnIndex("mp3_id"));
			String mp3name = c1.getString(c1.getColumnIndex("mp3name"));
			String mp3size = c1.getString(c1.getColumnIndex("mp3size"));
			String filepath = c1.getString(c1.getColumnIndex("filepath"));
			String artist = c1.getString(c1.getColumnIndex("artist"));
			int durration = c1.getInt(c1.getColumnIndex("duration"));
			String display_name = c1.getString(c1
					.getColumnIndex("display_name"));
			int keyid = c1.getInt(c1.getColumnIndex("id"));
			String albumId = c1.getString(c1.getColumnIndex("albumid"));
			Mp3Info mp3info = new Mp3Info();
			mp3info.setArtist(artist);
			mp3info.setDisplay_name(display_name);
			mp3info.setDuration(durration);
			mp3info.setFilepath(filepath);
			mp3info.setMp3id(mp3id);
			mp3info.setMp3Name(mp3name);
			mp3info.setMp3Size(mp3size);
			mp3info.setKeyId(keyid);
			mp3info.setAlbumid(Integer.parseInt(albumId));
			list.add(mp3info);			
		}				
		c1.close();
		return list;
	}

	/**
	 * 删除 本地的单首歌曲
	 */
	public boolean deleLocalMusic(int key) {
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from locallist where id = " + key + "";
			sqlDb.execSQL(sql);
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}

	/**
	 * 更新添加的音乐至于本地库中
	 */
	public boolean updateNewMusic(List<Mp3Info> list) {
		// 懒得去判断 对应添加的歌曲 以及减少 的歌曲 直接删除全部 重新全部添加
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from locallist";
			sqlDb.execSQL(sql);
			GB2Alpha gb = new GB2Alpha();
			for (int i = 0; i < list.size(); i++) {
				Mp3Info mp3info = list.get(i);
				String mp3name = mp3info.getMp3Name().replaceAll("\\|", "");
				sqlDb.execSQL(
						"INSERT INTO locallist VALUES(null, ?,?, ?,?,?,?,?,?,?)",
						new Object[] {
								mp3info.getMp3id(),
								mp3name,
								mp3info.getAlbumid(),
								mp3info.getMp3Size(),
								mp3info.getFilepath(),
								mp3info.getArtist(),
								mp3info.getDuration(),
								mp3info.getDisplay_name(),
								SortMusicUtil.getAlpha(gb.String2Alpha(mp3info
										.getDisplay_name())) });
			}
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}

	// 每播放一首歌 则将此歌曲插入对应库中 且将存在的原歌曲删除掉 且删除配置的对于的最近歌曲的数量的音乐
	public boolean insertrecently(Mp3Info mp3info) {
		String nowtime = new SimpleDateFormat("MMddHHmmssSSS")
				.format(new Date());
		// 如果存在 则删除之
		if (judgeRecently(mp3info)) {
			deleteRecently(mp3info);
		}
		boolean insertState = true;
		sqlDb.beginTransaction();
		try {
			sqlDb.execSQL(
					"INSERT INTO recentlymusic VALUES( ?, ?,?,?,?,?,?,?,?,?,?)",
					new Object[] { mp3info.getMp3id(), mp3info.getMp3Name(),
							mp3info.getMp3Size(), mp3info.getFilepath(),
							mp3info.getArtist(), mp3info.getDuration(),
							mp3info.getDisplay_name(), mp3info.getAlbumid(),
							mp3info.getLoOnFlag(), mp3info.getLogo(), nowtime });
			Log.i(TAG, "插入最近听的音乐成功");
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			insertState = false;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		deleteOldMusic();
		return insertState;
	}

	// 删除最近听的音乐中的歌曲
	public boolean deleteRecently(Mp3Info mp3info) {
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from recentlymusic where mp3_id = '"
					+ mp3info.getMp3id() + "'";
			sqlDb.execSQL(sql);
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}

	// 查询最近听的是否存在此歌曲
	public boolean judgeRecently(Mp3Info mp3info) {
		List<Mp3Info> list = new ArrayList<Mp3Info>();
		Cursor c = sqlDb.rawQuery("SELECT * FROM recentlymusic where mp3_id = '"
				+ mp3info.getMp3id()+"'", null);
		if (c.getColumnCount() >= 1) {
			return true;
		}
		return false;
	}

	// 删除所有最近听的歌曲
	public boolean deleteAllRecently() {
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from recentlymusic";
			sqlDb.execSQL(sql);
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}

	// 判断如果超过了 配置的数据 则删除以前的数据
	public void deleteOldMusic() {
		// int total =
		// Integer.parseInt(context.getString(R.string.recentnumber));
		int total = 40;
		Cursor c = sqlDb.rawQuery("SELECT * FROM recentlymusic order by time",
				null);
		List<String> list = new ArrayList<String>();
		while (c.moveToNext()) {
			String mp3id = c.getString(c.getColumnIndex("mp3_id"));
			list.add(mp3id);
		}
		Log.i(TAG, "总共多条数据" + list.size());
		c.close();
		if (c != null && list.size() > total) {
			Log.i(TAG, "进入删除多余音乐的操作处理  即将 删除" + (list.size() - total) + "条数据");
			sqlDb.beginTransaction();

			try {

				for (int i = 0; i < list.size() - total; i++) {
					String sql = "delete from recentlymusic where mp3_id = '"
							+ list.get(i) + "'";
					sqlDb.execSQL(sql);
				}
				sqlDb.setTransactionSuccessful();
			} catch (Exception e) {
				Log.i(TAG, e.getMessage());
			} finally {
				sqlDb.endTransaction();
			}
		}
	}

	// 查询出所有喜欢的音乐
	public List<Mp3Info> getallRecentlyMusic() {
		List<Mp3Info> list = new ArrayList<Mp3Info>();
		Cursor c1 = sqlDb.rawQuery("SELECT * FROM recentlymusic order by time",
				null);
		while (c1.moveToNext()) {
			String mp3id = c1.getString(c1.getColumnIndex("mp3_id"));
			String mp3name = c1.getString(c1.getColumnIndex("mp3name"));
			String mp3size = c1.getString(c1.getColumnIndex("mp3size"));
			String filepath = c1.getString(c1.getColumnIndex("filepath"));
			String artist = c1.getString(c1.getColumnIndex("artist"));
			int durration = c1.getInt(c1.getColumnIndex("duration"));
			String display_name = c1.getString(c1
					.getColumnIndex("display_name"));
			String albumId = c1.getString(c1.getColumnIndex("albumid"));
			int loonflag = c1.getInt(c1.getColumnIndex("loonflag"));
			String logo = c1.getString(c1.getColumnIndex("logo"));
			Mp3Info mp3info = new Mp3Info();
			mp3info.setArtist(artist);
			mp3info.setDisplay_name(display_name);
			mp3info.setDuration(durration);
			mp3info.setFilepath(filepath);
			mp3info.setMp3id(mp3id);
			mp3info.setMp3Name(mp3name);
			mp3info.setMp3Size(mp3size);
			mp3info.setAlbumid(Integer.parseInt(albumId));
			mp3info.setLoOnFlag(loonflag);
			mp3info.setLogo(logo);
			list.add(mp3info);
		}
		c1.close();
		return list;
	}
	
	
	/**
	 * 添加音乐服务
	 */
	public boolean insertMusicService(MusicServiceModel data){
		boolean insertState = true;
		sqlDb.beginTransaction();

		try {
			sqlDb.execSQL(
					"INSERT INTO musicservice VALUES(?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { data.getMuseid(),data.getMusenmae(),data.getAccess_token(),data.getRefresh_token(),data.getToken_time(),data.getExpires_in(),data.getRe_expires_in(),data.getR1_expires_in(),data.getR2_expires_in(),data.getW1_expires_in(),data.getW2_expires_in()});
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			insertState = false;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		return insertState;
	}
	
	/**
	 * 查询现有的音乐服务
	 */
	public List<MusicServiceModel> getMusicService(){
		List<MusicServiceModel> list = new ArrayList<MusicServiceModel>();
		Cursor c1 = sqlDb.rawQuery("SELECT * FROM musicservice",
				null);
		while (c1.moveToNext()) {
			String museid = c1.getString(c1.getColumnIndex("museid"));
			String musename = c1.getString(c1.getColumnIndex("musename"));
			String access_token = c1.getString(c1.getColumnIndex("access_token"));
			String refresh_token = c1.getString(c1.getColumnIndex("refresh_token"));
			String token_time = c1.getString(c1.getColumnIndex("token_time"));
			String expires_in = c1.getString(c1.getColumnIndex("expires_in"));
			String re_expires_in = c1.getString(c1.getColumnIndex("re_expires_in"));
			String r1_expires_in = c1.getString(c1.getColumnIndex("r1_expires_in"));
			String r2_expires_in = c1.getString(c1.getColumnIndex("r2_expires_in"));
			String w1_expires_in = c1.getString(c1.getColumnIndex("w1_expires_in"));
			String w2_expires_in = c1.getString(c1.getColumnIndex("w2_expires_in"));
			MusicServiceModel model = new MusicServiceModel();
			model.setMuseid(museid);
			model.setMusenmae(musename);
			model.setAccess_token(access_token);
			model.setRefresh_token(refresh_token);
			model.setToken_time(token_time);
			model.setExpires_in(expires_in);
			model.setRe_expires_in(re_expires_in);
			model.setR1_expires_in(r1_expires_in);
			model.setR2_expires_in(r2_expires_in);
			model.setW1_expires_in(w1_expires_in);
			model.setW2_expires_in(w2_expires_in);
			list.add(model);
		}
		c1.close();
		return list;
	}
	
	/**
	 * 删除音乐服务
	 */
	public boolean deletems(String museid){
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from musicservice where museid = '" + museid + "'";
			sqlDb.execSQL(sql);
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}
	
	/**
	 * 当前音乐播放列表查询
	 */
	public List<Mp3Info> getcurrmp3(){
		List<Mp3Info> list = new ArrayList<Mp3Info>();
		Cursor c1 = sqlDb.rawQuery("SELECT * FROM currentlist",
				null);
		while (c1.moveToNext()) {
			Mp3Info mp3info = new Mp3Info();
			String mp3_id = c1.getString(c1.getColumnIndex("mp3_id"));
			String mp3name = c1.getString(c1.getColumnIndex("mp3name"));
			String filepath = c1.getString(c1.getColumnIndex("filepath"));
			String artist = c1.getString(c1.getColumnIndex("artist"));
			String display_name = c1.getString(c1.getColumnIndex("display_name"));
			int loonflag = c1.getInt(c1.getColumnIndex("loonflag"));
			String logo = c1.getString(c1.getColumnIndex("logo"));
			mp3info.setMp3id(mp3_id);
			mp3info.setMp3Name(mp3name);
			mp3info.setFilepath(filepath);
			mp3info.setDisplay_name(display_name);
			mp3info.setArtist(artist);
			mp3info.setLogo(logo);
			mp3info.setLoOnFlag(loonflag);			
			list.add(mp3info);
		}
		c1.close();
		return list;
	}
	
	/**
	 * 插入当前音乐播放列表
	 */
	public boolean insertCurrmp3(List<Mp3Info> list){
		boolean insertState = true;
		sqlDb.beginTransaction();
		
		//先删除列表中所有的数据
		String sql = "delete from currentlist";
		sqlDb.execSQL(sql);
		
		try {
			for (int i = 0; i < list.size(); i++) {
				Mp3Info mp3info = list.get(i);
				String mp3name = mp3info.getMp3Name().replaceAll("\\|", "");
				sqlDb.execSQL(
						"INSERT INTO currentlist VALUES(?, ?,?,?,?,?,?)",
						new Object[] {
								mp3info.getMp3id(),
								mp3name,
								mp3info.getAlbumid(),
								mp3info.getMp3Size(),
								mp3info.getFilepath(),
								mp3info.getArtist(),
								mp3info.getDuration(),
								mp3info.getDisplay_name()
								 });
			}
			sqlDb.setTransactionSuccessful(); // 设置事务成功完成
		} catch (Exception e) {
			e.printStackTrace();
			insertState = false;
		} finally {
			sqlDb.endTransaction(); // 结束事务
		}
		return insertState;
	}
	
	/**
	 * 删除播放列表中的某首音乐
	 */
	public boolean deletecurr(String mp3id){
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from currentlist where mp3_id = '" + mp3id + "'";
			sqlDb.execSQL(sql);
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}
	
	/**
	 * 删除播放列表中所有数据
	 */
	public boolean deleteallcurr(){
		boolean state = true;
		sqlDb.beginTransaction();
		try {
			String sql = "delete from currentlist";
			sqlDb.execSQL(sql);
			sqlDb.setTransactionSuccessful();
		} catch (Exception e) {
			state = false;
		} finally {
			sqlDb.endTransaction();
		}
		return state;
	}
}
