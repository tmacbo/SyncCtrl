package com.alibaba.model;

import java.io.Serializable;

public class Rank implements Serializable {

	private String cont;

	private String logo;

	private String logo_middle;

	private Long object_id;

	private String share_type;

	private String title;

	private String type;

	private String update_date;

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLogo_middle() {
		return logo_middle;
	}

	public void setLogo_middle(String logo_middle) {
		this.logo_middle = logo_middle;
	}

	public Long getObject_id() {
		return object_id;
	}

	public void setObject_id(Long object_id) {
		this.object_id = object_id;
	}

	public String getShare_type() {
		return share_type;
	}

	public void setShare_type(String share_type) {
		this.share_type = share_type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

}
