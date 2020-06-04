package metamorph.utilitycontroller;

public class RequestParameterObject{
	private String name;
	private Object value;
	private int orderNumber;
	private Class type;
	private int category;
	public RequestParameterObject(String name, Object value, int orderNumber, Class type, int category) {
		this.name = name;
		this.value = value;
		this.orderNumber = orderNumber;
		this.type = type;
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	
}
