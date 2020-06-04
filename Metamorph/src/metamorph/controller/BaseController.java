package metamorph.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metamorph.dao.DAO;
import metamorph.helper.RequestResponseServletObject;
import metamorph.utility.MetamorphNest;
import metamorph.utilitycontroller.ControllerMap;

public class BaseController<T> extends HttpServlet{
	protected RequestResponseServletObject<T> requestResponse;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			requestResponse = new RequestResponseServletObject(request,response);
			try {
				ControllerMap.invokeMethod(this,request,response);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			requestResponse = new RequestResponseServletObject(request,response);
			try {
				ControllerMap.invokeMethod(this,request,response);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
