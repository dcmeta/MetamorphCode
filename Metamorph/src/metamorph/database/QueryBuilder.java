package metamorph.database;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysql.jdbc.Statement;

import metamorph.helper.Helper;
import metamorph.object.MetamorphField;
import metamorph.object.MetamorphForeignKey;
import metamorph.object.MetamorphObject;


public class QueryBuilder {
	private Connection con;
	private PreparedStatement pst;
	public int querySave(MetamorphObject metaO, Object obj2) throws Exception{
		String query = "insert into "+metaO.getName()+"(";
		List<MetamorphField> columns = metaO.getMetamorphField();
		Map<Integer, ColumnTableObjectLinker> newOrderColumns = new HashMap();
		int i = 0;
		int newOrder = 1;
		for(MetamorphField column : columns){
			query+=column.getColumnName();
			if(i<columns.size()-1)
				query+=", ";
			if(column.getMetamorphPrimaryKey().isAutoIncrement()){
				column.getSetterMethod().invoke(obj2, 0);
			}
			if(column.getIsTimestamp()==1||column.getIsTimestamp()==2){
				column.getSetterMethod().invoke(obj2, new Timestamp(new Date().getTime()));
			}
			ColumnTableObjectLinker cto = new ColumnTableObjectLinker(obj2,column);
			newOrderColumns.put(newOrder,cto);
			
			newOrder++;
			i++;
		}
		if(metaO.getMetamorphForeignKey().size()>0){
			query+=",";
			int x = 0;
			for(MetamorphForeignKey fko : metaO.getMetamorphForeignKey()){
				Object newObj = fko.getGetterMethod().invoke(obj2, null);
				
				for(MetamorphField column : fko.getForeignKeyObj().getMetamorphField()){
					if(column.getMetamorphPrimaryKey().isPrimaryKey()){
						if(x>0)
							query+=",";
						query+=fko.getColumnName();
						ColumnTableObjectLinker cto = new ColumnTableObjectLinker(newObj,column);
						newOrderColumns.put(newOrder,cto);
						newOrder++;
						x++;
						
					}
				}
			}
		}
		
		query+=")";
		i=0;
		query+="values(";
		
		for(MetamorphField column : columns){
			query+="?";
			if(i<columns.size()-1)
				query+=", ";
			i++;
		}
		if(metaO.getMetamorphForeignKey().size()>0){
			query+=",";
			int x = 0;
			for(MetamorphForeignKey fko : metaO.getMetamorphForeignKey()){
				for(MetamorphField column : columns){
					if(column.getMetamorphPrimaryKey().isPrimaryKey()){
						if(x>0)
							query+=",";
						query+="?";
						x++;
					}
				}
			}
		}
		query+=")";
		return runStatementInsert(newOrderColumns,obj2,query);
	}
	public int countPrimaryKey(List<MetamorphField> cols){
		int i =0;
		for(MetamorphField col : cols){
			if(col.getMetamorphPrimaryKey().isPrimaryKey()){
				i++;
			}
		}
		return i;
	}
	public Object queryUpdate(MetamorphObject obj, Object obj2) throws Exception{
		String query = "update "+obj.getName()+" set ";
		List<MetamorphField> columns = obj.getMetamorphField();
		List<MetamorphField> pks = new ArrayList<>();
		Map<Integer, ColumnTableObjectLinker> newOrderColumns = new HashMap();
		int i = 0;
		int newOrder = 1;
		for(MetamorphField column : columns){
			if(!column.getMetamorphPrimaryKey().isPrimaryKey()&&column.getIsTimestamp()!=1){
				query+=column.getColumnName() + "=?";
				if(i!=columns.size()-1)
					query+=",";
				if(column.getIsTimestamp()==2)
					column.getSetterMethod().invoke(obj2, new Timestamp(new Date().getTime()));
				ColumnTableObjectLinker cto = new ColumnTableObjectLinker(obj2,column);
				newOrderColumns.put(newOrder,cto);
				newOrder++;
				
			}else if(column.getMetamorphPrimaryKey().isPrimaryKey()){
				pks.add(column);
			}
			
			i++;
		}
		if(obj.getMetamorphForeignKey().size()>0){
			int x = 0;
			if(newOrder>1)
				query+=",";
			for(MetamorphForeignKey fko : obj.getMetamorphForeignKey()){
				Object newObj = fko.getGetterMethod().invoke(obj2, null);
				for(MetamorphField column : fko.getForeignKeyObj().getMetamorphField()){
					if(column.getMetamorphPrimaryKey().isPrimaryKey()){
						if(x>0)
							query+=",";
						query+=fko.getColumnName() + "=?";
						ColumnTableObjectLinker cto = new ColumnTableObjectLinker(newObj,column);
						newOrderColumns.put(newOrder,cto);
						newOrder++;
						x++;
					}
					
				}
			}
		}
		i=0;
		query+=" where ";
		for(MetamorphField pk : pks){
			query+=pk.getColumnName() + "=?" ;
			if(i!=pks.size()-1)
				query+=" and ";
			ColumnTableObjectLinker cto = new ColumnTableObjectLinker(obj2,pk);
			newOrderColumns.put(newOrder,cto);
			newOrder++;
		}
		return runStatement(newOrderColumns,obj2,query);
	}
	public String getQueryForeignKey(MetamorphObject obj){
		String query = "";
		String pk = getPrimaryKey(obj.getMetamorphField()).getColumnName();
		if(obj.getMetamorphForeignKey().size()>0){
			for(MetamorphForeignKey fko : obj.getMetamorphForeignKey()){
				query += String.format(" join %s %s on %s.%s = %s.%s", fko.getTableReferenceName(), fko.getForeignKeyObj().getAliasName(), fko.getForeignKeyObj().getAliasName(),fko.getColumnReferenceName(),obj.getAliasName(),fko.getColumnName());
				query += getQueryForeignKey(fko.getForeignKeyObj());
			}
		}
		return query;
	}
	public String getSelectQueryForeignKey(MetamorphObject obj){
		String query = "";
		int i = 0;
		if(obj.getMetamorphForeignKey().size()>0){
			for(MetamorphForeignKey fko : obj.getMetamorphForeignKey()){
				if(i>0)
					query+=",";
				query += getColumnQuery(fko.getForeignKeyObj());
				if(fko.getForeignKeyObj().getMetamorphForeignKey().size()>0){
					query+=",";
				}
				query += getSelectQueryForeignKey(fko.getForeignKeyObj());
				i++;
			}
		}
		return query;
	}
	public String getColumnQuery(MetamorphObject obj){
		String query = "";
		int i =0;
		String pk = getPrimaryKey(obj.getMetamorphField()).getColumnName();
		for(MetamorphField co : obj.getMetamorphField()){
				query += String.format("%s.%s ", obj.getAliasName(),co.getColumnName());
			if(i<obj.getMetamorphField().size()-1){
				query+=",";
			}
			i++;
		}
		return query;
	}
	public String prepareSelectQuery(MetamorphObject obj){
		String query = "select ";
		query += getColumnQuery(obj);
		if(obj.getMetamorphForeignKey().size()>0){
			query += ",";
		}
		query += getSelectQueryForeignKey(obj);
		String pk = getPrimaryKey(obj.getMetamorphField()).getColumnName();
		query += " from "+obj.getName()+" "+obj.getAliasName();
		query += getQueryForeignKey(obj);
		return query;
	}
	public List<Object> queryReadAll(MetamorphObject obj) throws Exception{
		con = DBUtil.getConnection();
		String query = prepareSelectQuery(obj);
		pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		List<Object> result = new ArrayList<>();
		List<MetamorphField> cols = obj.getMetamorphField();
		while(rs.next()){
			result.add(seTableObjectValue(obj,rs,""));
		}
		DBUtil.release(con, pst,rs);
		return result;
	}
	public List<MetamorphForeignKey> copyForeignKeyObject(List<MetamorphForeignKey> fkoList){
		List<MetamorphForeignKey> newFKOList = new ArrayList<>();
		for(MetamorphForeignKey fko : newFKOList){
			newFKOList.add(fko);
		}
		return newFKOList;
	}
	public Object queryReadById(MetamorphObject obj, int id) throws Exception{
		con = DBUtil.getConnection();
		MetamorphField pk = getPrimaryKey(obj.getMetamorphField());
		String query = prepareSelectQuery(obj);
		query += " where " + obj.getAliasName()+"."+pk.getColumnName() + "="+id;
		pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		List<MetamorphField> cols = obj.getMetamorphField();
		if(rs.next()){
			return seTableObjectValue(obj,rs,"");
		}
		DBUtil.release(con, pst,rs);
		return null;
	}
	public Object queryDelete(MetamorphObject obj, Object obj2) throws Exception{
		String query = "delete from "+obj.getName()+ " where ";
		List<MetamorphField> pkList = getPrimaryKeyList(obj.getMetamorphField());
		Map<Integer, ColumnTableObjectLinker> newMap = new HashMap();
		int i=0;
		for(MetamorphField col : pkList){
			query+=col.getColumnName()+"=?";
			if(i!=pkList.size()-1)
				query+=" and ";
			ColumnTableObjectLinker cto = new ColumnTableObjectLinker(obj2,col);
			newMap.put(i, cto);
			i++;
		}
		return runStatement(newMap,obj2,query);
	}
	public int queryDeleteById(MetamorphObject obj,int id) throws Exception{
		String query = "delete from "+obj.getName()+ " where ";
		List<MetamorphField> pkList = getPrimaryKeyList(obj.getMetamorphField());
		query+=pkList.get(0).getColumnName()+"=?";
		runStatement(id, query);
		return id;
	}
	public PreparedStatement getValue(Map<Integer, ColumnTableObjectLinker> columns, PreparedStatement pst, Object obj2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		for(Integer idx : columns.keySet()){
			pst.setObject(idx, columns.get(idx).getCo().getGetterMethod().invoke(columns.get(idx).getObj(), null));
		}
		return pst;
	}
	public int runStatementInsert(Map<Integer, ColumnTableObjectLinker> columns, Object obj2, String query) throws Exception{
		con = DBUtil.getConnection();
		con.setAutoCommit(false);
		pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst = getValue(columns,pst,obj2);
		pst.executeUpdate();
		ResultSet rs = pst.getGeneratedKeys();
		int id = 0;
		if(rs.next()){
			id = rs.getInt(1);
		}
		rs.close();
		return id;
	}
	public Object runStatement(Map<Integer, ColumnTableObjectLinker> columns, Object obj2, String query) throws Exception{
		con = DBUtil.getConnection();
		con.setAutoCommit(false);
		pst = con.prepareStatement(query);
		pst = getValue(columns,pst,obj2);
		pst.executeUpdate();
		return obj2;
	}
	public void runStatement(int id, String query) throws Exception{
		con = DBUtil.getConnection();
		con.setAutoCommit(false);
		pst = con.prepareStatement(query);
		pst.setInt(1, id);
		pst.executeUpdate();
	}
	public Object seTableObjectValue(MetamorphObject tb, ResultSet rs, String parent) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		Object objRelease = tb.getMetamorphClass().newInstance();
		String pk = getPrimaryKey(tb.getMetamorphField()).getColumnName();
		for(MetamorphField co : tb.getMetamorphField()){
			Class type = co.getFieldType();
			co.getSetterMethod().invoke(objRelease,Helper.convertObject(type, rs.getString(String.format("%s.%s",tb.getAliasName(), co.getColumnName()))));
		}
		for(MetamorphForeignKey fko : tb.getMetamorphForeignKey()){
			Object fkoObjRelease = seTableObjectValue(fko.getForeignKeyObj(),rs,tb.getName());
			fko.getSetterMethod().invoke(objRelease,fkoObjRelease);
		}
		return objRelease;
	}
	public MetamorphField getColumnValue(List<MetamorphField> colList, Object key){
		for(MetamorphField col : colList){
			if(col.getColumnName().equals(key))
				return col;
		}
		return null;
	}
	public MetamorphField getPrimaryKey(List<MetamorphField> cols){
		for(MetamorphField col : cols){
			if(col.getMetamorphPrimaryKey().isPrimaryKey())
				return col;
		}
		return null;
	}
	public List<MetamorphField> getPrimaryKeyList(List<MetamorphField> cols){
		List<MetamorphField> colPK = new ArrayList<>();
		for(MetamorphField col : cols){
			if(col.getMetamorphPrimaryKey().isPrimaryKey())
				colPK.add(col);
		}
		return colPK;
	}
	public void runPostCustomQuery(String query, Map<Integer,String> fields) throws Exception{
		con = DBUtil.getConnection();
		con.setAutoCommit(false);
		pst = con.prepareStatement(query);
		for(Integer key : fields.keySet()){
			pst.setString(key, fields.get(key));
		}
		pst.executeUpdate();
	}
