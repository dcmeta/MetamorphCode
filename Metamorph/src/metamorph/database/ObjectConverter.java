package metamorph.database;

public class ObjectConverter<T> extends java.lang.Object{
	private Object obj;
	public ObjectConverter(Object obj){
		this.obj = obj;
	}
	public T get(){
		return (T) obj;
	}
}
