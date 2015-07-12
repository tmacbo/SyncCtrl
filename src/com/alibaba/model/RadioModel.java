package com.alibaba.model;

import java.io.Serializable;
import java.util.List;


public class RadioModel implements Serializable{


	private static final long serialVersionUID = 8045977675742378289L;
	
	
	private List<RadioTypeInfo> data;
	
	
	public List<RadioTypeInfo> getData() {
		return data;
	}


	public void setData(List<RadioTypeInfo> data) {
		this.data = data;
	}


}
