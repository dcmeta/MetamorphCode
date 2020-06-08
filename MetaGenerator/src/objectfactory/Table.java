package objectfactory;
import java.util.*;
public class Table {
	private String name;
	private List<Column> cols;
	public Table(){}
	public Table(String name, List<Column> cols) {
		super();
		this.name = name;
		this.cols = cols;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Column> getCols() {
		return cols;
	}
	public void setCols(List<Column> cols) {
		this.cols = cols;
	}
	
}
