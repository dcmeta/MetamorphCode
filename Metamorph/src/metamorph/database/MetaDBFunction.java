package metamorph.database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import metamorph.annotation.Column;
import metamorph.helper.Helper;
import metamorph.object.MetamorphField;
import metamorph.object.MetamorphForeignKey;
import metamorph.object.MetamorphObject;
import metamorph.utility.MetamorphNest;

public class MetaDBFunction {
	public static Object serializeObject(Object obj, Map<String,Object> rs) throws Exception{
		return dissambleField(obj,rs);
	}
	public static Object dissambleField(Object obj, Map<String,Object> rs){
		for(Field f : obj.getClass().getDeclaredFields()){
			Column col = f.getAnnotation(Column.class);
			if(col!=null){
				try {
					Object colValue = rs.get(col.name());
					if(colValue!=null){
						getSetterMethod(obj.getClass(),f).invoke(obj, Helper.convertObject(f.getType(), colValue));
					}
					
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
			}else{
				MetamorphObject metaO = MetamorphNest.getMetamorphGenerator().getMetamorphObject(f.getType());
				if(metaO!=null){
					Object newObj = null;
					try {
						newObj = serializeMetaO(metaO,rs);
						Method invoke = getSetterMethod(obj.getClass(),f);
						invoke.invoke(obj, newObj);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						// TODO Auto-generated catch block
						System.out.println(e1);
					}
				}
			}
		}
		return obj;
	}
	public static Object serializeMetaO(MetamorphObject metaO,Map<String,Object> rs){
		Object objRelease = null;
		try {
			objRelease = metaO.getMetamorphClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		for(MetamorphField metaF : metaO.getMetamorphField()){
			try {
				Object colValue = rs.get(metaF.getColumnName());
				if(colValue!=null)
					metaF.getSetterMethod().invoke(objRelease, Helper.convertObject(metaF.getFieldType(), colValue));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
			}
		}
		for(MetamorphForeignKey metaFK : metaO.getMetamorphForeignKey()){
			Object newFKObj = serializeMetaO(metaFK.getForeignKeyObj(),rs);
			try {
				metaFK.getSetterMethod().invoke(objRelease, newFKObj);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return objRelease;
	}
	public static Method getSetterMethod(Class c, Field f){
		try {
			return c.getMethod(String.format("set%s",convertToSentenceCase(f.getName())), f.getType());
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		return null;
	}
	public static String convertToSentenceCase(String fieldName){
		return String.format("%c", fieldName.charAt(0)).toUpperCase() + fieldName.substring(1, fieldName.length());
	}
}
