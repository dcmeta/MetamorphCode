package metamorph.utilitycontroller;

import java.lang.reflect.Method;

public class RequestBodyForeignKeyObject {
	private Method getterMethod;
	private Method setterMethod;
	
	public RequestBodyForeignKeyObject(Method getterMethod, Method setterMethod) {
		super();
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
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
