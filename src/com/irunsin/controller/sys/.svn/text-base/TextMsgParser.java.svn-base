package com.irunsin.controller.sys;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public abstract class TextMsgParser
{

	public static <T> T parser(Class<T> s, String resp) {
		XmlPullParser parser;
		try {

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			parser = factory.newPullParser();
			if (resp != null) {
				parser.setInput(new StringReader(resp));

				Object data = s.newInstance();
				parser.nextTag();
				TextMsgParser.parser(data, parser, "res");

				return s.cast(data);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getTypeStr(int type) {
		switch (type) {
		case XmlPullParser.START_DOCUMENT:

			return "XmlPullParser.START_DOCUMENT";

		case XmlPullParser.CDSECT:

			return "XmlPullParser.CDSECT";

		case XmlPullParser.COMMENT:

			return "XmlPullParser.COMMENT";

		case XmlPullParser.DOCDECL:

			return "XmlPullParser.DOCDECL";

		case XmlPullParser.END_DOCUMENT:

			return "XmlPullParser.END_DOCUMENT";

		case XmlPullParser.END_TAG:

			return "XmlPullParser.END_TAG";

		case XmlPullParser.IGNORABLE_WHITESPACE:

			return "XmlPullParser.IGNORABLE_WHITESPACE";

		case XmlPullParser.ENTITY_REF:

			return "XmlPullParser.ENTITY_REF";

		case XmlPullParser.TEXT:

			return "XmlPullParser.TEXT";

		case XmlPullParser.START_TAG:

			return "XmlPullParser.START_TAG";

		case XmlPullParser.PROCESSING_INSTRUCTION:

			return "XmlPullParser.PROCESSING_INSTRUCTION";

		default:
			return null;
		}
	}

	public static void parser(Object object, XmlPullParser p, String name) {
		try {
			int type = 0;
			p.require(XmlPullParser.START_TAG, null, name);

			while (true) {
				type = p.next();
				String n = p.getName();
				if (type == XmlPullParser.END_TAG) {
					if (n != null && n.equals(name)) {
						break;
					}
				}
				else if (type == XmlPullParser.START_TAG) {
					setValue(object, n, p);
				}
			}

			p.require(XmlPullParser.END_TAG, null, name);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 通过属性得到对应的Set方法
	 * 
	 * @param name
	 * @return
	 */
	public static String setMethodName(String name) {
		if (StreamsUtils.isStrBlank(name)) {
			return null;
		}

		if (name.equals("resultCode")) {
			return "setRespCode";
		}
		else if (name.equals("resultDesc")) {
			return "setRespMsg";
		}
		else {
			char first = name.charAt(0);
			first = Character.toUpperCase(first);
			return "set" + first + name.substring(1);
		}
	}

	/**
	 * 通过属性得到对应的Get方法
	 * 
	 * @param name
	 * @return
	 */
	public static String getMethodName(String name) {
		if (StreamsUtils.isStrBlank(name)) {
			return null;
		}

		if (name.equals("resultCode")) {
			return "getRespCode";
		}
		else if (name.equals("resultDesc")) {
			return "getRespMsg";
		}
		else {
			char first = name.charAt(0);
			first = Character.toUpperCase(first);
			return "get" + first + name.substring(1);
		}
	}

	public static void setValue(Object obj, String name, XmlPullParser p) {
		String setMethodName = setMethodName(name);
		String getMethodName = getMethodName(name);

		String val;

		Class c = obj.getClass();

		if (setMethodName == null) {
			return;
		}
		try {

			Method getMethod = c.getMethod(getMethodName);
			Class returnType = getMethod.getReturnType();
			Method setMethod = c.getMethod(setMethodName, returnType);

			Object valObj = null;

			if (returnType == int.class || returnType == Integer.class) {
				val = p.nextText();
				valObj = Integer.parseInt(val);

				setMethod.invoke(obj, valObj);
			}
			else if (returnType == Double.class) {
				val = p.nextText();
				valObj = Double.parseDouble(val);

				setMethod.invoke(obj, valObj);
			}
			else if (returnType == Boolean.class) {
				val = p.nextText();
				valObj = Boolean.parseBoolean(val);

				setMethod.invoke(obj, valObj);
			}
			else if (returnType == String.class) {
				val = p.nextText();
				valObj = val;

				setMethod.invoke(obj, valObj);

			}
			else if (returnType == ArrayList.class) {

				ArrayList arrayList = (ArrayList) getMethod.invoke(obj, null);
				if (arrayList == null) {
					arrayList = new ArrayList();
					setMethod.invoke(obj, arrayList);
				}

				Field f = c.getDeclaredField(name);

				Type fc = f.getGenericType();

				if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
				{
					ParameterizedType pt = (ParameterizedType) fc;
					Class genericClazz = (Class) pt.getActualTypeArguments()[0]; // 【4】

					Object objectInList = genericClazz.newInstance();

					arrayList.add(objectInList);

					parser(objectInList, p, name);
				}

			}
			else {
				valObj = returnType.newInstance();
				parser(valObj, p, name);

				setMethod.invoke(obj, valObj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public XmlPullParser getXmlPullParser(String text) {
		XmlPullParser xmlPullParser = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			xmlPullParser = factory.newPullParser();
			if (text != null) {
				xmlPullParser.setInput(new StringReader(text));
			}
		} catch (Exception e) {
			Global.error("TextMsgParser.getXmlPullParser() error", e);
			e.printStackTrace();
		}
		return xmlPullParser;
	}

	public abstract ResponseBean parser(String resp);

}
