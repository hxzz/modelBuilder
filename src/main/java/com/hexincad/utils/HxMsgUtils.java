package com.hexincad.utils;

import java.util.List;

import com.hexincad.modelPost.HxMsg;
import com.hexincad.modelPost.ModelParams;
import com.hexincad.modelPost.Param;

public class HxMsgUtils {
	public static String createSendString(HxMsg msg){
		if(msg==null){
			return null;
		}
		
		StringBuffer sb=new StringBuffer();
		sb.append(msg.getCmd()).append(" ")    //cmd
		  .append(msg.getJobID()).append(" ");  //jobID
		
		ModelParams params=msg.getParams();
		sb.append(params.getId()).append(" ") ;  //id
		ModelParams mp=msg.getParams();   
		List<Param> plist=mp.getParams();
		int count =4;
		for(Param p:plist){
		   String[] values=	p.values;
		   for(String v:values){
			   sb.append(" ").append(v);   //params
			   count ++;
		   }
		}
		sb.append(" ").append(msg.getPath());
		count++;
		return count+" "+sb.toString();
	}
	
	public static HxMsg createMsgFromString(String msg){
		if(msg==null){
			return null;
		}
		
		String [] str=msg.trim().split(" ");
		String cmd=str[1];
		HxMsg hm=new HxMsg();
		hm.setCmd(cmd);
		if(cmd.equals("generated_success")){
			hm.setJobID(str[2]);
			hm.setPath(str[3]);
		}else if(cmd.equals("generated_failed")){
			hm.setDetails(str[2]);
		}
		return hm;
		
	}

	


}
