package metamorph.object;

public class MetamorphPrimaryKey {
	private boolean isPrimaryKey;
	private boolean isAutoIncrement;
	public MetamorphPrimaryKey(boolean isPrimaryKey, boolean isAutoIncrement) {
		super();
		this.isPrimaryKey = isPrimaryKey;
		this.isAutoIncrement = isAutoIncrement;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}
	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}
	
}
