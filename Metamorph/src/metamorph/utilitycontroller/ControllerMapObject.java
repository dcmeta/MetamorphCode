package metamorph.utilitycontroller;

import java.lang.reflect.Method;
import java.util.Map;

public class ControllerMapObject {
	private String name;
	private Map<String, Map<String,RequestMethodObject>> methodMap;
	
	public ControllerMapObject(String name, Map<String, Map<String,RequestMethodObject>> methodMap) {
		super();
		this.name = name;
		this.methodMap = methodMap;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Map<String,RequestMethodObject>> getMethodMap() {
		return methodMap;
	}
	public void setMethodMap(Map<String, Map<String,RequestMethodObject>> methodMap) {
		this.methodMap = methodMap;
	}
	
}
