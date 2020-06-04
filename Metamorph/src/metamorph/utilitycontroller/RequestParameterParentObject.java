package metamorph.utilitycontroller;

public class RequestParameterParentObject {
	private String name;
	private int value;
	private int orderNumber;

	public RequestParameterParentObject(String name, int value, int orderNumber) {
		super();
		this.name = name;
		this.value = value;
		this.orderNumber = orderNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
}
