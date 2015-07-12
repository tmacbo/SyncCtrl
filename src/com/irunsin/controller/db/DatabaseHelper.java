package com.irunsin.controller.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, 1);
	}

	/*
	 * 第一次创建数据库时执行
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建本地音乐列表
		db.execSQL("create table IF NOT EXISTS locallist(id INTEGER primary key AUTOINCREMENT,mp3_id varchar,mp3name varchar,albumid varchar,mp3size varchar,filepath varchar,artist varchar,duration integer,display_name varchar,firstletter varchar)");
		// TODO Auto-generated method stub
		db.execSQL("create table IF NOT EXISTS playlist(id INTEGER PRIMARY KEY AUTOINCREMENT,style int,list_name varchar,list_describe varchar,list_label varchar)");
		db.execSQL("create table IF NOT EXISTS songdetail(id INTEGER primary key AUTOINCREMENT,mp3_id varchar,list_id int)");
		db.execSQL("create table IF NOT EXISTS remdevice(id INTEGER primary key AUTOINCREMENT,ssdp_pac varchar,uuid varchar)");
		// favoritemusic 中type代表类型 0 为本地音乐 1为网络在线音乐
		db.execSQL("create table IF NOT EXISTS favoritemusic(id INTEGER primary key AUTOINCREMENT,mp3_id varchar,mp3name varchar,mp3size varchar,filepath varchar,artist varchar,duration integer,display_name varchar,type integer)");
		// alarmusic代表存放闹钟的音乐 type代表开关 0为开启 1为关闭
		db.execSQL("create table IF NOT EXISTS alarm(id INTEGER primary key AUTOINCREMENT,alarm_id varchar,weeks varchar,time varchar,type INTEGER,device varchar)");
		db.execSQL("create table IF NOT EXISTS alarmmusic(mp3_id varchar,mp3name varchar,mp3size varchar,filepath varchar,artist varchar,duration integer,display_name varchar,alarm_id varchar)");
		// 创建最近播放的音乐
		db.execSQL("create table IF NOT EXISTS recentlymusic(mp3_id varchar,mp3name varchar,mp3size varchar,filepath varchar,artist varchar,duration integer,display_name varchar,albumid varchar,loonflag integer,logo varchar,time integer)");	
		/***
		 * museid  服务ID  musenmae服务名称    access_token 授权码  refresh_token 刷新码  token_time 授权码有效期开始时间
		 * expires_in 授权码过期时间  re_expires_in 刷新码过期时间 r1_expires_in  r1级别API或字段的访问过期时间
		 * r2_expires_in  r2级别API或字段的访问过期时间  w1_expires_in  w1级别API或字段的访问过期时间  w2_expires_in   w2级别API或字段的访问过期时间
		 */
		db.execSQL("create table IF NOT EXISTS musicservice(museid varchar,musename varchar,access_token varchar,refresh_token varchar,token_time varchar,expires_in varchar,re_expires_in varchar,r1_expires_in varchar,r2_expires_in varchar,w1_expires_in varchar,w2_expires_in varchar)");		
		
		/**
		 * 播放器当前播放列表
		 */
		db.execSQL("create table IF NOT EXISTS currentlist(mp3_id varchar,mp3name varchar,filepath varchar,artist varchar,display_name varchar,loonflag integer,logo varchar)");
	}

	/*
	 * 当版本变化时调用此方法
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
