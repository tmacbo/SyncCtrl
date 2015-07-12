package com.alibaba.model;

import java.io.Serializable;


public class SearchSongsData implements Serializable{

	  private SearchSongsDataSongs songs;

	  public SearchSongsDataSongs getSongs()
	  {
	    return this.songs; }

	  public void setSongs(SearchSongsDataSongs songs) {
	    this.songs = songs;
	  }
}
