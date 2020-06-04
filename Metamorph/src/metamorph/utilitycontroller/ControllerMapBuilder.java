package metamorph.utilitycontroller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metamorph.annotation.*;

public class ControllerMapBuilder {
	private Map<String, Class> controllerMapper = new HashMap();
	private Map<String, ControllerMapObject> controllerMapObject= new HashMap(); 
	public void compile() throws NoSuchMethodException, SecurityException{
		for(String key : controllerMapper.keySet()){
			controllerMapObject.put(controllerMapper.get(key).getName(),new ControllerMapObject(controllerMapper.get(key).getName(),getMethods(controllerMapper.get(key))));
		}
	}
	public Map<String, ControllerMapObject> getControllerMapObject(){
		return controllerMapObject;
	}
	public void addController(Class c){
		controllerMapper.put(c.getName(), c);
	}
	public Map<String, Map<String,RequestMethodObject>> getMethods(Class o) throws NoSuchMethodException, SecurityException{
		Map<String, Map<String,RequestMethodObject>> methodMapper = new HashMap();
		
		methodMapper.put("post",getPostMethod(o.getMethods()));
		methodMapper.put("get",getGetMethod(o.getMethods()));
		return methodMapper;
	}
	public Map<String,RequestMethodObject> getPostMethod(Method[] methods) throws NoSuchMethodException, SecurityException{
		Map<String,RequestMethodObject> postMap = new HashMap();
		for(Method m : methods){
			PostMethod postAnnotation = m.getAnnotation(PostMethod.class);
			if(postAnnotation!=null){
				String url = postAnnotation.url();
				List<RequestParameterObject> pathAndParam = getURLVariablesAndParam(m);
				RequestMethodObject requestMethodObject = new RequestMethodObject(getRequestBody(m)
						,pathAndParam
						,m);
				if(url.equals("")){
					postMap.put(url, requestMethodObject);					
				}else{
					postMap.put(convertURL(url),requestMethodObject);
				}
			}
		}
		return postMap;
	}
	public Map<String,RequestMethodObject> getGetMethod(Method[] methods) throws NoSuchMethodException, SecurityException{
		Map<String,RequestMethodObject> getMap = new HashMap();
		for(Method m : methods){
			GetMethod getAnnotation = m.getAnnotation(GetMethod.class);
			if(getAnnotation!=null){
				String url = getAnnotation.url();
				List<RequestParameterObject> pathAndParam = getURLVariablesAndParam(m);
				RequestMethodObject requestMethodObject = new RequestMethodObject(getRequestBody(m),pathAndParam,m);
				if(url.equals("")){
					getMap.put(url, requestMethodObject);					
				}else{
					getMap.put(convertURL(url),requestMethodObject);
				}
			}
		}
		return getMap;
	}
	public RequestBodyObject getRequestBody(Method method) throws NoSuchMethodException, SecurityException{
		for(Parameter parameter : method.getParameters()){
			RequestBody reqBody = parameter.getAnnotation(RequestBody.class);
			if(reqBody != null){
				return new RequestBodyObject(parameter.getName(),parameter.getType(),getField(parameter.getType()),reqBody.contentType());
			}
		}
		return null;
	}
	public List<RequestBodyFieldObject> getField(Class obj) throws NoSuchMethodException, SecurityException{
		List<RequestBodyFieldObject> fields = new ArrayList<>();
		for(Field f : obj.getDeclaredFields()){
			List<Method> gsMethod = getGetterSetterMethod(obj,f);
			fields.add(new RequestBodyFieldObject(f.getName(),f.getType(),gsMethod.get(1),gsMethod.get(0)));
		}
		return fields;
	}
	public List<Method> getGetterSetterMethod(Class obj, Field field) throws NoSuchMethodException, SecurityException{
		List<Method> getterSetterMethod = new ArrayList<>();
		getterSetterMethod.add(obj.getDeclaredMethod(String.format("set%s", convertToSentenceCase(field.getName())), field.getType()));
		getterSetterMethod.add(obj.getDeclaredMethod(String.format("get%s", convertToSentenceCase(field.getName())), null));
		return getterSetterMethod;
	}
	public List<RequestParameterObject> getURLVariablesAndParam(Method method){
		List<RequestParameterObject> reqParamList = new ArrayList<>();
		int i = 1;
		for(Parameter parameter : method.getParameters()){
			URLVariable urlVar = parameter.getAnnotation(URLVariable.class);
			RequestParameter reqPar = parameter.getAnnotation(RequestParameter.class);
			if(urlVar!=null){
				reqParamList.add(new RequestParameterObject(parameter.getName(),0,i,Integer.class,0));
			}else if(reqPar!=null){
				reqParamList.add(new RequestParameterObject(reqPar.name(),0,i,parameter.getType(),1));
			}
			i++;
		}
		return reqParamList;
	}
	public String convertURL(String url){
		String[] urls = url.split("/");
		int i=0;
		for(String u : urls){
			if(isURLVariable(u)){
				urls[i]="*";
			}
			i++;
		}
		return String.join("/", urls);
		
	}
	public boolean isURLVariable(String s){
		if(s.length()>=1 && s.charAt(0) == '{' && s.charAt(s.length()-1)  == '}'){
			return true;
		}
		return false;
	}
	public String convertToSentenceCase(String fieldName){
		return String.format("%c", fieldName.charAt(0)).toUpperCase() + fieldName.substring(1, fieldName.length());
	}
}
