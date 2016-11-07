package com.hexincad.filters;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.hexincad.modelPost.SocketPost;

/**
 * Servlet Filter implementation class IpFilter
 */
@WebFilter(filterName="/IpFilter",urlPatterns="/*")
public class IpFilter implements Filter {
    /**
     * Default constructor. 
     */
    public IpFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 String ip = request.getRemoteHost();  
	   
		 if(ip!=null){
			    ip=ip.trim();
		 }
		 Map<String,String> ipMap=IPStore.getInstance((HttpServletRequest)request).getIpMap();
		 if(validateIP(ip,ipMap)){
			    chain.doFilter(request, response);
		 }
		
	}
	
	 /** 
     *  
     * 功能描述: ip地址权限校验 
     *  
     * @param ipStr 请求ip 
     * @return 校验是否通过 
     */  
    private boolean validateIP(String ipStr,Map<String,String>ipMap) {  
        if (ipMap.isEmpty()) {  
            return true;  
        }  
        if (ipStr==null||ipStr.length()==0) {  
            return false;  
        }  
        if(ipMap.containsKey(ipStr))return true;
        return false;  
    }  


	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
