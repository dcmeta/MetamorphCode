package metamorph.utilitycontroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import metamorph.helper.Helper;


public class ControllerMap {
	private static ControllerMapBuilder controllerMapBuilder = new ControllerMapBuilder();
	public void process(){
		try {
			controllerMapBuilder.compile();
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	public ControllerMapBuilder getControllerMapBuilder(){
		return controllerMapBuilder;
	}
	public void addController(Class c){
		controllerMapBuilder.addController(c);
	}
	public static void invokeMethod(Object servletClass, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		for(String key : controllerMapBuilder.getControllerMapObject().keySet()){
			if(key.equals(servletClass.getClass().getName())){
				if(request.getMethod().equalsIgnoreCase("post")){
					ControllerMapObject cmo = controllerMapBuilder.getControllerMapObject().get(key);
					filter(request,cmo.getMethodMap().get("post"),servletClass, "post");
				}else if(request.getMethod().equalsIgnoreCase("get")){
					ControllerMapObject cmo = controllerMapBuilder.getControllerMapObject().get(key);
					filter(request,cmo.getMethodMap().get("get"),servletClass, "get");
				}
			}
		}
	}
	public static void filter(HttpServletRequest request,  Map<String,RequestMethodObject> methodMap, Object servletClass, String method) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		RequestMethodObject methodObj=null;
		String urls = "";
		List<Integer> urlVariableList = new ArrayList<>();
		Map<String,List<Integer>> mapUrl = request.getPathInfo() == null ? new HashMap() : convertURL(request.getPathInfo());
		for(String url : mapUrl.keySet()){
			urls = url;
			urlVariableList = mapUrl.get(url);
		}
		try{
			methodObj = methodMap.get(urls);
			Object obj = null;
			if(method.equals("post")){
				if(methodObj!=null){
					obj =  mapRequestBody(methodObj,request);
				}
				if(methodObj.getRequestParam()!=null && methodObj.getRequestParam().size()>0)
					prepareMultipleParamMethod(methodObj,urlVariableList, request,servletClass,obj);
				else
					methodObj.getMethod().invoke(servletClass, obj);
			}else{
				if(methodObj.getRequestParam()!=null && methodObj.getRequestParam().size()>0)
					prepareMultipleParamMethod(methodObj,urlVariableList, request,servletClass,null);
				else
					methodObj.getMethod().invoke(servletClass, null);
			}
		}catch(Exception e){
			System.err.println(e);
		}
		
	}
	public static void prepareMultipleParamMethod(RequestMethodObject methodObj, List<Integer> urlVariableList, HttpServletRequest request, Object servletClass, Object reqBody){
		Object[] urlVariables = new Object[reqBody==null ? methodObj.getRequestParam().size() : methodObj.getRequestParam().size()+1];
		
		int urlVariableOrderNumber = 0;
		int methodOrderNumber = 0;
		if(reqBody!=null){
			urlVariables[methodOrderNumber] = reqBody;
			methodOrderNumber++;
		}
			for(RequestParameterObject rp : methodObj.getRequestParam()){
				if(rp.getCategory()==0){
					
					urlVariables[methodOrderNumber] = urlVariableList.get(urlVariableOrderNumber);
					urlVariableOrderNumber++;
				}else{
					urlVariables[methodOrderNumber] = Helper.convertObject(rp.getType(), request.getParameter(rp.getName()));
				}
				methodOrderNumber++;
			}
			invokeMultipleParamMethod(servletClass, methodObj, urlVariables);
	}
	public static void invokeMultipleParamMethod(Object servletClass, RequestMethodObject methodObj, Object...args){
		try {
			methodObj.getMethod().invoke(servletClass, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println(e);
		}
	}
	public static Object mapRequestBody(RequestMethodObject obj, HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		
		Object newObj = (obj.getParameters().getType()).newInstance();
		try {
			if(obj.getParameters().getContentType()==0)
				newObj = setRequestBodyField(obj.getParameters().getFields(),newObj,request);
			else if(obj.getParameters().getContentType()==1)
				newObj = setRequestBodyFieldMultipart(obj.getParameters().getFields(),newObj,request);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return newObj;
	}
	public static Object setRequestBodyField(List<RequestBodyFieldObject> fields, Object obj, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		for(RequestBodyFieldObject field : fields){
			if(request.getParameter(field.getName())!=null){
				Class type = field.getType();
				try{
					field.getSetterMethod().invoke(obj,Helper.convertObject(type, request.getParameter(field.getName())));
				}catch(Exception e){}
				
			}	
		}
		return obj;
	}
	public static Object setRequestBodyFieldMultipart(List<RequestBodyFieldObject> fields, Object obj, HttpServletRequest request){
		String imgName=null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		Map<String,String> mapItem = new HashMap();
		Map<String,FileItem> files = new HashMap();
		List<FileItem> items = new ArrayList<>();
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e2) {
			e2.printStackTrace();
		}
		for(FileItem item : items){
			if(!item.isFormField()){
				files.put(item.getFieldName(), item);
				
			}else{
				mapItem.put(item.getFieldName(), item.getString());
			}
		}
		for(RequestBodyFieldObject field : fields){
			if(mapItem.get(field.getName())!=null){
				Class type = field.getType();
				try {
					field.getSetterMethod().invoke(obj,Helper.convertObject(type, mapItem.get(field.getName())));
				}  catch (Exception e) {
					e.printStackTrace();
				} 
			}	
			if(field.getType()==FileItem.class){
				if(files.get(field.getName())!=null){
					Class type = field.getType();
					try {
						field.getSetterMethod().invoke(obj,Helper.convertObject(type, files.get(field.getName())));
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(e);
					} 
				}
			}
		}
		return obj;
	}
	
	public static Map<String,List<Integer>> convertURL(String url){
		String[] urls = url.split("/");
		List<Integer> urlVariables = new ArrayList<>();
		Map<String,List<Integer>> result = new HashMap();
		int i=0;
		for(String u : urls){
			Integer num = getNumber(u);
			if(num != null){
				urls[i]="*";
				urlVariables.add(num);
			}
			i++;
		}
		result.put(String.join("/", urls), urlVariables);
		return result;
	}
	public static Integer getNumber(String s){
		try{
			
			return Integer.parseInt(s);
		}catch(Exception e){
			return null;
		}
	}
}
