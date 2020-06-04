package metamorph.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.ResourceBundle;

import metamorph.helper.Temporary;

public class DBUtil {
	static ResourceBundle rb = ResourceBundle.getBundle("dbconfig");
	
	static String driver = rb.getString("jdbc.driver");
	static String hostname = rb.getString("jdbc.hostname");
	static String port = rb.getString("jdbc.port");
	static String dbname = rb.getString("jdbc.dbname");
	static String username = rb.getString("jdbc.user");
	static String password = rb.getString("jdbc.pass");
	public static Connection getConnection() throws Exception{
		Class.forName(driver);
		return DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s",hostname,port,dbname),username,password);
	}
	public static void release(Connection con, Statement st){
		if(st!=null){
			try{
				st.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		if(con!=null){
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	public static void release(Connection con, Statement st, ResultSet rs){
		if(st!=null){
			try{
				st.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		if(con!=null){
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		if(rs!=null){
			try{
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
}
