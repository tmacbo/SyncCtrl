package com.irunsin.controller.sys;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 可能会新增某些公共的操作方法，所以将其定义为抽象类
 * 
 */
public abstract class RequestBean {


	public String version;
	public String plat;

	public abstract String getRequestKey();

	public abstract String getRequestStr();

	public abstract TextMsgParser getMessageParser();

	private HashMap<String, String> reqDatas = new HashMap<String, String>();
	private ArrayList<String> reqKeys = new ArrayList<String>();

	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	public void put(String key, String val) {
		reqDatas.put(key, val);
		reqKeys.add(key);
	}

	public String getReqStrFromReqDatas() {
		StringBuffer buffer = new StringBuffer();

		put("version", StreamsUtils.replaceIfNull(version, ""));
		put("plat", StreamsUtils.replaceIfNull(plat, ""));

		buffer.append("<req>");
		for (String key : reqKeys) {
			buffer.append("<" + key + ">");
			buffer.append(reqDatas.get(key));
			buffer.append("</" + key + ">");
		}
		buffer.append("</req>");

		return buffer.toString();
	}

	public String getReqFromFields() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<req>");
		Method[] ms = getClass().getDeclaredMethods();
		for (int i = 0; i < ms.length; i++) {
			Method method = ms[i];
			String methodName = method.getName();
			if (methodName.startsWith("get")
					&& !(methodName.equals("getRequestKey")
							|| methodName.equals("getRequestStr")
							|| methodName.equals("getMessageParser") || methodName
								.equals("getReqFromFields"))) {

				if (method.getParameterTypes() == null
						|| method.getParameterTypes().length == 0) {
					try {
						String key = Character
								.toLowerCase(methodName.charAt(3))
								+ methodName.substring(4);
						Object val = method.invoke(this);
						if (val != null) {
							buffer.append("<" + key + ">");
							buffer.append(val);
							buffer.append("</" + key + ">");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		buffer.append("</req>");

		return buffer.toString();
	}

}
