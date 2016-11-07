package com.hexincad.filters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hexincad.utils.PathUtils;

public class IPStore {
	private static IPStore store=null;
	private HashMap<String,String> ipMap=new HashMap<String,String>();
	
	private IPStore(HttpServletRequest request){
		init( request);
	}
	
	public static IPStore getInstance(HttpServletRequest request){
		if(store==null){
			store=new IPStore(request);
		}
		return store;
	}

	public HashMap<String,String> getIpMap() {
		return ipMap;
	}

	public void setIpMap(HashMap<String,String> ipMap) {
		this.ipMap = ipMap;
	}
	
	private void init(HttpServletRequest request){
		 String path= PathUtils.getResourcesPath(request, "/resources/config/ips.txt");
		 try {
			List<String> list=Files.readAllLines(Paths.get(path));
			for(String ip:list){
				ipMap.put(ip, ip);
			}
		 } catch (IOException e) {
			e.printStackTrace();
		}
	}
}
