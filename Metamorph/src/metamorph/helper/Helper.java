package metamorph.helper;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.fileupload.FileItem;

import metamorph.object.MetamorphField;
import metamorph.object.MetamorphObject;
import metamorph.utility.MetamorphNest;

public class Helper {
	public static Object convertObject(Class type, Object value){
		if(type.getName().equals("int") || type.getName().equals("java.lang.Integer")){
			return Integer.parseInt(value.toString());
		}else if(type.getName().equals("long") || type.getName().equals("java.lang.Long")){
			return Long.parseLong(value.toString());
		}else if(type.getName().equals("java.util.Date")){
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			try {
				return simpleDateFormat.parse(value.toString());
			} catch (ParseException e) {
				pattern = "yyyy-MM-dd";
				try {
					return new SimpleDateFormat(pattern).parse(value.toString());
				} catch (ParseException e1) {
					return null;
				}
			}
		}else if(type.getName().equals("boolean") || type.getName().equals("java.lang.Boolean")){
			return Boolean.parseBoolean(value.toString());
		}else if(type.getName().equals("double") || type.getName().equals("java.lang.Double")){
			return Double.parseDouble(value.toString());
		}else if(type.getName().equals("java.lang.String")){
			return value;
		}else if(type == FileItem.class){
			return value;
		}else{
			try {
				Object obj = type.newInstance();
				MetamorphObject to = MetamorphNest.getMetamorphGenerator().getMetamorphObject(type);
				for(MetamorphField co : to.getMetamorphField()){
					if(co.getMetamorphPrimaryKey().isPrimaryKey()){
						try {
							co.getSetterMethod().invoke(obj, convertObject(co.getFieldType(),value));
						} catch (IllegalArgumentException e) {
							System.out.println(e);
						} catch (InvocationTargetException e) {
							System.out.println(e);
						}
					}
				}
				return obj;
			} catch (InstantiationException | IllegalAccessException e) {
				System.out.println(e);
			}
			return null;
		}
	}
}
