package metamorph.helper;

public class Temporary {
	public static String url="";
	public static String databaseURL="";
	public static String databaseName="";
	public static String dbUsername;
	public static String dbPass="";
	public static String storeUrl;
	public static String showUrl;
	public static String updateUrl;
	public static String editUrl;
	public static String createUrl;
	public static String deleteUrl;
	public static String contextUrl;
	public static String indexUrl="/template/index.jsp";
	public static String templateShowUrl="/template/template.jsp";
	public static String templateEditCreateUrl="/template/template_create_edit.jsp";
	public static String getStoreURL(){
		return contextUrl+storeUrl;
	}
	public static String getShowURL(){
		return contextUrl+showUrl;
	}
	public static String getUpdateURL(){
		return contextUrl+updateUrl;
	}
	public static String getEditURL(){
		return contextUrl+editUrl;
	}
	public static String getCreateURL(){
		return contextUrl+createUrl;
	}
	public static String getDeleteURL(){
		return contextUrl+deleteUrl;
	}
}
