package com.alibaba.model;

import java.io.Serializable;
import java.util.List;

public class AlbumDetail implements Serializable {
	private String album_category;

	private Long album_id;

	private String album_name;

	private Long artist_id;

	private String artist_Logo;

	private String artist_name;

	private Long category;

	private Long cd_count;

	private Boolean checkRate;

	private Long collects;

	private Long comments;

	private String company;

	private String description;

	private Long gmtPublish;

	private Long grade;
	private Long is_check;

	private Long is_play;

	private String language;

	private String logo;

	private Long playAuthority;

	private Long playCount;

	private Long recommends;

	private Long song_count;

	private List<Song> songs;

	private Long status;

	private String sub_title;

	public String getAlbum_category() {
		return album_category;
	}

	public void setAlbum_category(String album_category) {
		this.album_category = album_category;
	}

	public Long getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(Long album_id) {
		this.album_id = album_id;
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

	public String getArtist_Logo() {
		return artist_Logo;
	}

	public void setArtist_Logo(String artist_Logo) {
		this.artist_Logo = artist_Logo;
	}

	public String getArtist_name() {
		return artist_name;
	}

	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public Long getCd_count() {
		return cd_count;
	}

	public void setCd_count(Long cd_count) {
		this.cd_count = cd_count;
	}

	public Boolean getCheckRate() {
		return checkRate;
	}

	public void setCheckRate(Boolean checkRate) {
		this.checkRate = checkRate;
	}

	public Long getCollects() {
		return collects;
	}

	public void setCollects(Long collects) {
		this.collects = collects;
	}

	public Long getComments() {
		return comments;
	}

	public void setComments(Long comments) {
		this.comments = comments;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getGmtPublish() {
		return gmtPublish;
	}

	public void setGmtPublish(Long gmtPublish) {
		this.gmtPublish = gmtPublish;
	}

	public Long getGrade() {
		return grade;
	}

	public void setGrade(Long grade) {
		this.grade = grade;
	}

	public Long getIs_check() {
		return is_check;
	}

	public void setIs_check(Long is_check) {
		this.is_check = is_check;
	}

	public Long getIs_play() {
		return is_play;
	}

	public void setIs_play(Long is_play) {
		this.is_play = is_play;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Long getPlayAuthority() {
		return playAuthority;
	}

	public void setPlayAuthority(Long playAuthority) {
		this.playAuthority = playAuthority;
	}

	public Long getPlayCount() {
		return playCount;
	}

	public void setPlayCount(Long playCount) {
		this.playCount = playCount;
	}

	public Long getRecommends() {
		return recommends;
	}

	public void setRecommends(Long recommends) {
		this.recommends = recommends;
	}

	public Long getSong_count() {
		return song_count;
	}

	public void setSong_count(Long song_count) {
		this.song_count = song_count;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getSub_title() {
		return sub_title;
	}

	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}

}
