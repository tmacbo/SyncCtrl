package com.irunsin.controller.model;

import java.io.Serializable;


/**
 * 用户选择记住的 设备
 * @author nobita
 *
 */
public class RememberDevices implements Serializable{
	
	private int id;
	private String ssdpString;
	private String uuid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSsdpString() {
		return ssdpString;
	}
	public void setSsdpString(String ssdpString) {
		this.ssdpString = ssdpString;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
		
}
