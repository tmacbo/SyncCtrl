package com.alibaba.model;

import java.io.Serializable;

public class Song implements Serializable {

	private Long album_id;

	private String album_logo;

	private String album_name;

	private Long artist_id;

	private String artist_logo;

	private String artist_name;

	private Long demo;

	private Boolean favourite;

	private Long length;

	private String listen_file;

	private String logo;

	private String lyric_file;

	private String lyric_text;

	private String name;

	private Long playAuthority;

	private Long playCounts;

	private Long playSeconds;

	private Long recommends;

	private String singers;

	private Long song_id;

	private String song_name;

	private String title;

	public Long getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(Long album_id) {
		this.album_id = album_id;
	}

	public String getAlbum_logo() {
		return album_logo;
	}

	public void setAlbum_logo(String album_logo) {
		this.album_logo = album_logo;
	}

	public String getAlbum_name() {
		return album_name;
	}

	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}

	public Long getArtist_id() {
		return artist_id;
	}

	public void setArtist_id(Long artist_id) {
		this.artist_id = artist_id;
	}

	public String getArtist_logo() {
		return artist_logo;
	}

	public void setArtist_logo(String artist_logo) {
		this.artist_logo = artist_logo;
	}

	public String getArtist_name() {
		return artist_name;
	}

	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}

	public Long getDemo() {
		return demo;
	}

	public void setDemo(Long demo) {
		this.demo = demo;
	}

	public Boolean getFavourite() {
		return favourite;
	}

	public void setFavourite(Boolean favourite) {
		this.favourite = favourite;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public String getListen_file() {
		return listen_file;
	}

	public void setListen_file(String listen_file) {
		this.listen_file = listen_file;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLyric_file() {
		return lyric_file;
	}

	public void setLyric_file(String lyric_file) {
		this.lyric_file = lyric_file;
	}

	public String getLyric_text() {
		return lyric_text;
	}

	public void setLyric_text(String lyric_text) {
		this.lyric_text = lyric_text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPlayAuthority() {
		return playAuthority;
	}

	public void setPlayAuthority(Long playAuthority) {
		this.playAuthority = playAuthority;
	}

	public Long getPlayCounts() {
		return playCounts;
	}

	public void setPlayCounts(Long playCounts) {
		this.playCounts = playCounts;
	}

	public Long getPlaySeconds() {
		return playSeconds;
	}

	public void setPlaySeconds(Long playSeconds) {
		this.playSeconds = playSeconds;
	}

	public Long getRecommends() {
		return recommends;
	}

	public void setRecommends(Long recommends) {
		this.recommends = recommends;
	}

	public String getSingers() {
		return singers;
	}

	public void setSingers(String singers) {
		this.singers = singers;
	}

	public Long getSong_id() {
		return song_id;
	}

	public void setSong_id(Long song_id) {
		this.song_id = song_id;
	}

	public String getSong_name() {
		return song_name;
	}

	public void setSong_name(String song_name) {
		this.song_name = song_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
