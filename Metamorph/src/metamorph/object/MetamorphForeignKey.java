package metamorph.object;

import java.lang.reflect.Method;

public class MetamorphForeignKey {
	private String columnName;
	private String tableReferenceName;
	private String columnReferenceName;
	private String fieldName;
	private Class foreignKeyClass;
	private MetamorphObject foreignKeyObj;
	private Method getterMethod;
	private Method setterMethod;
	public MetamorphForeignKey(String columnName, String fieldName, String tableReferenceName, String columnReferenceName,
			Class foreignKeyClass,MetamorphObject foreignKeyObj, Method getterMethod, Method setterMethod) {
		super();
		this.columnName = columnName;
		this.fieldName = fieldName;
		this.tableReferenceName = tableReferenceName;
		this.columnReferenceName = columnReferenceName;
		this.foreignKeyClass = foreignKeyClass;
		this.foreignKeyObj = foreignKeyObj;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getTableReferenceName() {
		return tableReferenceName;
	}
	public void setTableReferenceName(String tableReferenceName) {
		this.tableReferenceName = tableReferenceName;
	}
	public String getColumnReferenceName() {
		return columnReferenceName;
	}
	public void setColumnReferenceName(String columnReferenceName) {
		this.columnReferenceName = columnReferenceName;
	}
	public Class getForeignKeyClass() {
		return foreignKeyClass;
	}
	public void setForeignKeyClass(Class foreignKeyClass) {
		this.foreignKeyClass = foreignKeyClass;
	}
	public MetamorphObject getForeignKeyObj() {
		return foreignKeyObj;
	}
	public void setForeignKeyObj(MetamorphObject foreignKeyObj) {
		this.foreignKeyObj = foreignKeyObj;
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
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
