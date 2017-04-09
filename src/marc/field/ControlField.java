package marc.field;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlField extends Field {
	private static final int RADIX = 10;	// all numbers in data are in base-10
	
	protected char[] data;
	
	public ControlField(){
		super();
		data = new char[0];
	}
	public ControlField(int length){
		super();
		data = new char[length];
		Arrays.fill(data, FixedField.BLANK);
	}
	public ControlField(String tag, int length){
		super(tag, Field.BLANK_INDICATOR, Field.BLANK_INDICATOR);
		data = new char[length];
		Arrays.fill(data, FixedField.BLANK);
	}
	public ControlField(String tag, char[] value){
		super(tag, Field.BLANK_INDICATOR, Field.BLANK_INDICATOR);
		data = Arrays.copyOf(value, value.length);
	}
	
	@Override
	public int getDataCount(){
		return 1;
	}
	@Override
	public char[] getFieldData(){
		char[] value = null;
		if (data == null){
			value = new char[0];
		} else {
			value = Arrays.copyOf(data, data.length);
		}
		return value;
	}
	@Override
	public String getFieldString(){
		return String.valueOf(data);
	}
	@Override
	public void setFieldData(char[] value){
		if (value == null){
			data = new char[0];
		} else {
			if (value.length != data.length){
				data = new char[value.length];
			}
			System.arraycopy(value, 0, data, 0, data.length);
		}
	}
	
	/**
	 * Returns data at index as char[length], padding with '|' character as necessary.
	 * @param index the index to query
	 * @param length the length of the data array to return
	 * @return the data at index to index+length
	 */
	public char[] getData(int index, int length) {
		char[] value = new char[length];
		if (index >= 0 && index+length <= data.length){
			value = Arrays.copyOfRange(data, index, index+length);
		} else {
			Arrays.fill(value, FixedField.BLANK);
		}
		return value;
	}
	
	/**
	 * Returns data at index as an integer.
	 * @param index the index to query
	 * @param length the length to query
	 * @return the data at index to index+length as an integer
	 */
	public int getValueFromData(int index, int length){
		int value = 0;
		String tmp = new String(getData(index, length));
		try {
			value = Integer.parseInt(tmp, RADIX);
		} catch (NumberFormatException e){
			value = 0;
		}
		return value;
	}
	
	/**
	 * Sets the field character at index to value.
	 * @param value the value to set to
	 * @param index the index of the field to set
	 */
	public void setData(char value, int index){
		setDataToValue(value, index);
	}
	public void setData(char[] value, int index, int length){
		setDataToValue(value, FixedField.BLANK, index, length);
	}
	
	protected void setDataToValue(int value, int offset, int length){
		String format = String.format("%%%02dd", length);
		String s = String.format(format, value);
		for (int i = 0; i < s.length(); ++i){
			data[i+offset] = s.charAt(i);
		}
	}
	protected void setDataToValue(char value, int index){
		data[index] = value;
	}
	/**
	 * Sets data to value, left-aligned. If the length of value is less than length,
	 * the extra positions are padded with the specified fill character.
	 * @param value the value to set
	 * @param fill the fill character to pad with
	 * @param offset the position to set data at
	 * @param length the length to set to, padding if necessary
	 */
	protected void setDataToValue(char value[], char fill, int offset, int length){
		for (int i = 0; i < length; ++i){
			if (i < value.length){
				data[i+offset] = value[i];
			} else {
				data[i+offset] = fill;
			}
		}
	}
	
	@Override
	public boolean contains(Pattern query){
		String reference = String.copyValueOf(data);
		Matcher m = query.matcher(reference);
		boolean match = m.find();
		return match;
	}
	
	public ControlField copy(){
		ControlField copy = new ControlField(this.tag, data.length);
		copy.setFieldData(this.data);
		return copy;
	}
}
