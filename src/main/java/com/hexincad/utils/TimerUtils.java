package com.hexincad.utils;

public class TimerUtils {
	public static double toSeconds(String time) throws Exception{
		
		if(time==null)return -1;
		time=time.trim();
		if(time.length()==0)return -1;
		String reg="[0-9][0-9]:[0-9][0-9]";
		if(!time.matches(reg)){
			throw new Exception("时间格式不对！");
		}
		
		String[]  t=time.split(":");
		String h=t[0].trim().replace("0", "");
		String m=t[1].trim().replace("0", "");
		if(h.length()==0)h="0";
		if(m.length()==0)m="0";
		return  Integer.valueOf(h)*3600+Integer.valueOf(m)*60;
		 
	}
	
	public static String toTimeFormat(double time) throws Exception{
		if(time<0)throw new Exception("时间不能为负数！");
		int h=(int)(time/3600);
		int m=(int)(time%3600/60);
		String hh="00",mm="00";
		if(h>0&&h<10){
			hh="0"+h;
		}else if(h>9){
			hh=h+"";
		}
		if(m>0&&m<10){
			mm="0"+m;
		}else if(m>9){
			mm=m+"";
		}
		return hh+":"+mm;
		
	}
}
