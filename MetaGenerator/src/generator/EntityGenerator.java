package generator;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

import helper.Helper;
import helper.Temporary;
import objectfactory.Column;
import objectfactory.Table;

public class EntityGenerator {
	public static void generateModel(Table table) throws SQLException, JClassAlreadyExistsException, IOException{
		  generate(table);
	}
	public static void generate(Table table) throws JClassAlreadyExistsException{
		JCodeModel codeModel = new JCodeModel();
  	  	List<Column> c = table.getCols();
	  	JPackage jp = codeModel._package(Temporary.entityPkg);
	  	JDefinedClass jc = jp._class(Helper.sentenceCase(table.getName()));
	  	jc.annotate(codeModel.ref(Temporary.annotationPkg+".Table")).param("name", table.getName());
	  	for(Column co : c){
			  JFieldVar var = null;
			  if(co.isForeignKey()){
				var = jc.field(JMod.PRIVATE, codeModel.ref(Temporary.entityPkg+"."+Helper.sentenceCase(co.getTableReference())), camelcasify(co.getName()));
			  }else{
				  var = jc.field(JMod.PRIVATE, filterClass(co.getType()), camelcasify(co.getName()));
			  }
			  
			  if(co.isForeignKey()){
				var.annotate(codeModel.ref(Temporary.annotationPkg+".ForeignKey")).param("table_reference", co.getTableReference())
				.param("reference_column", co.getColumnReference())
				.param("column", co.getName())
				.param("class_reference", codeModel.ref(Helper.sentenceCase(co.getTableReference())));
			  }
			  else if(co.isPrimaryKey() ){
				var.annotate(codeModel.ref(Temporary.annotationPkg+".Id"));
				var.annotate(codeModel.ref(Temporary.annotationPkg+".AutoGenerate"));
				var.annotate(codeModel.ref(Temporary.annotationPkg+".Column")).param("name", co.getName());
			  }else{
				  var.annotate(codeModel.ref(Temporary.annotationPkg+".Column")).param("name", co.getName());
			  }
			  if(co.getName().equals("created_at"))
				  var.annotate(codeModel.ref(Temporary.annotationPkg+".Timestamp")).param("name", "create");
			  else if(co.getName().equals("updated_at"))
				  var.annotate(codeModel.ref(Temporary.annotationPkg+".Timestamp")).param("name", "update");
			 getterMethod(jc,var);
			 setterMethod(jc,var,codeModel);
	  }
	  try{
		  codeModel.build(new File("src/"));
	  }catch(IOException e){System.out.println(e);}
	}
	public static void getterMethod(JDefinedClass jc, JFieldVar var){
		 JMethod getter = jc.method(JMod.PUBLIC, var.type(), getterName(var.name()));
       getter.body()._return(var);
	}
	public static String getterName(String name){
		return String.format("get%s",Helper.sentenceCase(name));
	}
	public static void setterMethod(JDefinedClass jc, JFieldVar var,JCodeModel codeModel){
		JMethod setter = jc.method(JMod.PUBLIC, codeModel.VOID, setterName(var.name()));
       setter.param(var.type(), var.name());
       setter.body().assign(JExpr._this().ref(var.name()), JExpr.ref(var.name()));
	}
	public static String setterName(String name){
		return String.format("set%s",Helper.sentenceCase(name));
	}
	public static String camelcasify(String in) {
	    StringBuilder sb = new StringBuilder();
	    boolean capitalizeNext = false;
	    for (char c : in.toCharArray()) {
	        if (c == '_') {
	            capitalizeNext = true;
	        } else {
	            if (capitalizeNext) {
	                sb.append(Character.toUpperCase(c));
	                capitalizeNext = false;
	            } else {
	                sb.append(c);
	            }
	        }
	    }
	    return sb.toString();
	}
	public static Class filterClass(String name){
		if(name.contains("var") || name.contains("text") || name.contains("char"))
			return String.class;
		else if(name.contains("bool"))
			return Boolean.class;
		else if(name.contains("int") || name.contains("num")|| name.contains("year"))
			return Integer.class;
		else if(name.contains("date") || name.contains("time"))
			return Date.class;
		else if(name.contains("double") || name.contains("real")|| name.contains("decimal")|| name.contains("float"))
			return Double.class;
		else 
			return null;
	}
	public static String foreignKeyName(String name){
		name = camelcasify(name);
		return String.format("%s%s", name.substring(0,1).toLowerCase(),name.substring(1,name.length()));
	}
	public static List<String> getForeignKeyTableName(Connection conn, String dbName, String tableName, String columnName)throws SQLException, JClassAlreadyExistsException{
		PreparedStatement pst = null;
		ResultSet rs = null;
  	  	String sql = String.format("SELECT REFERENCED_TABLE_NAME AS 'table', REFERENCED_COLUMN_NAME AS 'column' FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE "
  	  			+ "REFERENCED_TABLE_SCHEMA = '%s' AND "
  	  			+ "TABLE_NAME = '%s' AND "
  	  			+ "COLUMN_NAME = '%s';", dbName, tableName, columnName);
  	
  	  	pst = conn.prepareStatement(sql);
  	  	rs = pst.executeQuery();
  	  	List<String> nameAndColumn = new ArrayList<>();
  	  	if(rs.next()){
  	  		nameAndColumn.add(rs.getString("table"));
  	  		nameAndColumn.add(rs.getString("column"));
  	  	}
  	  	return nameAndColumn;
	}
}
