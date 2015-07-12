package com.alibaba.model;

import java.io.Serializable;
import java.util.List;

public class RankList implements Serializable {

	private String category;

	private List<Rank> items;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Rank> getItems() {
		return items;
	}

	public void setItems(List<Rank> items) {
		this.items = items;
	}

}
