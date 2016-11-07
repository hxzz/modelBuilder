package com.hexincad.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hexincad.modelPost.HxMsg;
import com.hexincad.modelPost.SocketPost;
import com.hexincad.utils.JacksonUtil;

/**
 * Servlet implementation class ModeleBuidlerServlet
 */
@WebServlet("/ModeleBuidlerServlet")
public class ModeleBuidlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ModeleBuidlerServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected  void doGet(HttpServletRequest request, HttpServletResponse response){
		
		try {
			request.setCharacterEncoding("UTF-8");
			String msg=request.getParameter("msg");
			HxMsg hxMsg=null;
			hxMsg = JacksonUtil.json2Bean(msg, HxMsg.class);
			response.setCharacterEncoding("UTF-8");
			
			boolean rs=SocketPost.addToQueue(hxMsg);
			if(!rs){
				response.getWriter().append("failed");
				return;
			}
			response.getWriter().append("recieved");
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	

	
}
