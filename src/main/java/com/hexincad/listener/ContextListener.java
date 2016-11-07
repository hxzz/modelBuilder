package com.hexincad.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.hexincad.modelPost.SocketPost;
import com.hexincad.utils.PathUtils;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  { 
         // TODO Auto-generated method stub
    	String path=PathUtils.getResourcesPath(event.getServletContext(),"config/ports.properties" );
    	
    	SocketPost builder=SocketPost.getInstance();
    	if(builder==null){
			System.out.println("HXCAD转发服务器实例获取失败！………………………………………………………………………………");
			return;
		}
    	builder.setPortsFilePath(path);
		builder.init();
		System.out.println("HXCAD转发服务器参数初始化完毕………………………………………………………………………………");
		builder.start();
		System.out.println("HXCAD转发服务器启动完毕………………………………………………………………………………");
    }
	
}
