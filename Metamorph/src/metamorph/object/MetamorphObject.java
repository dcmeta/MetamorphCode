package metamorph.object;

import java.util.List;

public class MetamorphObject {
	private List<MetamorphField> metamorphField;
	private List<MetamorphForeignKey> metamorphForeignKey;
	private String url;
	private String name;
	private Class metamorphClass;
	private String aliasName;
	public MetamorphObject(List<MetamorphField> metamorphField, List<MetamorphForeignKey> metamorphForeignKey,
			String url, String name, Class metamorphClass, String aliasName) {
		super();
		this.metamorphField = metamorphField;
		this.metamorphForeignKey = metamorphForeignKey;
		this.url = url;
		this.name = name;
		this.metamorphClass = metamorphClass;
		this.aliasName = aliasName;
	}
	public List<MetamorphField> getMetamorphField() {
		return metamorphField;
	}
	public void setMetamorphField(List<MetamorphField> metamorphField) {
		this.metamorphField = metamorphField;
	}
	public List<MetamorphForeignKey> getMetamorphForeignKey() {
		return metamorphForeignKey;
	}
	public void setMetamorphForeignKey(List<MetamorphForeignKey> metamorphForeignKey) {
		this.metamorphForeignKey = metamorphForeignKey;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getMetamorphClass() {
		return metamorphClass;
	}
	public void setMetamorphClass(Class metamorphClass) {
		this.metamorphClass = metamorphClass;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
}
