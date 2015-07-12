package com.cyboperation.engine;

import java.util.List;

import org.cybergarage.upnp.Device;

import android.content.Context;

import com.cyboperation.inter.IController;
import com.cyboperation.util.DLNAUtil;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.util.IrunSinApplication;

/**
 * 此类  封装出去 外围直接调用的方法
 * @author Administrator
 *
 */
public class DlnaPackageImp{

	
	
	
	
	//获取得到Dolary的设备列表
	public List<Device> getListDevice(){
		return DLNAContainer.getInstance().getDevices();
	}
	
	//本地推送歌曲方法   LoOnFlag  本地或者在线的标志  0为本地  1为在线
	public static boolean pushMp3(Mp3Info mp3info,Device mdevice,int LoOnFlag,Context context){
		String mp3path = mp3info.getFilepath();
		System.out.println("获取到的路径为 = " +mp3path);
		boolean playresult = false;
		String sendpath = null;
		
		if(LoOnFlag == 0 ){
			//获取路径
			sendpath = DLNAUtil.getMp3FilePath(mp3info,context);
			
		}else if(LoOnFlag == 1){
			sendpath = mp3path;
		}else{
			
		}

		mdevice = ((IrunSinApplication)context.getApplicationContext()).getDevice();
		if(mdevice == null){
			return false;
		}
		String path = sendpath;
		
		//拼接一个存放到CurrentURIMetaData 中的数据  用于解析获取到当前播放的歌曲的名称以及姓名   拼接规则为runtin|mp3name|author
		String metaData = "runtin|"+mp3info.getMp3Name()+"|"+mp3info.getArtist()+"|"+mp3info.getMp3id()+"|"+ mp3info.getDisplay_name()+ "|" + mp3info.getFilepath() + "|" +mp3info.getLogo() + "|" +mp3info.getLoOnFlag();
				
		//实例化  播放工具类对象
		IController control = new MultiPointController(context);
		playresult = control.play(mdevice, path,metaData);	
		
		return playresult;
	}	
	


}
