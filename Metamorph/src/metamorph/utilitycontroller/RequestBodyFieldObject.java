package metamorph.utilitycontroller;

import java.lang.reflect.Method;
import java.util.List;

public class RequestBodyFieldObject{
	private String name;
	private Class type;
	private Method getterMethod;
	private Method setterMethod;
	
	public RequestBodyFieldObject(String name, Class type, Method getterMethod, Method setterMethod) {
		super();
		this.name = name;
		this.type = type;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
	public Method getGetterMethod() {
		return getterMethod;
	}
	public void setGetterMethod(Method getterMethod) {
		this.getterMethod = getterMethod;
	}
	public Method getSetterMethod() {
		return setterMethod;
	}
	public void setSetterMethod(Method setterMethod) {
		this.setterMethod = setterMethod;
	}
}
