package metamorph.helper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metamorph.dao.DAO;
import metamorph.object.MetamorphField;
import metamorph.object.MetamorphForeignKey;
import metamorph.object.MetamorphObject;
import metamorph.utility.MetamorphNest;

public class RequestResponseServletObject<T>{
	private HttpServletRequest request;
	private HttpServletResponse response;
	public RequestResponseServletObject(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	public void forward(String url){
		try{
			request.getRequestDispatcher(url).forward(request, response);
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	public void sendRedirect(String url){
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void addAttribute(String attrName, Object obj){
		request.setAttribute(attrName, obj);
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
