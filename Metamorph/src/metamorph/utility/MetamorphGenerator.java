package metamorph.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metamorph.annotation.Timestamp;

import metamorph.annotation.AutoGenerate;
import metamorph.annotation.Column;
import metamorph.annotation.ForeignKey;
import metamorph.annotation.Id;
import metamorph.annotation.Table;
import metamorph.object.MetamorphField;
import metamorph.object.MetamorphForeignKey;
import metamorph.object.MetamorphObject;
import metamorph.object.MetamorphPrimaryKey;

public class MetamorphGenerator {
	private List<Class> uncompiledMetamorphClass = new ArrayList<>();
	private Map<Class, MetamorphObject> metamorphObjects = new HashMap();
	private Map<String, Class> metamorphURLLinker = new HashMap();
	public void process(){
		for(Class c : uncompiledMetamorphClass){
			metamorphObjects.put(c, processMetamorphObject(c));
		}
	}
	public void addMetamorphClass(Class c){
		uncompiledMetamorphClass.add(c);
	}
	public MetamorphObject getMetamorphObject(Class c){
		return metamorphObjects.get(c);
	}
	public Class getMetamorphClassByURL(String url){
		return metamorphURLLinker.get(url);
	}
	public Map<Class, MetamorphObject> getMetamorphObjectMap(){
		return metamorphObjects;
	}
	public Map<String, Class> getMetamorphURLLinker(){
		return metamorphURLLinker;
	}
	public MetamorphObject getMetamorphObjByURL(String url){
		return metamorphObjects.get(metamorphURLLinker.get(url));
	}
	public MetamorphObject processMetamorphObject(Class c){
		Table tableAnnotation = (Table) c.getAnnotation(Table.class);
		String url=null,tableName=null;
		if(tableAnnotation != null)
			tableName = tableAnnotation.name();
		Map<String, List> metamorphFieldMap = processMetamorphFieldList(c);
		return new MetamorphObject(metamorphFieldMap.get("field"),metamorphFieldMap.get("fk"),url,tableName,c,getAlphaNumericString());
	}
	public String getAlphaNumericString() 
    { 

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(10); 
  
        for (int i = 0; i < 10; i++) { 
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        }
        return sb.toString(); 
    } 
	public Map<String, List> processMetamorphFieldList(Class c){
		Map<String, List> metamorphFieldMap = new HashMap();
		List<MetamorphForeignKey> mfk = new ArrayList<>();
		List<MetamorphField> mf = new ArrayList<>();
		for(Field f : c.getDeclaredFields()){
			Column column = f.getAnnotation(Column.class);
			Timestamp timestamp = f.getAnnotation(Timestamp.class);
			int timestampType=0;
			if(timestamp!=null){
				if(timestamp.name().equals("create"))
					timestampType=1;
				else if(timestamp.name().equals("update"))
					timestampType=2;
			}
			if(column!=null)
				mf.add(new MetamorphField(f.getName(),column.name(),f.getType(), processMetamorphPrimaryKey(f),getGetterMethod(f,c),getSetterMethod(f,c),timestampType));
			else
				mfk.add(processMetamorphForeignKey(f,c));
		}
		metamorphFieldMap.put("field", mf);
		metamorphFieldMap.put("fk", mfk);
		return metamorphFieldMap;
	}
	public MetamorphPrimaryKey processMetamorphPrimaryKey(Field f){
		Id pkAnnotation = f.getAnnotation(Id.class);
		AutoGenerate aiAnnotation = f.getAnnotation(AutoGenerate.class);
		return new MetamorphPrimaryKey(pkAnnotation == null ? false : true, aiAnnotation == null ? false : true);
	}
	public MetamorphForeignKey processMetamorphForeignKey(Field f, Class c){
		ForeignKey fk = f.getAnnotation(ForeignKey.class);
		if(fk!=null){
			return new MetamorphForeignKey(fk.column(),f.getName(),fk.table_reference(),fk.reference_column(),f.getType(),processMetamorphObject(f.getType()),getGetterMethod(f,c),getSetterMethod(f,c));
		}
		return null;
	}
	public Method getGetterMethod(Field field, Class c){
		String sentenceCase = convertToSentenceCase(field.getName());
		String getterName = String.format("get%s", sentenceCase);
		try {
			return c.getDeclaredMethod(getterName, null);
		} catch (Exception e) {
			return null;
		}
	}
	public Method getSetterMethod(Field field, Class c){
		String sentenceCase = convertToSentenceCase(field.getName());
		String getterName = String.format("set%s", sentenceCase);
		try {
			return c.getDeclaredMethod(getterName, field.getType());
		} catch (Exception e) {
			return null;
		}
	}
	public String convertToSentenceCase(String fieldName){
		return String.format("%c", fieldName.charAt(0)).toUpperCase() + fieldName.substring(1, fieldName.length());
	}
}
