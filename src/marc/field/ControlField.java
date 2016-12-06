package marc.field;

import java.nio.charset.Charset;
import java.util.Arrays;

import marc.MARC;
import marc.Record;

public class ControlField extends Field {
	protected int FIXED_FIELD_LENGTH;
	protected char[] data;
	
	public ControlField(){
		super();
		FIXED_FIELD_LENGTH = 0;
		data = new char[FIXED_FIELD_LENGTH];
		Arrays.fill(data, MARC.FILL_CHAR);
	}
	public ControlField(int length){
		super();
		FIXED_FIELD_LENGTH = length;
		data = new char[FIXED_FIELD_LENGTH];
		Arrays.fill(data, MARC.FILL_CHAR);
	}
	public ControlField(String tag, int length){
		super();
		setTag(tag);
		FIXED_FIELD_LENGTH = length;
		data = new char[FIXED_FIELD_LENGTH];
		Arrays.fill(data, MARC.FILL_CHAR);
	}
	
	public int[] getIndexArray(){
		int[] array = new int[0];
		return array;
	}
	public int getIndexLength(int index){
		return 0;
	}
	public boolean isMultivalue(int index){
		return false;
	}
	
	/**
	 * Returns data at index as char[length].
	 * @param index the index to query
	 * @param length the length of the data array to return
	 * @return the data at index to index+length
	 */
	public char[] getData(int index, int length) {
		char[] value = new char[length];
		if (index >= 0 && index+length <= FIXED_FIELD_LENGTH){
			value = Arrays.copyOfRange(data, index, index+length);
		} else {
			Arrays.fill(value, MARC.FILL_CHAR);
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
			value = Integer.parseInt(tmp, 10);
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
		setDataToValue(value, MARC.FILL_CHAR, index, length);
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
	 * the extra positions are padded with fill character.
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
	public int getSubfieldCount(){
		return 1;
	}
	@Override
	public Subfield getSubfield(int index){
		Subfield s = null;
		if (index == 0){
			s = new Subfield('a', new String(data));
		}
		return s;
	}
	@Override
	public String getSubfield(){
		return String.copyValueOf(data);
	}
	public void setAllSubfields(byte[] value, Charset encoding){
		String tmp = new String(value, encoding);
		data = Arrays.copyOf(tmp.toCharArray(), data.length);
		for (int i = 0; i < data.length; ++i){
			if (data[i] == '\u0000'){
				data[i] = MARC.FILL_CHAR;
			}
		}
	}
	public void setAllSubfields(char[] value){
		data = Arrays.copyOf(value, data.length);
		for (int i = 0; i < data.length; ++i){
			if (data[i] == '\u0000'){
				data[i] = MARC.FILL_CHAR;
			}
		}
	}
	@Override
	public void setAllSubfields(String value){
		data = Arrays.copyOf(value.toCharArray(), data.length);
		for (int i = 0; i < data.length; ++i){
			if (data[i] == '\u0000'){
				data[i] = MARC.FILL_CHAR;
			}
		}
	}
	@Override
	public void setAllSubfields(Subfield[] value){
		if (value == null){
			value = new Subfield[0];
		}
		if (value.length > 0){
			setAllSubfields(value[0].getData());
		}
	}
	@Override
	public void setSubfield(int index, Subfield value){
		if (index == 0 && value != null){
			if (value.getCode() == 'a'){
				setAllSubfields(value.getData());
			}
		}
	}
	
	public String toString(){
		String s = super.toString() + "$a" + getSubfield();
		return s;
	}
	
	@Override
	public boolean contains(String query, final boolean caseSensitive){
		String data0 = String.copyValueOf(data);
		if (!caseSensitive){
			data0 = data0.toLowerCase(Record.LOCALE);
			query = query.toLowerCase(Record.LOCALE);
		}
		int index = data0.indexOf(query);
		boolean match = (index != -1);
		return match;
	}
}
