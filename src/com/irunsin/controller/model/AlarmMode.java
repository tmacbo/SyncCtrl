package com.irunsin.controller.model;

import java.io.Serializable;
import java.util.List;

/***
 * 闹钟的mode类
 * @author nobita
 *
 */
public class AlarmMode implements Serializable{

	private String weeks;
	private String startTime;
	private List<Mp3Info> list;
	private int alarmtype;  //闹钟类型   0开始播放    1关闭播放
	private String deviceName;
	private String alarmId;
	
	
	
	public String getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	public String getWeeks() {
		return weeks;
	}
	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public List<Mp3Info> getList() {
		return list;
	}
	public void setList(List<Mp3Info> list) {
		this.list = list;
	}
	public int getAlarmtype() {
		return alarmtype;
	}
	public void setAlarmtype(int alarmtype) {
		this.alarmtype = alarmtype;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
			
}
