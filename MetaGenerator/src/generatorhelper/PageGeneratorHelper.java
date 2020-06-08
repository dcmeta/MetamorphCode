package generatorhelper;

import java.io.File;

import helper.Helper;
import objectfactory.Column;
import objectfactory.Table;

public class PageGeneratorHelper {
	public static String jspTagGenerator(){
		return "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8"+
				"pageEncoding=\"UTF-8\"%>"+
				"<%@ taglib uri=\"http://java.sun.com/jsp/jstl/core\" prefix=\"c\"%>";
	}
	public static String getPrimaryKey(String table){
		Table tbl = Helper.getTable(table);
		for(Column data : tbl.getCols()){
			if(data.isPrimaryKey())
				return Helper.camelcasify(data.getName());
		}
		return "";
	}
	public static void createNewDirectory(String path){
	    File directory = new File(path);
	    if (! directory.exists()){
	        directory.mkdirs();
	    }

	}
}
