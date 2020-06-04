package metamorph.utilitycontroller;

import java.lang.reflect.Method;
import java.util.List;

public class RequestMethodObject {
	private RequestBodyObject parameters;
	private List<RequestParameterObject> requestParam;
	private Method method;
	
	public RequestMethodObject(RequestBodyObject parameters,List<RequestParameterObject> requestParam, Method method) {
		super();
		this.parameters = parameters;
		this.requestParam = requestParam;
		this.method = method;
	}
	public RequestBodyObject getParameters() {
		return parameters;
	}
	public void setParameters(RequestBodyObject parameters) {
		this.parameters = parameters;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public List<RequestParameterObject> getRequestParam() {
		return requestParam;
	}
	public void setRequestParam(List<RequestParameterObject> requestParam) {
		this.requestParam = requestParam;
	}
	
	
}
