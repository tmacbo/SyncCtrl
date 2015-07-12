package com.cyboperation.inter;

import org.cybergarage.upnp.Device;
/**
 * 此接口封装的方法为设备的控制 包括  播放  暂停 快进等
 * @author Administrator
 *
 */ 
public interface IController {

	/**
	 * Play the video with the video path.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @param path
	 *            The path of the video.
	 * @return If is success to play the video.
	 */
	boolean play(Device device, String path,String metaData);

	
//	boolean playnext(Device device, String path);
	/**
	 * Go on playing the video from the position.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @param pausePosition
	 *            The format must be 00:00:00.
	 */
	boolean goon(String pausePosition);

	/**
	 * All the state is "STOPPED" // "PLAYING" // "TRANSITIONING"//
	 * "PAUSED_PLAYBACK"// "PAUSED_RECORDING"// "RECORDING" //
	 * "NO_MEDIA_PRESENT//
	 */
	String getTransportState();

	/**
	 * Get the min volume value,this must be 0.
	 * 
	 * @param device
	 * @return
	 */
	int getMinVolumeValue();

	/**
	 * Get the max volume value, usually it is 100.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @return The max volume value.
	 */
	int getMaxVolumeValue();

	/**
	 * Seek the playing video to a target position.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @param targetPosition
	 *            Target position we want to set.
	 * @return
	 */
	boolean seek(String targetPosition);

	/**
	 * Get the current playing position of the video.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @return Current playing position is 00:00:00
	 */
	String getPositionInfo();

	
	void getArgumentList();
	
	/**
	 * Get the duration of the video playing.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @return The media duration like 00:00:00,if get failed it will return
	 *         null.
	 */
	String getMediaDuration();

	/**
	 * Mute the device or not.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @param targetValue
	 *            1 is that want mute, 0 if you want make it off of mute.
	 * @return If is success to mute the device.
	 */
	boolean setMute(Device device, String targetValue);

	/**
	 * Get if the device is mute.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @return 1 is mute, otherwise will return 0.
	 */
	String getMute(Device device);

	/**
	 * Set the device's voice.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @param value
	 *            Target voice want be set.
	 * @return
	 */
	boolean setVoice(int value);

	/**
	 * Get the current voice of the device.
	 * 
	 * @param device
	 *            The device be controlled.
	 * @return Current voice.
	 */
	int getVoice();

	/**
	 * Stop to play.
	 * 
	 * @param device
	 *            The device to controlled.
	 * @return If if success to stop the video.
	 */
	boolean stop(Device device);

	/**
	 * Pause the playing video.
	 * 
	 * @param device
	 *            The device to controlled.
	 * @return If if success to pause the video.
	 */
	boolean pause();
	
	/**
	 * 订阅
	 */
	void eventSub();
	void cancelSub();
	/**
	 * 扩展测试功能
	 */
	void getinfo();
	boolean setlist(String path);
}
