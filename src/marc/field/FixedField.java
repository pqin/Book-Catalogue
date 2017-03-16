package marc.field;

import java.util.Arrays;

import marc.MARC;

public class FixedField extends ControlField {
	public FixedField(){
		super();
		data = new char[0];
		Arrays.fill(data, MARC.BLANK_CHAR);
	}
	public FixedField(String tag, final int length){
		super();
		setTag(tag);
		data = new char[length];
		Arrays.fill(data, MARC.BLANK_CHAR);
	}
	
	@Override
	public void clear(){
		Arrays.fill(data, MARC.BLANK_CHAR);
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
