package com.irunsin.controller.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.irunsin.controller.sys.Global;

public class XmlParseUtil {

	private static final String TAG = "XmlParseUtil";
	public static Map<String, String> getXmlVal(String xmlString){
		//xmlͷ
		try {
			xmlString = new String(xmlString.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		XmlPullParser parser = getXmlPullParser(xmlString);
		int eventType;
		Map<String, String> map = new HashMap<String, String>();
		try {
			eventType = parser.getEventType();
			List<String> list = new ArrayList<String>();
			while(eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_DOCUMENT){
					Log.i(TAG, "开始解析");
				}else if(eventType == XmlPullParser.START_TAG){
					String name = parser.getName(); 
					if(!name.equals("Event") && !name.equals("e:propertyset") && !name.equals("e:property") && !name.equals("LastChange")){								
						String value = parser.getAttributeValue(0);
						map.put(name, value);
					}
					list.add(name);
				}else if (eventType == XmlPullParser.END_TAG) {
//	                Log.i(TAG,"End tag " + parser.getName());
	            } else if (eventType == XmlPullParser.TEXT) {
//	                Log.i(TAG,"Text " + parser.getText());
	            }
				parser.next();
				eventType = parser.getEventType();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
		return map;
	}
	
	
	public static XmlPullParser getXmlPullParser(String text) {
		XmlPullParser xmlPullParser = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			xmlPullParser = factory.newPullParser();
			if (text != null) {				
				xmlPullParser.setInput(new StringReader(text));
				
				
			}
		} catch (Exception e) {
			Global.error("getXmlPullParser() error", e);
			e.printStackTrace();
		}
		return xmlPullParser;
	}
	
	
		
	
}
