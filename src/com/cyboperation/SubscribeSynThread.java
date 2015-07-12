package com.cyboperation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;

import org.cybergarage.http.HTTPPacket;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.sys.Global;
import com.irunsin.controller.util.HTMLDecoder;
import com.irunsin.controller.util.IrunSinApplication;
import com.irunsin.controller.util.XmlParseUtil;


/**
 * 订阅 接收gena报文的线程
 * @author nobita
 *
 */
public class SubscribeSynThread extends Thread{
	
	private static final String TAG = "SubscribeSynThread";
	
	public static final int DEFAULT_CHUNK_SIZE = 512 * 1024;
	
	
	private Context context;
	private Socket clientSocket;
	private IrunSinApplication irunsin;
	public SubscribeSynThread(Context context,Socket clientSocket){
		this.context = context;
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		
			InputStream in = null;
			OutputStream out = null;
			try {
				in = clientSocket.getInputStream();
				out = clientSocket.getOutputStream();
				//获取订阅返回报文
				HTTPPacket datapacket = new HTTPPacket(in);
				//获取报文结束后返回成功出去
				String responce = "HTTP:/1.1 200 OK";				
				out.write(responce.getBytes());
				//解析报文做相应处理
				final String contentString = datapacket.getContentString();				
				String sidString = datapacket.getHeaderValue("SID");
				String seqString = datapacket.getHeaderValue("SEQ");
				//订阅ID不一致直接退出
				String storageSubid = IrunSinApplication.AVT_SUB_SID; //播放服务的ID
				String recontrolSubid = IrunSinApplication.REC_SUB_SID;//控制服务的ID
				final String currentSubid = sidString.split(":")[1];
				
				if(!storageSubid.equals("")){
					if(!storageSubid.equals(currentSubid) && !recontrolSubid.equals(currentSubid)){
						//发送取消订阅出去
						Global.debug("当前的订阅号为 yyyyyyyyyyyyyyyyy" + sidString);						
						return;
					}	
				}else{
					return;
				}
				int begindex = contentString.indexOf("<LastChange>");
				int lastindex = contentString.indexOf("</LastChange>");
				if(begindex == -1 || lastindex == -1){
					Log.i(TAG, "报文异常");
					try{
					warnSendMessage(contentString, context);
					}catch(Exception e){
						Log.i(TAG, "解析异常报文时  又出现异常");
						//捕获异常不抛出
						return;
					}
					//获取AVTransportURI
					return;
				}
				irunsin = (IrunSinApplication)context.getApplicationContext();
				String xmlString;
				try{
//				xmlString = contentString.substring(begindex +12, lastindex);
				xmlString = contentString;
				
				}catch(Exception e){
					Log.i(TAG, "解析字符串异常");
		
					//当出现无法正常解析的报文时调用此方法解析得到对应的数据
					try{
						warnSendMessage(contentString, context);
					}catch(Exception e1){
						Log.i(TAG, "解析异常报文时  又出现异常");
						//捕获异常不抛出
						return;
					}
					return;
				}
				xmlString = xmlString.replaceAll("&lt;", "<");
				xmlString = xmlString.replaceAll("&gt;", ">");
				xmlString = xmlString.replaceAll( "&quot;","\"");
				Global.debug("订阅收到的报文如下：" + xmlString);
				//获取返回报文的数据
				Map<String, String> val = XmlParseUtil.getXmlVal(xmlString);
				if(null==val){
					return;
				}
				//解析值储存
				String transportState = "";
				String author = "";
				String mp3name = "";
				String avturi = "";
				String metadata = "";
				String mp3id = "";
				String display = "";
				String logo = "";
				String loOnflag = "";
				String filepath = "";
				int volum = -1;  // 声音变化后的值
				for (Entry<String, String> entry : val.entrySet()) {
					if(entry.getKey().equals("TransportState")){
						transportState = entry.getValue().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
					}else if(entry.getKey().equals("CurrentTrackURI")){
						avturi = entry.getValue().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
					}else if(entry.getKey().equals("Volume_RG_HW")){
						String value = entry.getValue();
						if(value.contains("_")){
							volum = Integer.parseInt(value.split("_")[1]);
						}else{
							volum = Integer.parseInt(value);
						}
						Log.i(TAG, "当前得到的音量大小为   = " +volum);
					}else if(entry.getKey().equals("AVTransportURIMetaData")){
						metadata = entry.getValue().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
					}
				}
				Log.i(TAG, "获取当前设备的占用歌曲为 = "+ avturi);
				
				if(volum != -1){
					//解析对应播放的音乐广播出去
					Intent intent = new Intent();
					intent.putExtra("volum", volum);
					intent.setAction("com.mp3.volum");
					context.sendBroadcast(intent);
				}
				//当medata不为空时   去解析
				if(!metadata.equals("")){
					if(metadata.startsWith("runtin")){
						String[] data = metadata.split("\\|");
						mp3name = data[1];
						author = data[2];	
						mp3id = data[3];
						display = data[4];
						filepath = data[5];
						logo = data[6];
						loOnflag = data[7];
						/**
						 * 生成对应的mp3info对象
						 */
						Mp3Info mp3info = new Mp3Info();
						mp3info.setArtist(author);
						mp3info.setDisplay_name(display);
						mp3info.setFilepath(filepath);
						mp3info.setMp3id(mp3id);
						mp3info.setLogo(logo);
						mp3info.setLoOnFlag(Integer.parseInt(loOnflag));
						mp3info.setMp3Name(mp3name);
						//赋值同步出去的歌曲名称   用于后续判断是否本机播放切歌
						irunsin.setPlay_mp3Id(mp3id);
						//解析对应播放的音乐广播出去		
						Intent intent = new Intent();
						intent.putExtra("mp3info", mp3info);
						intent.setAction("com.mp3.currdata");						
						context.sendBroadcast(intent);
						
						Intent intent1 = new Intent();
						intent1.putExtra("mp3name", mp3name);
						intent1.putExtra("author", author);
						intent1.setAction("com.mp3.notify"); //通知栏更新广播
						context.sendBroadcast(intent1);						
					}					
				}
				
				//当收到状态为stoped时 且此时状态为6  正在切歌   则代表 此时的STOPED 过滤
				if(transportState.equals("STOPPED")){
					String CurrentTransportActions = "";
					for (Entry<String, String> entry : val.entrySet()) {
						if(entry.getKey().equals("CurrentTransportActions")){
							CurrentTransportActions = entry.getValue().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
						}
					}
					if(!CurrentTransportActions.equals("")){
						Log.i(TAG, "此为切歌时的stoped 过滤掉");
						return;
					}
				}
				
				if(transportState.equals("PLAYING")){
					//当状态为PLAYING则开启进度条   发送进度条广播
					Intent intent1 = new Intent();
					intent1.setAction("com.mp3.begin");
					context.sendBroadcast(intent1);					
					irunsin.setPLAY_STATE(0);					
				}else if(transportState.equals("STOPPED") && irunsin.getPLAY_STATE() ==0 && !seqString.equals("1")){
					//此处还需要控制是否当前为本机占用   临时解决方案为  判断当前音乐与正在播放的音乐的一致性
					Log.i(TAG, "当前播放的音乐为 = "+irunsin.getMp3info().getMp3Name() + "当前获取的音乐为 = "+ mp3name);
					if(irunsin.getMp3info().getMp3id().equals(irunsin.getPlay_mp3Id())){
						Log.i(TAG, "切换到下一首");
						//切换下一首
						Intent intent = new Intent();
						intent.putExtra("FLAG", 3);
//						intent.setClass(context, MusicPlayService.class);
						context.startService(intent);							
					}else{
						Log.i(TAG, "设备被 占用");
						//赋值被占用
						irunsin.setPLAY_STATE(4);
						irunsin.setSOFT_STATE(false);
					}
				}else if(transportState.equals("PAUSED_PLAYBACK")){
					//代表设备暂停
					Intent intent = new Intent();
					intent.setAction("com.mp3.pause");
					context.sendBroadcast(intent);
					irunsin.setPLAY_STATE(1);
				}
			}catch (Exception e1) {
				Global.debug("订阅收取报文异常");
				e1.printStackTrace();
			}finally{
				try {
					clientSocket.close();
					in.close();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}											
	}
	

	/**
	 * 出现异常报文时处理方法
	 */
	public boolean warnSendMessage(String contentString,Context context){
		Log.i(TAG, "进入异常报文解析处理方法");
		String xmls = contentString.replaceAll("&lt;", "<");
		xmls = xmls.replaceAll("&gt;", ">");
		//因偶尔出现报文异常的情况    故获取对应的歌曲信息的位置信息
		int b = xmls.indexOf("<AVTransportURIMetaData");
		int e = xmls.indexOf("</AVTransportURIMetaData>");
		if(b!=-1 && e!=-1){
			String url = xmls.substring(b+29, e-2);
			Log.i(TAG, "报文异常获取到的报文如下 = " +url);
			if(url.equals("")){
				Log.i(TAG, "异常报文中当前播放的音乐为空");
				return false;
			}else{
				String avturi = HTMLDecoder.decode(url);
				avturi = avturi.replaceAll("&lt;", "<");
				avturi = avturi.replaceAll("&gt;", ">");
				Log.i(TAG, "异常后最终截取去解析的url为  = " +avturi);
				String[] data;
				try{
					 data= avturi.split("\\|");
				}catch(Exception e1){
					Log.i(TAG, "报文异常时去 获取对应信息错误");
					return false;
				}
				Log.i(TAG, "分割成功   待取值传递");
				if(data.length<3){
					Log.i(TAG, "非我族类 其心必异");
					return false;
				}
				String mp3name = data[1];
				String author = data[2];
				String mp3id = data[3];
				String display = data[4];
				String logo = data[5];
				String loOnflag = data[6];
				String filepath = data[7];
				Log.i(TAG, "已分析得到的 各个值   mp3name = "+mp3name  +  "   author = " + author);
				if(irunsin == null){
					irunsin = (IrunSinApplication) context.getApplicationContext();
				}
				/**
				 * 生成对应的mp3info对象
				 */
				Mp3Info mp3info = new Mp3Info();
				mp3info.setArtist(author);
				mp3info.setDisplay_name(display);
				mp3info.setFilepath(filepath);
				mp3info.setMp3id(mp3id);
				mp3info.setLogo(logo);
				mp3info.setLoOnFlag(Integer.parseInt(loOnflag));
				mp3info.setMp3Name(mp3name);
				//赋值同步出去的歌曲名称   用于后续判断是否本机播放切歌
				irunsin.setPlay_mp3Id(mp3id);
				//解析对应播放的音乐广播出去
				Intent intentyc = new Intent();
				mp3info.setMp3Name("");
				intentyc.putExtra("mp3info", mp3info);				
				intentyc.setAction("com.mp3.currdata");
				context.sendBroadcast(intentyc);
				Log.i(TAG, "异常报文后解析  截取报文成功");
			}							
		}
		
		int f = xmls.indexOf("<TransportState");
		int g = xmls.indexOf("</TransportState>");
		if(f!=-1 && g !=-1){
			String state = xmls.substring(f+21, g-2);
			if(state.equals("")){
				return false;
			}else{
				if(state.equals("PLAYING")){
					//当状态为PLAYING则开启进度条   发送进度条广播
					Intent intent1 = new Intent();
					intent1.setAction("com.mp3.begin");
					context.sendBroadcast(intent1);

					
					irunsin.setPLAY_STATE(0);
				}
			}
		}
		return true;
	}
	
	

}