//	public List<Object> runGetCustomQuery(String query, Map<Integer, String> fields, MetamorphObject tblObj) throws Exception{
//		con = DBUtil.getConnection();
//		pst = con.prepareStatement(query);
//		for(Integer key : fields.keySet()){
//			pst.setString(key, fields.get(key));
//		}
//		ResultSet rs = pst.executeQuery();
//		List<Object> result = new ArrayList<>();
//		List<MetamorphField> cols = tblObj.getMetamorphField();
//		while(rs.next()){
//			Map<String,Object> columnsMap = new HashMap();
//			for(MetamorphField col : cols){
//				columnsMap.put(col.getColumnName(), rs.getString(col.getColumnName()));
//			}
//			result.add(seTableObjectValue(tblObj,rs,""));
//		}
//		return result;
//	}
	public List<Map<String, Object>> runRawQuery(String query){
		ResultSet rs = null;
		List<Map<String, Object>> dataList = new ArrayList();
		try {
			con = DBUtil.getConnection();
			con.setAutoCommit(false);
			pst = con.prepareStatement(query);
			
			rs = pst.executeQuery();
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			int colCount = resultSetMetaData.getColumnCount();
			while(rs.next()){
				Map<String, Object> row = new HashMap();
				for (int i = 1; i <= colCount; i++) {
			        row.put(resultSetMetaData.getColumnName(i), rs.getObject(resultSetMetaData.getColumnName(i)));
			    }
				dataList.add(row);
			}
			DBUtil.release(con, pst,rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	public List<MetamorphField> copyList(List<MetamorphField> list){
		List<MetamorphField> newList = new ArrayList<>();
		for(MetamorphField co : list){
			newList.add(co);
		}
		return newList;
	}
	public void commit() throws SQLException{
		con.commit();
		DBUtil.release(con, pst);
	}
}
