package com.irunsin.controller.sys;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class StreamsUtils
{
	public static void saveByteArrayToFile(byte[] datas, File file) throws Exception {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(datas);
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	public static byte[] readByteArrayFromFile(File file) throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = fileInputStream.read(buffer)) > 0) {
			byteArrayOutputStream.write(buffer, 0, length);
		}
		byteArrayOutputStream.flush();
		fileInputStream.close();

		byte[] result = byteArrayOutputStream.toByteArray();

		byteArrayOutputStream.close();

		return result;
	}

	public static String getStringNotNull(String str) {
		if (str == null) {
			return "";
		}
		if (str.toLowerCase().equals("null")) {
			return "";
		}
		return str;
	}

	public static String replaceIfNull(String str, String other) {
		if (isStrBlank(str)) {
			return other;
		}
		else {
			return str;
		}
	}

	public static void writeButeArrayToFile(byte[] bytes, File file) throws Exception {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(bytes);
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	public static byte[] readByteArrayFromStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = inputStream.read(buffer)) > 0) {
			byteArrayOutputStream.write(buffer, 0, length);
		}
		byteArrayOutputStream.flush();
		inputStream.close();

		byte[] result = byteArrayOutputStream.toByteArray();

		byteArrayOutputStream.close();

		return result;
	}

	public static boolean isStrNotBlank(String str) {
		return (str != null) && (!str.equals(""));
	}

	public static boolean isStrBlank(String str) {
		return !isStrNotBlank(str);
	}

	public static String convertLongArrayToStr(ArrayList<Long> longs) {
		if (longs == null || longs.size() <= 0) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < longs.size(); i++) {
			buffer.append(longs.get(i));
			if (i != longs.size()) {
				buffer.append("!");
			}
		}
		return buffer.toString();
	}

	public static String createBase64DataFromFile(String path) {
		if (StreamsUtils.isStrBlank(path)) {
			return null;
		}

		return createBase64DataFromFile(new File(path));
	}

	public static String createBase64DataFromFile(File file) {
		if (file == null || (!file.exists())) {
			return null;
		}
		try {
			byte[] bytes = StreamsUtils.readByteArrayFromFile(file);
			if (bytes != null && bytes.length != 0) {
				return Base64.encode(bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
