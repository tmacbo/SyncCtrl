package com.irunsin.controller.model;

import java.io.Serializable;

public class FirVersionModel implements Serializable{

	private String name;
	private String version;
	private String changelog;
	private String versionShort;
	private String update_url;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getChangelog() {
		return changelog;
	}
	public void setChangelog(String changelog) {
		this.changelog = changelog;
	}
	public String getVersionShort() {
		return versionShort;
	}
	public void setVersionShort(String versionShort) {
		this.versionShort = versionShort;
	}
	public String getUpdate_url() {
		return update_url;
	}
	public void setUpdate_url(String update_url) {
		this.update_url = update_url;
	}
	
}
