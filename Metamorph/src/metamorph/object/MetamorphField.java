package metamorph.object;

import java.lang.reflect.Method;

public class MetamorphField {
	private String fieldName;
	private String columnName;
	private Class fieldType;
	private MetamorphPrimaryKey metamorphPrimaryKey;
	private Method getterMethod;
	private Method setterMethod;
	private int isTimestamp;
	
	public MetamorphField(String fieldName, String columnName, Class fieldType, MetamorphPrimaryKey metamorphPrimaryKey,
			Method getterMethod, Method setterMethod, int isTimestamp) {
		super();
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.fieldType = fieldType;
		this.metamorphPrimaryKey = metamorphPrimaryKey;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
		this.isTimestamp = isTimestamp;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Class getFieldType() {
		return fieldType;
	}
	public void setFieldType(Class fieldType) {
		this.fieldType = fieldType;
	}
	public MetamorphPrimaryKey getMetamorphPrimaryKey() {
		return metamorphPrimaryKey;
	}
	public void setMetamorphPrimaryKey(MetamorphPrimaryKey metamorphPrimaryKey) {
		this.metamorphPrimaryKey = metamorphPrimaryKey;
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
	public int getIsTimestamp() {
		return isTimestamp;
	}
	public void setIsTimestamp(int isTimestamp) {
		this.isTimestamp = isTimestamp;
	}
}
