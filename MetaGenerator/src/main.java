import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;

import downloader.FactoryDownloader;
import generator.PageGenerator;
import helper.Helper;

public class main {
	
	public static void main(String[] args) throws SQLException, JClassAlreadyExistsException, IOException{
		BufferedReader reader =  
                new BufferedReader(new InputStreamReader(System.in)); 
		System.out.print("$- ");
	     String cmd = reader.readLine();
	     if(!cmd.equals("")){
	    	 try{
	    		 String[] cmds = cmd.split(" ");
		    	 if(cmds[0].equals("mg")){
		    		 
		    		 filterSecondCmd(cmds);
		    	 }else{
		    		 System.err.println("Command not found");
		    	 }
	    	 }catch(Exception e){
	    		 System.err.println("Command not found");
	    	 }
	    	 
	     }
	}
	
	public static void filterSecondCmd(String[] cmds) throws SQLException, JClassAlreadyExistsException, IOException{
		try{
			if(cmds[1].equals("-a")){
				Helper.generate();
		    	PageGenerator.createWebXml();
			 }else if(cmds[1].equals("config"))
				Helper.generateDBConfig();
			 else if(cmds[1].equals("-m")){
				Helper.getProp();
				Helper.generateModelCommand(cmds);
			 }else if(cmds[1].equals("-c"))
				Helper.generateController(cmds);
			 else if(cmds[1].equals("-p"))
				Helper.generatePage(cmds);
			 else if(cmds[1].equals("-pc"))
				Helper.generateCreate(cmds);
			 else if(cmds[1].equals("-pe"))
				Helper.generateEdit(cmds);
			 else if(cmds[1].equals("-am"))
				Helper.generateAllModel();
			 else if(cmds[1].equals("-ac"))
				Helper.generateAllController();
			 else if(cmds[1].equals("-apc"))
				Helper.generateAllCreate();
			 else if(cmds[1].equals("-ap"))
				Helper.generateAllPage();
			 else if(cmds[1].equals("-ape"))
				Helper.generateAllEdit();
			 else if(cmds[1].equals("-d")){
				 Helper.generateDAO(cmds);
			 }else if(cmds[1].equals("-ad")){
				 Helper.generateAllDAO();
			 }else if(cmds[1].equals("-x")){
				 Helper.generateAllCompByTable(cmds);
			 }else if(cmds[1].equals("-h")){
				 showHelp();
			 }else if(cmds[1].equals("prepare")){
				 FactoryDownloader.readRepository();
			 }
			 else
				System.err.println("Command not found. Please use mg -h for getting help");
		}catch(Exception e){
   		 	System.err.println(e);
   	 	}	 
	}
	public static void showHelp(){
		System.out.println("-a : For generating All (Model, DAO, Controller, Show Page, Edit Page, Create Page, Controller Configurator, MetaO Configurator, web.xml");
		System.out.println("config : For generating dbconfig.properties");
		System.out.println("-m <Table Name> : For generating specific table's model");
		System.out.println("-d <Table Name> : For generating specific table's DAO");
		System.out.println("-c <Table Name> : For generating specific table's controller");
		System.out.println("-p <Table Name> : For generating specific table's show page");
		System.out.println("-pc <Table Name> : For generating specific table's create page");
		System.out.println("-pe <Table Name> : For generating specific table's edit page");
		System.out.println("-am : For generating all table's model");
		System.out.println("-ad : For generating all table's DAO");
		System.out.println("-ac : For generating all table's controller");
		System.out.println("-apc : For generating all table's create page");
		System.out.println("-ap : For generating all table's show page");
		System.out.println("-apc : For generating all table's create page");
		System.out.println("-ape : For generating all table's edit page");
		System.out.println("-x <Table Name> : For generating all components for specific table");
	}
}
