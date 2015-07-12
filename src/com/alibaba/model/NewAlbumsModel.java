package com.alibaba.model;

import java.io.Serializable;
import java.util.List;

public class NewAlbumsModel implements Serializable {

	private List<StandardAlbum> albums;

	public List<StandardAlbum> getAlbums() {
		return albums;
	}

	public void setAlbums(List<StandardAlbum> albums) {
		this.albums = albums;
	}

}
