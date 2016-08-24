package marc.field;

public class FixedDatum {
	private int index;
	private int length;
	private String label;
	private Object value;
	
	public FixedDatum(int index, int length, String label, Object value){
		this.index = index;
		this.length = length;
		this.label = label;
		this.value = value;
	}
	
	public int getIndex(){
		return index;
	}
	public int getLength(){
		return length;
	}
	public String getLabel(){
		return label;
	}
	public Object getValue(){
		return value;
	}
	
	public void setValue(Object value){
		this.value = value;
	}
}
