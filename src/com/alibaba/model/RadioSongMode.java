package com.alibaba.model;

import java.io.Serializable;
import java.util.List;

public class RadioSongMode implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3813512542269206575L;
	private List<StandardSong> data;

	public List<StandardSong> getData() {
		return data;
	}

	public void setData(List<StandardSong> data) {
		this.data = data;
	}
	
	
}
