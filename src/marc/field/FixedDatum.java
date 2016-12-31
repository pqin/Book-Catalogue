package marc.field;

public final class FixedDatum {
	private int index;
	private int length;
	private String label;
	
	public FixedDatum(int index, int length, String label){
		this.index = index;
		this.length = length;
		this.label = label;
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
	
	public FixedDatum copy(){
		FixedDatum copy = new FixedDatum(this.index, this.length, this.label);
		return copy;
	}
}
