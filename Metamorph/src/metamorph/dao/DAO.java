package metamorph.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metamorph.database.Database;


public class DAO<T> {
	protected Database db;
	public DAO(T t){
		db = new Database(t);
	}
	public int save(T t){
		int id = db.save(t);
		db.commit();
		return id;
	}
	public Object update(T t){
		Object obj = db.update(t);
		db.commit();
		return obj;
	}
	public Object delete(T t){
		Object obj = db.delete(t);
		db.commit();
		return obj;
	}
	public int delete(int id){
		db.deleteById(id);
		db.commit();
		return id;
	}
	public List<T> getAll(){
		return (List<T>) db.getAll();
	}
	public T getById(int i){
		return (T) db.getById(i);
	}
}
