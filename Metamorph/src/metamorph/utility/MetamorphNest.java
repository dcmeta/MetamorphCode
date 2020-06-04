package metamorph.utility;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import metamorph.annotation.AutoGenerate;
import metamorph.annotation.Column;
import metamorph.annotation.Id;
import metamorph.helper.Helper;
import metamorph.annotation.Table;
import metamorph.annotation.ForeignKey;

import metamorph.object.MetamorphField;
import metamorph.object.MetamorphForeignKey;
import metamorph.object.MetamorphObject;
import metamorph.object.MetamorphPrimaryKey;

public class MetamorphNest {
	private static MetamorphGenerator metamorphGenerator;
	public MetamorphNest(){
		metamorphGenerator = new MetamorphGenerator();
	}
	public void addClass(Class c){
		metamorphGenerator.addMetamorphClass(c);
	}
	public void process(){
		metamorphGenerator.process();
	}
	public static MetamorphGenerator getMetamorphGenerator(){
		return metamorphGenerator;
	}
	public static Object metamorphToObject(String url, HttpServletRequest request){
		try {
			MetamorphObject metamorphObject = metamorphGenerator.getMetamorphObjByURL(url);
			Object newObj = metamorphObject.getMetamorphClass().newInstance();
			try {
				newObj = setRequestBodyField(metamorphObject.getMetamorphField(),newObj,request);
				newObj = setForeignKey(metamorphObject.getMetamorphForeignKey(),newObj,request);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return newObj;
			
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Object setRequestBodyField(List<MetamorphField> fields, Object obj, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		for(MetamorphField field : fields){
			if(request.getParameter(field.getFieldName())!=null){
				Class type = field.getFieldType();
				field.getSetterMethod().invoke(obj,Helper.convertObject(type, request.getParameter(field.getFieldName())));
			}	
		}
		return obj;
	}
	public static Object setForeignKey(List<MetamorphForeignKey> fks, Object obj, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		for(MetamorphForeignKey fk : fks){
			Object newObj;
			try {
				newObj = fk.getForeignKeyClass().newInstance();
				for(MetamorphField f : fk.getForeignKeyObj().getMetamorphField()){
					if(f.getMetamorphPrimaryKey().isPrimaryKey()){
						f.getSetterMethod().invoke(newObj, Helper.convertObject(f.getFieldType(), request.getParameter(fk.getFieldName())));
					}
				}
				fk.getSetterMethod().invoke(obj,newObj);
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
			
			
		}
		return obj;
	}
}
