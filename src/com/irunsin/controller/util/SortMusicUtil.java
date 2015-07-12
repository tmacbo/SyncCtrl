package com.irunsin.controller.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.irunsin.controller.model.Mp3Info;


public class SortMusicUtil {
	
	public static List<Mp3Info> sortmusic(List<Mp3Info> list){

		GB2Alpha gb = new GB2Alpha();
		List<Mp3Info> showlist = new ArrayList<Mp3Info>();
		String[] word = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","S","Y","Z"};

		for (int i = 0; i < word.length; i++) {
			int size = list.size();
			int flag = 0;
			int mark = 0;
			while(flag < size){
				String s1 = gb.String2Alpha(list.get(mark).getDisplay_name());
				if(s1.startsWith(word[i])){
					showlist.add(list.get(mark));
					list.remove(mark);					
				}else{
					mark ++;
				}
				flag ++;
			}			
		}
		
		
		return showlist;		
	}
	
	public static List<Mp3Info> sortmusicTwo(List<Mp3Info> list){
		String[] word = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","S","Y","Z"};

		return null;
	}
	//��ú���ƴ������ĸ
    public static String getAlpha(String str) {  
        if (str == null) {  
            return "#";  
        }  
  
        if (str.trim().length() == 0) {  
            return "#";  
        }  
  
        char c = str.trim().substring(0, 1).charAt(0);  
        // ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ  
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");  
        if (pattern.matcher(c + "").matches()) {  
            return (c + "").toUpperCase();  
        } else {  
            return "#";  
        }  
    }  
}
