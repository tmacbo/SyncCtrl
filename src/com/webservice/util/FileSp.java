package com.webservice.util;

import java.io.File;

public class FileSp {

	//�жϴ��ļ��Ƿ����
	public static boolean isExist(String filepath){
		boolean flag = false;
		File file = new File(filepath);		
		if(file.exists()){
			flag = true;
		}else{
			flag = false;
		}
		return flag;
	}
	
	public static byte[] read(String filepath){
		byte[] data = new byte[50];
		return data;
	}
}
