package com.alibaba.model;

import java.io.Serializable;

public class RecommendModel implements Serializable {

	private static final long serialVersionUID = 2943383629423650122L;
	private CollectList data;

	public CollectList getData() {
		return data;
	}

	public void setData(CollectList data) {
		this.data = data;
	}

}
