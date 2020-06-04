package metamorph.database;

import metamorph.object.MetamorphField;
import metamorph.object.MetamorphObject;

public class ColumnTableObjectLinker {
	private Object obj;
	private MetamorphField co;
	
	public ColumnTableObjectLinker(Object obj, MetamorphField co) {
		super();
		this.obj = obj;
		this.co = co;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public MetamorphField getCo() {
		return co;
	}
	public void setCo(MetamorphField co) {
		this.co = co;
	}
	
}
