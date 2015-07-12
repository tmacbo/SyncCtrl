package com.alibaba.model;

import java.io.Serializable;
import java.util.List;

public class DailySongModel implements Serializable {

	private String logo;

	private List<RecommendSong> songs;

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<RecommendSong> getSongs() {
		return songs;
	}

	public void setSongs(List<RecommendSong> songs) {
		this.songs = songs;
	}

}
