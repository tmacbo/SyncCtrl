package com.irunsin.controller.sys;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5
{
	public Md5() {}

	/*
	 * 检验你的实现是否正确： MD5 ("") = d41d8cd98f00b204e9800998ecf8427e MD5 ("a") =
	 * 0cc175b9c0f1b6a831c399e269772661 MD5 ("abc") =
	 * 900150983cd24fb0d6963f7d28e17f72 MD5 ("message digest") =
	 * f96b697d7cb7938d525a2f31aaf161d0 MD5 ("abcdefghijklmnopqrstuvwxyz") =
	 * c3fcd3d76192e4007dfb496cca67e13b
	 */

	public void md5s(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
			System.out.println("result: " + buf.toString());// 32位的加密
			System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public String str;

	public final static String MD5(byte[] strTemp) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			// byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
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
	

	public static void main(String[] args) throws Exception {
		System.out.println(MD5(readByteArrayFromFile(new File("C:\\Users\\liukaifu\\Desktop\\libammsdk.jar"))));
		System.out.println(MD5(readByteArrayFromFile(new File("D:\\myWorkspace\\j2me-workspace\\2011-4-7\\weibo_2\\libs\\libammsdk.jar"))));
	}
}
