package com.alibaba.model;

import java.io.Serializable;
import java.util.List;

public class ArtistAlbumsResult implements Serializable{

	 private List<StandardAlbum> albums;

	  private Long next;

	  private Long previous;

	  private Long total_number;

	public List<StandardAlbum> getAlbums() {
		return albums;
	}

	public void setAlbums(List<StandardAlbum> albums) {
		this.albums = albums;
	}

	public Long getNext() {
		return next;
	}

	public void setNext(Long next) {
		this.next = next;
	}

	public Long getPrevious() {
		return previous;
	}

	public void setPrevious(Long previous) {
		this.previous = previous;
	}

	public Long getTotal_number() {
		return total_number;
	}

	public void setTotal_number(Long total_number) {
		this.total_number = total_number;
	}
	  
	  
}
