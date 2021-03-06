package marc.field;

import java.util.Arrays;

public class FixedField extends ControlField {
	public static final char BLANK = 0x20;
	public static final char FILL = 0x7C;
	
	public FixedField(){
		super();
		data = new char[0];
	}
	public FixedField(final int length){
		super();
		data = new char[length];
		Arrays.fill(data, BLANK);
	}
	public FixedField(String tag, final int length){
		super();
		setTag(tag);
		data = new char[length];
		Arrays.fill(data, BLANK);
	}
	
	@Override
	public void clear(){
		Arrays.fill(data, BLANK);
	}
	
	public char[] getData(FixedDatum mask){
		char[] value = new char[0];
		int start = mask.getIndex();
		int end = start + mask.getLength();
		if (start >= 0 && end <= data.length){
			value = Arrays.copyOfRange(data, start, end);
		}
		return value;
	}
	public void setData(char[] value, FixedDatum mask){
		setData(value, mask.getIndex(), mask.getLength());
	}
	
	public FixedField copy(){
		FixedField copy = new FixedField(this.tag, data.length);
		copy.setFieldData(this.data);
		return copy;
	}
}
