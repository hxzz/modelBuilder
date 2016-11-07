package com.hexincad.modelPost;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import com.hexincad.utils.HttpConnectUtil;
import com.hexincad.utils.HxMsgUtils;
import com.hexincad.utils.JacksonUtil;
import com.hexincad.utils.SocketUtil;

public class SocketPost {
	
	private  static  SocketPost sp;
	private  static LinkedBlockingDeque<SendInfo> msgQueue=new LinkedBlockingDeque<>();
	private  ExecutorService fixedThreadPoolRecieve = null;  
	private  ExecutorService fixedThreadPoolSend = null;  
	private  int sendPort,serverPort;
	private  int maxSendCount=3;
	private  ServerSocket server=null;
	private String portsFilePath=null;
	private  boolean started=false; 
	
	private  SocketPost(){
	}
	
	
	public    static SocketPost getInstance( ){
		if(sp==null){
			sp=new SocketPost();
		}
		return sp;
	}
	
	public void init(){
		 Properties prop = new Properties();   
	        try {   
	        	File file=new File(portsFilePath);
	        	if(!file.exists()){
	        		return;
	        	}
	        	InputStream in =new FileInputStream(file);   
	            prop.load(in);   
	            serverPort = Integer.valueOf(prop.getProperty("server.port").trim());   
	            sendPort = Integer.valueOf(prop.getProperty("HxCAD.port").trim());  
	            try {
	    			server=new ServerSocket(serverPort);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    			sp=null;
	    			server=null;
	    			return;
	    		}
	        } catch (IOException e) {   
	            e.printStackTrace();   
	        }   

	}
	
	public  void start(){
		System.out.println("-------------------------------------------------------");
		if(started)return;
		recieve();
		send();
	}
	
	private void send(){
		new Thread(){
			public void run(){
				while(true){			
					SendInfo info=null;
					try {
						info=msgQueue.take();
						send(info);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
				}
			}
		}.start();
	}
	private void  send(SendInfo info){
		if(fixedThreadPoolSend==null){
			fixedThreadPoolSend=Executors.newFixedThreadPool(10);  
		}
		Runnable target=null;
	
		target= new Runnable(){
				    public void run(){
						try {
							Socket s=new Socket("localhost",sendPort);
							
							boolean rs=SocketUtil.sendMsg(s, HxMsgUtils.createSendString(info.getMsg()));
							System.out.println(this+"发送消息：  "+HxMsgUtils.createSendString(info.getMsg())+"\n********************************");
							int count=info.getSendCount();
							info.setSendCount(count+1);
							if(!rs&&count<3){
								msgQueue.addFirst(info);
							}
						}catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			};
		
		fixedThreadPoolSend.execute(target);
		
	}
	
	 
	private void recieve(Socket s){
		if(fixedThreadPoolRecieve==null){
			 fixedThreadPoolRecieve = Executors.newFixedThreadPool(10);  
		}
		Runnable target = new Runnable(){
			public void run(){
				try {
					HxMsg msg=SocketUtil.recieveMsg(s);
					String json=JacksonUtil.bean2Json(msg,true);
					System.out.println("收到：   "+json+"\n***************************");
					if("generated_success".equals(msg.getCmd())){
						HttpConnectUtil.upload(msg.getPath(), "http://192.168.10.176:8080/test/ModelReciever");
					}
					//HttpConnectUtil.postHttp(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		fixedThreadPoolRecieve.execute(target);
	}
	
	private  void recieve(){
		new Thread(){
			public void run(){
				try {
					System.out.println("开始接收消息……………………………………………………………………");
					while(true){
						Socket s=server.accept();
						recieve(s);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public  static boolean addToQueue(HxMsg msg){
		SendInfo info=new SendInfo();
		info.setMsg(msg);
		msgQueue.addLast(info);
		return true;
	}

	public int getSendPort() {
		return sendPort;
	}


	public void setSendPort(int sendPort) {
		this.sendPort = sendPort;
	}


	public int getServerPort() {
		return serverPort;
	}


	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}


	public int getMaxSendCount() {
		return maxSendCount;
	}

	public void setMaxSendCount(int maxSendCount) {
		this.maxSendCount = maxSendCount;
	}
	
	public String getPortsFilePath() {
		return portsFilePath;
	}


	public void setPortsFilePath(String portsFilePath) {
		this.portsFilePath = portsFilePath;
	}


}
