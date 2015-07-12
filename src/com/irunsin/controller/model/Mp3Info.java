package com.irunsin.controller.model;

import java.io.Serializable;

public class Mp3Info implements Serializable {
	
	private int keyId; // 库中的主键ID
	private String mp3id;// MP3id
	private String mp3Name;
	private String mp3Size;
	private String filepath;
	private String artist;
	private int duration;
	private String display_name;
	private long albumid;	
	private int LoOnFlag = 0;// 标志本地还是网络在线音乐 0本地 1在线
	private String logo = "";

	private String lrcneturl = "";//网络歌词地址
	
	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public String getMp3id() {
		return mp3id;
	}

	public void setMp3id(String mp3id) {
		this.mp3id = mp3id;
	}

	public String getMp3Name() {
		return mp3Name;
	}

	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}

	public String getMp3Size() {
		return mp3Size;
	}

	public void setMp3Size(String mp3Size) {
		this.mp3Size = mp3Size;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public long getAlbumid() {
		return albumid;
	}

	public void setAlbumid(long albumid) {
		this.albumid = albumid;
	}

	public int getLoOnFlag() {
		return LoOnFlag;
	}

	public void setLoOnFlag(int loOnFlag) {
		LoOnFlag = loOnFlag;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLrcneturl() {
		return lrcneturl;
	}

	public void setLrcneturl(String lrcneturl) {
		this.lrcneturl = lrcneturl;
	}

	
}
