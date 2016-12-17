package marc.field;

import java.util.Arrays;

import marc.MARC;

public class FixedField extends ControlField {
	protected int FIXED_FIELD_LENGTH;
	protected FixedDatum[] map = buildMap();
	
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
	/**
	 * Returns array of all valid index positions for the fixed field.
	 * @return indices index array
	 */
	public int[] getIndexArray(){
		int[] array = new int[0];
		return array;
	}
	/**
	 * Returns length in characters of value at specified index for the fixed field.
	 * @return length length in characters of value
	 */
	public int getIndexLength(int index){
		return 0;
	}
	/**
	 * Returns whether data at index has multiple values.
	 * @param index the index to query
	 * @return data is multi-valued or not
	 */
	public boolean isMultivalue(int index){
		return false;
	}
	
	protected FixedDatum[] buildMap(){
		FixedDatum[] m = new FixedDatum[0];
		return m;
	}
}
