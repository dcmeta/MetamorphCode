package generator;

import java.util.HashMap;
import java.util.Map;

import generatorhelper.PageGeneratorHelper;
import helper.Helper;
import objectfactory.Column;
import objectfactory.Table;

public class PageTableComponentGenerator {
	public static String generateTableComponent(Table table){
		String tableComp = PageGeneratorHelper.jspTagGenerator();
		Map<String,String> tableHeadAndBody = generateBodyAndHead(table);
		
		tableComp += generateTableName(table.getName());
		tableComp += "<div class=\"d-sm-flex align-items-center justify-content-between mb-4\"><h1 class=\"h3 mb-0 text-gray-800\"></h1>\n";
		tableComp += generateAddButton();
		tableComp += "</div>\n";
		tableComp += " <div class=\"card shadow mb-4\">\n";
		tableComp += "<div class=\"card-header py-3\">\n";
		tableComp += "<h6 class=\"m-0 font-weight-bold text-primary\">Table</h6></div>\n";
		tableComp += "<div class=\"card-body\">\n";
		tableComp += "<div class=\"table-responsive\">\n";
		tableComp += "<table class=\"table table-bordered\" id=\"dataTable\" width=\"100%\" cellspacing=\"0\">\n";
		tableComp += "<thead>\n";
		tableComp += tableHeadAndBody.get("head");
		tableComp += "</thead>\n";
		tableComp += "<tbody>\n";
		tableComp += tableHeadAndBody.get("body");
		tableComp += "</tbody>\n";
		tableComp += "</table>\n";
		tableComp += "</div>\n</div>\n</div>";
		
		return tableComp;
	}
	public static String generateTableName(String tableName){
		return "<h1 class=\"h3 mb-2 text-gray-800\">"+tableName+"</h1>";
	}
	public static String generateAddButton(){
		return "<a href='create' class='d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm'><i class='text-white-50'></i> Add New Data</a>";
	}
	public static Map<String,String> generateBodyAndHead(Table table){
		Map<String,String> bodyAndHeadMap = new HashMap();
		String head="",body="",pkName="";
				  
		body += "<c:forEach items=\"${result }\" var=\"result\" varStatus=\"vss\">\n<tr>";
		for(Column data : table.getCols()){
			head+="<th>"+Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName())))+"</th>\n";
			if(data.isPrimaryKey()){
				body+="\n<td>${vss.count}</td>";
				pkName = data.getName();
			}
			else{
				if(data.isForeignKey()){
					body+=String.format("<td>${result.%s.%s }</td>",Helper.camelcasify(data.getName()), PageGeneratorHelper.getPrimaryKey(data.getTableReference()));
				}else{
					body+=String.format("<td>${result.%s }</td>",Helper.camelcasify(data.getName()));
				}
			}
		}
		head+="<th>Update</th>\n<th>Delete</th>";
		body+=String.format("\n<td><a href='edit/${result.%s}' class='d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm'><i class='text-white-50'></i> Update</a></td>",Helper.camelcasify(pkName));
		body+=String.format("\n<td><a href='delete/${result.%s}' class='d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm'><i class='text-white-50'></i> Delete</a></td>",Helper.camelcasify(pkName));
		body+="\n</tr>\n</c:forEach>";
		bodyAndHeadMap.put("head", head);
		bodyAndHeadMap.put("body", body);
		return bodyAndHeadMap;
	}
}
