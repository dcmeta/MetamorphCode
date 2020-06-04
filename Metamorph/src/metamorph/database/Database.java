package metamorph.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import metamorph.object.MetamorphObject;
import metamorph.utility.MetamorphNest;

public class Database {
	private MetamorphObject metaO;
	private Object obj;
	private QueryBuilder queryBuilder;
	public Database(Object obj){
		metaO = MetamorphNest.getMetamorphGenerator().getMetamorphObject(obj.getClass());
		this.obj = obj;
	}
	public Database(){}
	public int save(Object obj){
		try {
			queryBuilder = new QueryBuilder();
			return queryBuilder.querySave(metaO, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public List<Object> getAll(){
		try {
			queryBuilder = new QueryBuilder();
			return queryBuilder.queryReadAll(metaO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Object update(Object obj){
		try {
			queryBuilder = new QueryBuilder();
			return queryBuilder.queryUpdate(metaO, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Object getById(int id){
		try {
			queryBuilder = new QueryBuilder();
			return queryBuilder.queryReadById(metaO, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Object delete(Object obj){
		try {
			queryBuilder = new QueryBuilder();
			return queryBuilder.queryDelete(metaO, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public int deleteById(int id){
		try {
			queryBuilder = new QueryBuilder();
			return queryBuilder.queryDeleteById(metaO, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public void runCustomPostQuery(String query, Map<Integer, String> fields){
		try {
			queryBuilder = new QueryBuilder();
			queryBuilder.runPostCustomQuery(query, fields);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	public List<Object> runCustomGetQuery(String query, Map<Integer, String> fields){
//		try {
//			queryBuilder = new QueryBuilder();
//			return queryBuilder.runGetCustomQuery(query, fields, metaO);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	public List<Map<String,Object>> runRawFetchQuery(String query){
		try {
			queryBuilder = new QueryBuilder();
			return queryBuilder.runRawQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void commit(){
		try {
			queryBuilder.commit();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}
