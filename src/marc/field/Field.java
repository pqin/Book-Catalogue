package marc.field;

import java.util.Arrays;

import marc.MARC;
import marc.Record;

public class Field implements Comparable<Field> {
	private static final int RADIX = 10;	// all numbers in data are in base-10
	
	protected String tag;
	protected char indicator1, indicator2;
	protected char[] data;
	
	public Field(){
		tag = MARC.UNKNOWN_TAG;
		indicator1 = MARC.BLANK_CHAR;
		indicator2 = MARC.BLANK_CHAR;
		data = new char[0];
	}
	public Field(String tag, char ind1, char ind2){
		this.tag = tag;
		indicator1 = ind1;
		indicator2 = ind2;
		data = new char[0];
	}
	
	public String getTag(){
		return tag;
	}
	public void setTag(String tag){
		this.tag = tag;
	}
	public char getIndicator1(){
		return indicator1;
	}
	public void setIndicator1(char indicator){
		indicator1 = indicator;
	}
	public char getIndicator2(){
		return indicator2;
	}
	public void setIndicator2(char indicator){
		indicator2 = indicator;
	}
	public int getSubfieldCount(){
		return 0;
	}
	public Subfield getSubfield(int index){
		return null;
	}
	public String getSubfield(){
		return null;
	}
	public void setAllSubfields(String value){
		// implementation defined
	}
	public void setAllSubfields(Subfield[] value){
		// implementation defined
	}
	public void setSubfield(int index, Subfield value){
		// implementation defined
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
	
	public boolean isControlField(){
		return tag.startsWith("00");
	}
	
	public String toString(){
		String s = String.format("%s%c%c", tag, indicator1, indicator2);
		return s;
	}
	
	public boolean contains(String query, final boolean caseSensitive){
		String reference = String.copyValueOf(data);
		if (!caseSensitive){
			reference = reference.toLowerCase(Record.LOCALE);
			query = query.toLowerCase(Record.LOCALE);
		}
		return (reference.indexOf(query) != -1);
	}
	
	@Override
	public int compareTo(Field o) {
		String tag0 = this.tag;
		String tag1 = o.tag;
		int result = tag0.compareTo(tag1);
		if (result != 0){
			if (tag0.equals(MARC.UNKNOWN_TAG)){
				result = 1;
			} else if (tag0.equals(MARC.LEADER_TAG)){
				result = -1;
			}
		}
		return result;
	}
}
