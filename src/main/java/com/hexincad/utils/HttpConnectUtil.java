package com.hexincad.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.hexincad.modelPost.HxMsg;

public class HttpConnectUtil {
		
		private static  String URL = "http://192.168.10.176:8080/test/ModelReciever";
		
		/**
		 * get方式
		 * @param url
		 * @author www.yoodb.com
		 * @return
		 */
		public static String getHttp() {
			String responseMsg = "";
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod = new GetMethod(URL);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
			try {
				httpClient.executeMethod(getMethod);
				/*ByteArrayOutputStream out = new ByteArrayOutputStream();
				InputStream in = getMethod.getResponseBodyAsStream();
				int len = 0;
				byte[] buf = new byte[1024];
				while((len=in.read(buf))!=-1){
					out.write(buf, 0, len);
				}
				responseMsg = out.toString("UTF-8");*/
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//释放连接
				getMethod.releaseConnection();
			}
			return responseMsg;
		}

		/**
		 * post方式
		 * @param url
		 * @param code
		 * @param type
		 * @author www.yoodb.com
		 * @return
		 */
		public static String postHttp(HxMsg msg) {
			String responseMsg = "";
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setContentCharset("UTF-8");
			PostMethod postMethod = new PostMethod(URL);
			try {
				postMethod.addParameter("msg", JacksonUtil.bean2Json(msg));
				httpClient.executeMethod(postMethod);
				System.out.println("#####发送：  "+JacksonUtil.bean2Json(msg)+"####################################\n");
				/*ByteArrayOutputStream out = new ByteArrayOutputStream();
				InputStream in = postMethod.getResponseBodyAsStream();
				int len = 0;
				byte[] buf = new byte[1024];
				while((len=in.read(buf))!=-1){
					out.write(buf, 0, len);
				}
				responseMsg = out.toString("UTF-8");
				if(responseMsg.equals("success")){
					
				}*/
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				postMethod.releaseConnection();
			}
			return responseMsg;
		}
		
		
		public static boolean upload(final String localFile,final String url){
			File file=new File(localFile);
			PostMethod post=new PostMethod(url);
			HttpClient client =new HttpClient();
			
			try {
				FilePart part =new FilePart(file.getName(), file);
				part.setContentType("x-world/x-vrml");   //vrml文件的MIMETYPE
				part.setCharSet("UTF-8");
				Part[] parts={part};
				
				post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
				client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
				int status =client.executeMethod(post);
				if(status==HttpStatus.SC_OK){
					return true;
				}else{
					return false;
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return false;
		}

}
