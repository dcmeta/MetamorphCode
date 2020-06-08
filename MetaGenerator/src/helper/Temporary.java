package helper;
import java.util.ArrayList;
import java.util.List;

import objectfactory.Table;

public class Temporary {
	public static String entityPkg = "entity";
	public static String controllerPkg = "controller";
	public static String configPkg = "config";
	public static String daoPkg = "dao";
	public static String annotationPkg = "metamorph.annotation";
	public static String controllerConfigPkg = "metamorph.utilitycontroller";
	public static String daoParentPkg = "metamorph.dao";
	public static String controllerParentPkg = "metamorph.controller";
	public static String metaOPkg = "metamorph.utility";
	public static List<Table> tableList = new ArrayList<>();
	public static String driver ;
	public static String hostname;
	public static String port;
	public static String dbname;
	public static String username;
	public static String password;
}
