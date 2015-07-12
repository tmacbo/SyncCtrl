package com.irunsin.controller.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntegerUtil {

	/*
	 * 此方法为获取长度内的四个随机整数
	 * length 为总长度
	 */
	public static List<Integer> getrandom(int length){
		List<Integer> list = new ArrayList<Integer>();
		Random ra =new Random();		
		while(list.size()<4){
			boolean flag = true;
			int i = ra.nextInt(length);
			for (int j = 0; j < list.size(); j++) {
				if(list.get(j) == i){
					flag = false;
					break;
				}
			}
			if(flag){
				list.add(i);
			}			
		}		
		return list;		
	}
}
