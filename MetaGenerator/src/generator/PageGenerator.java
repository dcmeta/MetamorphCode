package generator;
import java.io.File;
import objectfactory.*;
import helper.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;

import generatorhelper.PageGeneratorHelper;


public class PageGenerator {
	public static void generateClass() throws IOException{
		 
		String sideBar = "";
		 for(Table t : Temporary.tableList){
			 String table = t.getName();
			 sideBar += "<li class=\"nav-item\">\n<a class=\"nav-link\" href=\"${pageContext.request.contextPath}/"+table+"/show\">"+table+"</a>\n</li>\n";
		 }
			 
		 try {
			 PageGeneratorHelper.createNewDirectory("WebContent/template/");
		      File myObj = new File("WebContent/factory/template/sidebar.jsp");
		      FileWriter writer = new FileWriter("WebContent/template/sidebar.jsp");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        if(data.contains("#*TableList*#")){
		        	data = data.replace("#*TableList*#", sideBar);
		        	writer.write(data);
		        }
		        else
		        	writer.write(data);
		      }
		      myReader.close();
		      writer.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	public static void createWebXml(){
		try {
			PageGeneratorHelper.createNewDirectory("WebContent/WEB-INF/");
			 FileWriter writer = new FileWriter("WebContent/WEB-INF/web.xml");
			 writer.write("<?xml version='1.0' encoding='UTF-8'?>");
			 writer.write("\n<web-app xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://java.sun.com/xml/ns/javaee' xsi:schemaLocation='http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd' id='WebApp_ID' version='3.0'>");
			 writer.write("\n<display-name>Metamorph Example</display-name>");
			 writer.write("\n<welcome-file-list>");
			 writer.write("\n<welcome-file>/page/index.jsp</welcome-file>");
			 writer.write("\n</welcome-file-list>");
			 writer.write("\n<servlet>");
			 writer.write("\n<servlet-name>metaOConfig</servlet-name>");
			 writer.write("\n<servlet-class>config.MetaOConfigurator</servlet-class>");
			 writer.write("\n<load-on-startup>1</load-on-startup>");
			 writer.write("\n</servlet>");
			 writer.write("\n<servlet-mapping>");
			 writer.write("\n<servlet-name>metaOConfig</servlet-name>");
			 writer.write("\n<url-pattern>/moc</url-pattern>");
			 writer.write("\n</servlet-mapping>");
			 writer.write("\n<servlet>");
			 writer.write("\n<servlet-name>controllerconfig</servlet-name>");
			 writer.write("\n<servlet-class>config.MetaOControllerConfigurator</servlet-class>");
			 writer.write("\n<load-on-startup>2</load-on-startup>");
			 writer.write("\n</servlet>");
			 writer.write("\n<servlet-mapping>");
			 writer.write("\n<servlet-name>controllerconfig</servlet-name>");
			 writer.write("\n<url-pattern>/cc</url-pattern>");
			 writer.write("\n</servlet-mapping>");
			 writer.write("\n</web-app>");
			 writer.close();
			 System.out.println("Successfully wrote to the file.");
	    } catch (IOException e) {
	    	System.out.println("An error occurred.");
	    	e.printStackTrace();
	    }
	}
	
	public static void generateTableComponent(Table table) throws IOException{
		 try {
			 PageGeneratorHelper.createNewDirectory("WebContent/component/");
		      File myObj = new File(String.format("WebContent/factory/component/table_template_component.jsp", table.getName()));
		      FileWriter writer = new FileWriter(String.format("WebContent/component/table_%s_component.jsp", table.getName()));
		      Scanner myReader = new Scanner(myObj);
		      String head="",body="",pkName="";
		      String btnAdd = "<a href='create' class='d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm'><i class='text-white-50'></i> Add New Data</a>";
		      
		      body += "<c:forEach items=\"${result }\" var=\"result\" varStatus=\"vss\">\n<tr>";
		      for(Column data : table.getCols()){
		    	  head+="<th>"+Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName())))+"</th>\n";
		    	  if(data.isPrimaryKey()){
		    		  body+="\n<td>${vss.count}</td>";
		    		  pkName = data.getName();
		    	  }
		    	  else{
		    		  if(data.isForeignKey()){
		    			  body+=String.format("<td>${result.%s.%s }</td>",Helper.camelcasify(data.getName()),PageGeneratorHelper.getPrimaryKey(data.getTableReference()));
		    		  }else{
		    			  body+=String.format("<td>${result.%s }</td>",Helper.camelcasify(data.getName()));
		    		  }
		    	  }
		    		  
		      }
		      head+="<th>Update</th>\n<th>Delete</th>";
		      body+=String.format("\n<td><a href='edit/${result.%s}' class='d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm'><i class='text-white-50'></i> Update</a></td>",Helper.camelcasify(pkName));
		      body+=String.format("\n<td><a href='delete/${result.%s}' class='d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm'><i class='text-white-50'></i> Delete</a></td>",Helper.camelcasify(pkName));
		      body+="\n</tr>\n</c:forEach>";
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        writer.write("\n");
		        if(data.contains("#*Head*#"))
		        	writer.write(head);
		        else if(data.contains("#*Body*#"))
		        	writer.write(body);
		        else if(data.contains("#*Title*#")){
		        	data = data.replace("#*Title*#", table.getName());
		        	writer.write(data);
		        }else if(data.contains("#*Add*#")){
		        	data = data.replace("#*Add*#", btnAdd);
		        	writer.write(data);
		        }
		        else
		        	writer.write(data);
		      }
		      myReader.close();
		      writer.close();
		      System.out.println(String.format("Page component s is created",table.getName()));
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	public static void generatePage(Table table) throws IOException{
		 try {
			 PageGeneratorHelper.createNewDirectory("WebContent/page/");
		      File myObj = new File(String.format("WebContent/factory/page/table_template_page.jsp", table.getName()));
		      FileWriter writer = new FileWriter(String.format("WebContent/page/table_%s_page.jsp", table.getName()));
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        writer.write("\n");
		        if(data.contains("#*Comp*#")){
		        	data = data.replace("#*Comp*#", table.getName());
		        	writer.write(data);
		        }
		        else
		        	writer.write(data);
		      }
		      myReader.close();
		      writer.close();
		      System.out.println(String.format("Page %s is created",table.getName()));
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	public static void generateCreate(Table table) throws IOException{
		 try {
			 PageGeneratorHelper.createNewDirectory("WebContent/page/");
		      File myObj = new File(String.format("WebContent/factory/page/create_edit_template_page.jsp", table.getName()));
		      FileWriter writer = new FileWriter(String.format("WebContent/page/create_%s_page.jsp", table.getName()));
		      Scanner myReader = new Scanner(myObj);
		      String body = "",url="store";
		      for(Column data : table.getCols()){
		    	  if(data.getType().equals("date")){
		    		  body+="<div class='form-group'>\n";
			    	  body+=String.format("%s</br>\n", Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName()))));
			    	  body+=String.format("<input type='date' name='%s' class='form-control dateFormater' required/>",Helper.camelcasify(data.getName()));
			    	  body+="</div>";
		    	  }else if((!data.getType().equals("timestamp"))&&!data.isPrimaryKey()&&!data.isForeignKey()){
		    		  body+="<div class='form-group'>\n";
			    	  body+=String.format("%s</br>\n", Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName()))));
			    	  body+=String.format("<input name='%s' class='form-control' placeholder='%s' required/>",Helper.camelcasify(data.getName()),Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName()))));
			    	  body+="</div>";
		    	  }else if(data.isForeignKey()){
		    		  body+="<div class='form-group'>\n";
		    		  body+=String.format("%s</br>\n", Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName()))));
		    		  body+=String.format("<select name='%s' class='form-control' required> <c:forEach items=\"${%s }\" var='%s' varStatus='vss'>"+
									"<option value=${%s.%s }>${%s.%s }</option>"+
								"</c:forEach></select>",Helper.camelcasify(data.getName()),data.getTableReference(),data.getTableReference(),data.getTableReference(), Helper.camelcasify(data.getColumnReference()),data.getTableReference(),Helper.camelcasify(data.getColumnReference()));
		    		  body+="</div>";
		    	  }
		      }
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        writer.write("\n");
		        if(data.contains("#*Body*#")){
		        	data = data.replace("#*Body*#", body);
		        	writer.write(data);
		        }
		        else if(data.contains("#*URL*#")){
		        	data = data.replace("#*URL*#", url);
		        	writer.write(data);
		        }else
		        	writer.write(data);
		      }
		      myReader.close();
		      writer.close();
		      System.out.println(String.format("Page create %s is created",table.getName()));
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	public static void generateEdit(Table table) throws IOException{
		 try {
			 PageGeneratorHelper.createNewDirectory("WebContent/page/");
		      File myObj = new File(String.format("WebContent/factory/page/create_edit_template_page.jsp", table.getName()));
		      FileWriter writer = new FileWriter(String.format("WebContent/page/edit_%s_page.jsp", table.getName()));
		      Scanner myReader = new Scanner(myObj);
		      String body = "",url=String.format("${pageContext.request.contextPath}/%s/update", table.getName());
		      for(Column data : table.getCols()){
		    	  if(data.getType().equals("date")){
		    		  body+="<div class='form-group'>\n";
			    	  body+=String.format("%s</br>\n", Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName()))));
			    	  body+=String.format("<input type='date' name='%s' value='${result.%s}' class='form-control dateFormater' required/>",Helper.camelcasify(data.getName()),Helper.camelcasify(data.getName()));
			    	  body+="</div>";
		    	  }else if(data.isPrimaryKey()){
		    		  body+=String.format("<input type='hidden' name='%s' value='${result.%s}'/></br>",Helper.camelcasify(data.getName()),Helper.camelcasify(data.getName()),Helper.camelcasify(data.getName()));
		    	  }else if((!data.getType().equals("timestamp"))&& !data.isForeignKey()){
		    		  body+="<div class='form-group'>\n";
			    	  body+=String.format("%s</br>\n", Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName()))));
			    	  body+=String.format("<input name='%s' value='${result.%s}' class='form-control' placeholder='%s' required/>",Helper.camelcasify(data.getName()),Helper.camelcasify(data.getName()),Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName()))));
			    	  body+="</div>";
		    	  }else if(data.isForeignKey()){
		    		  body+="<div class='form-group'>\n";
		    		  body+=String.format("%s</br>\n", Helper.sentenceCase(Helper.getNormalName(Helper.camelcasify(data.getName()))));
		    		  body+=String.format("<select name='%s' class='form-control' required> <c:forEach items='${%s }' var='%s' varStatus='vss'>"+
									"<option value=${%s.%s } ${result.%s.%s == %s.%s ? 'selected' : ''}>${%s.%s }</option>"+
								"</c:forEach></select>",Helper.camelcasify(data.getName()),data.getTableReference(),data.getTableReference(),data.getTableReference(),Helper.camelcasify(data.getColumnReference()),Helper.camelcasify(data.getName()),Helper.camelcasify(data.getColumnReference()),data.getTableReference(),Helper.camelcasify(data.getColumnReference()),data.getTableReference(),Helper.camelcasify(data.getColumnReference()));
		    		  body+="</div>";
		    	  }
		      }
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        writer.write("\n");
		        if(data.contains("#*Body*#")){
		        	data = data.replace("#*Body*#", body);
		        	writer.write(data);
		        }
		        else if(data.contains("#*URL*#")){
		        	data = data.replace("#*URL*#", url);
		        	writer.write(data);
		        }else
		        	writer.write(data);
		      }
		      myReader.close();
		      writer.close();
		      System.out.println(String.format("Page edit %s is created",table.getName()));
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
}
