package metamorph.utilitycontroller;

import java.lang.reflect.Method;
import java.util.List;

public class RequestBodyObject {
	private String name;
	private Class type;
	private List<RequestBodyFieldObject> fields;
	private int contentType;
	public RequestBodyObject(String name, Class type, List<RequestBodyFieldObject> fields, int contentType) {
		super();
		this.name = name;
		this.type = type;
		this.fields = fields;
		this.contentType = contentType;
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
	public List<RequestBodyFieldObject> getFields() {
		return fields;
	}
	public void setFields(List<RequestBodyFieldObject> fields) {
		this.fields = fields;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	
}
