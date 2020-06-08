package objectfactory;

public class Column {
	private String name;
	private String type;
	private boolean isPrimaryKey;
	private boolean isForeignKey;
	private String tableReference;
	private String columnReference;
	public Column(String name, String type, boolean isPrimaryKey, boolean isForeignKey, String tableReference, String columnReference) {
		super();
		this.name = name;
		this.type = type;
		this.isPrimaryKey = isPrimaryKey;
		this.isForeignKey = isForeignKey;
		this.tableReference = tableReference;
		this.columnReference = columnReference;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public boolean isForeignKey() {
		return isForeignKey;
	}
	public void setForeignKey(boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
	}
	public String getTableReference() {
		return tableReference;
	}
	public void setTableReference(String tableReference) {
		this.tableReference = tableReference;
	}
	public String getColumnReference() {
		return columnReference;
	}
	public void setColumnReference(String columnReference) {
		this.columnReference = columnReference;
	}
	
	
}
