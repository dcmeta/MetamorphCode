import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.codemodel.JClassAlreadyExistsException;

public class Helper {
	static Connection conn = null;
	public static String sentenceCase(String name){
		name = name.replace("_", "");
		return String.format("%s%s", name.substring(0,1).toUpperCase(),name.substring(1,name.length()));
	}
	public static void generateDBConfig(){
		 try {
			 FileWriter writer = new FileWriter("src/dbconfig.properties");
			 writer.write("jdbc.driver = com.mysql.jdbc.Driver\n");
			 writer.write("jdbc.hostname = localhost\n");
			 writer.write("jdbc.port = 3306\n");
			 writer.write("jdbc.dbname =\n");
			 writer.write("jdbc.user =\n");
			 writer.write("jdbc.pass =\n");
			 writer.close();
			 System.out.println("Successfully wrote to the file.");
	    } catch (IOException e) {
	    	System.out.println("An error occurred.");
	    	e.printStackTrace();
	    }
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
	public static String getNormalName(String s){
		return s.replaceAll("([^_])([A-Z])", "$1 $2");
	}
	public static Connection getConnection(){
		
	      try {
	         try {
	            Class.forName(Temporary.driver);
	         } catch (Exception e) {
	            System.out.println(e);
	         }
	         if(conn==null)
	        	 conn = (Connection) DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s?useSSL=false",Temporary.hostname,Temporary.port,Temporary.dbname), Temporary.username, Temporary.password);
	        
	      } catch (Exception e) {
	         System.out.println(e);
	      }
	      return conn;
	}
	public static Table getTable(String s){
		for(Table t : Temporary.tableList){
			if(t.getName().equals(s))
				return t;
		}
		return null;
	}
	public static void generateAllModel() throws SQLException, JClassAlreadyExistsException, IOException{
		Helper.generateAllTable();
		for(Table t : Temporary.tableList){
			EntityGenerator.generateModel(t);
		}
	}
	public static void generateAllController() throws SQLException, JClassAlreadyExistsException, IOException{
		Helper.generateAllTable();
		for(Table t : Temporary.tableList){
			ContollerGenerator.generateController(t);
		}
	}
	public static void generateAllPage() throws SQLException, JClassAlreadyExistsException, IOException{
		Helper.generateAllTable();
		for(Table t : Temporary.tableList){
			PageGenerator.generateTableComponent(t);
		  	 PageGenerator.generatePage(t);
		}
	}
	public static void generateAllCreate() throws SQLException, JClassAlreadyExistsException, IOException{
		Helper.generateAllTable();
		for(Table t : Temporary.tableList){
			PageGenerator.generateCreate(t);
		}
	}
	public static void generateAllEdit() throws SQLException, JClassAlreadyExistsException, IOException{
		Helper.generateAllTable();
		for(Table t : Temporary.tableList){
			PageGenerator.generateEdit(t);
		}
	}
	public static void generateModelCommand(String[] cmds) throws SQLException, JClassAlreadyExistsException, IOException{
		
		if(cmds[2]!=null){
			Helper.generateAllTable();
			EntityGenerator.generateModel(Helper.getTable(cmds[2]));
		}else{
			System.err.println("Command not found");
		}
	}
	public static void generateController(String[] cmds) throws SQLException, JClassAlreadyExistsException, IOException{
		
		if(cmds[2]!=null){
			Helper.generateAllTable();
			ContollerGenerator.generateController(Helper.getTable(cmds[2]));
		}else{
			System.err.println("Command not found");
		}
	}
	public static void generatePage(String[] cmds) throws IOException, SQLException, JClassAlreadyExistsException{
		if(cmds[2]!=null){
			Helper.generateAllTable();
			PageGenerator.generateTableComponent(Helper.getTable(cmds[2]));
			PageGenerator.generatePage(Helper.getTable(cmds[2]));
		}else{
			System.err.println("Command not found");
		}
	}
	public static void generateCreate(String[] cmds) throws IOException, SQLException, JClassAlreadyExistsException{
		if(cmds[2]!=null){
			Helper.generateAllTable();
			PageGenerator.generateCreate(Helper.getTable(cmds[2]));
		}else{
			System.err.println("Command not found");
		}
	}
	public static void generateEdit(String[] cmds) throws IOException, SQLException, JClassAlreadyExistsException{
		if(cmds[2]!=null){
			Helper.generateAllTable();
			PageGenerator.generateEdit(Helper.getTable(cmds[2]));
		}else{
			System.err.println("Command not found");
		}
	}
	public static void generatePage() throws SQLException, JClassAlreadyExistsException, IOException{
		Helper.generateAllTable();
		for(Table table : Temporary.tableList){
			
		}
	  	 
	}
	public static void generateAllTable() throws SQLException, JClassAlreadyExistsException, IOException{

		Helper.getProp();
		Connection conn = Helper.getConnection();
	      PreparedStatement pst = null;
	      ResultSet rs = null;
	      DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();
	      rs = meta.getTables(null, null, null, new String[] {
	         "TABLE"
	      });
	      int count = 0;
	      List<String> tableList = new ArrayList<>();
	      while (rs.next()) {
	         tableList.add(rs.getString("TABLE_NAME"));
	         count++;
	      }

		Temporary.tableList.clear();
	      rs.close();
	      for(String s : tableList){
	    	  List<Column> columnList = new ArrayList<>();
		  	  Table table = new Table();
		  	  String sql = String.format("show columns from %s", s);
		  	  pst = Helper.getConnection().prepareStatement(sql);
		  	  rs = pst.executeQuery();
		  	  while(rs.next()){
		  		  List<String> fkComp = new ArrayList<>();
		  		  fkComp = EntityGenerator.getForeignKeyTableName(Helper.getConnection(),Temporary.dbname,s,rs.getString("Field"));
		  		  columnList.add(new Column(rs.getString("Field")
		  				, rs.getString("Type")
		  	  			, rs.getString("Key").equals("PRI")?true:false
		  	  			, rs.getString("Key").equals("MUL")?true:false
		  	  			, rs.getString("Key").equals("MUL") ? fkComp.get(0):null
		  	  		  	, rs.getString("Key").equals("MUL") ? fkComp.get(1):null));
		  	  	  }
		  	  	  rs.close();
		  	  	  pst.close();
		  	  	  Temporary.tableList.add(table);
		  	  	  table.setName(s);
			  	  table.setCols(columnList);
	      }
	      
	    
	}
	public static void generateDAO(String[] cmds) throws SQLException, JClassAlreadyExistsException, IOException{
		if(cmds[2]!=null){
			Helper.generateAllTable();
			DAOGenerator.generateDAO(Helper.getTable(cmds[2]));
		}else{
			System.err.println("Command not found");
		}
	}
	public static void generateAllDAO() throws SQLException, JClassAlreadyExistsException, IOException{
		Helper.generateAllTable();
		for(Table t : Temporary.tableList){
			DAOGenerator.generateDAO(t);
		}
	}
	public static void generateAllCompByTable(String[] cmds)throws SQLException, JClassAlreadyExistsException, IOException{
		Helper.generateAllTable();
			EntityGenerator.generateModel(Helper.getTable(cmds[2]));
	  	  	DAOGenerator.generateDAO(Helper.getTable(cmds[2]));
		  	PageGenerator.generateTableComponent(Helper.getTable(cmds[2]));
	  	  PageGenerator.generatePage(Helper.getTable(cmds[2]));
	  	  PageGenerator.generateCreate(Helper.getTable(cmds[2]));
		  PageGenerator.generateEdit(Helper.getTable(cmds[2]));
    	  ContollerGenerator.generateController(Helper.getTable(cmds[2]));
    	  MetaOConfigGenerator.generateMetaOConfig();
  	    ControllerConfigGenerator.generateControllerConfig();
  	  	PageGenerator.generateClass();
	}
	public static void generate()throws SQLException, JClassAlreadyExistsException, IOException{
		Helper.generateAllTable();
		for(Table table : Temporary.tableList){
			EntityGenerator.generateModel(table);
	  	  	DAOGenerator.generateDAO(table);
		  	PageGenerator.generateTableComponent(table);
	  	  PageGenerator.generatePage(table);
	  	  PageGenerator.generateCreate(table);
		  PageGenerator.generateEdit(table);
    	  ContollerGenerator.generateController(table);
		}
		MetaOConfigGenerator.generateMetaOConfig();
	    ControllerConfigGenerator.generateControllerConfig();
	  	PageGenerator.generateClass();
	}
	public static void getProp(){
		ResourceBundle rb = ResourceBundle.getBundle("src/dbconfig");
		Temporary.driver = rb.getString("jdbc.driver");
		Temporary.hostname = rb.getString("jdbc.hostname");
		Temporary.port = rb.getString("jdbc.port");
		Temporary.dbname = rb.getString("jdbc.dbname");
		Temporary.username = rb.getString("jdbc.user");
		Temporary.password = rb.getString("jdbc.pass");
	}
}
