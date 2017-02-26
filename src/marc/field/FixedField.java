package marc.field;

import java.util.Arrays;

import marc.MARC;

public class FixedField extends ControlField {
	protected int FIXED_FIELD_LENGTH;
	
	public FixedField(){
		super();
		FIXED_FIELD_LENGTH = 0;
		data = new char[FIXED_FIELD_LENGTH];
		Arrays.fill(data, MARC.FILL_CHAR);
	}
	public FixedField(int length){
		super();
		FIXED_FIELD_LENGTH = length;
		data = new char[FIXED_FIELD_LENGTH];
		Arrays.fill(data, MARC.FILL_CHAR);
	}
	public FixedField(String tag, int length){
		super();
		setTag(tag);
		FIXED_FIELD_LENGTH = length;
		data = new char[FIXED_FIELD_LENGTH];
		Arrays.fill(data, MARC.FILL_CHAR);
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
	
	public FixedField copy(){
		FixedField copy = new FixedField(this.tag, data.length);
		copy.setFieldData(this.data);
		return copy;
	}
}
