package generator;

import generatorhelper.PageGeneratorHelper;
import objectfactory.Table;

public class PageTableGenerator {
	public static String generateTable(Table table){
		String tableResult = PageGeneratorHelper.jspTagGenerator();
		tableResult += "<jsp:include page=\"/template/template.jsp\">";
		tableResult += "<jsp:param name=\"content\" value=\"/component/table_"+table.getName()+"_component.jsp\" />";
		tableResult += "</jsp:include>";
		return tableResult;
	}
}
