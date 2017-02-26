package marc.field;

import java.util.Arrays;

import marc.MARC;


public final class Leader extends FixedField {
	public static final int LENGTH = 0;
	public static final int STATUS = 5;
	public static final int TYPE = 6;
	public static final int BIBLIO_LEVEL = 7;
	public static final int CONTROL_TYPE = 8;
	public static final int CHARACTER_CODING_SCHEME = 9;
	private static final int BASE_ADDRESS = 12;
	public static final int ENCODING_LEVEL = 17;
	public static final int DESCRIPTIVE_CATALOGUING_FORM = 18;
	public static final int MULTIPART_LEVEL = 19;
	private static final int DIRECTORY_STRUCTURE = 20;
	
	private int length;
	private int baseAddress;
	
	public Leader(){
		super(MARC.LEADER_TAG, MARC.LEADER_FIELD_LENGTH);		
		length = 0;
		baseAddress = FIXED_FIELD_LENGTH;
		initialize();
	}
	private void initialize(){
		setLength(0);
		setDataToValue('n', STATUS);
		setDataToValue('a', TYPE);
		setDataToValue('m', BIBLIO_LEVEL);
		setDataToValue(MARC.BLANK_CHAR, CONTROL_TYPE);
		setDataToValue(MARC.BLANK_CHAR, CHARACTER_CODING_SCHEME);
		data[10] = '2';				// indicator count
		data[11] = '2';				// subfield code count
		setDataToValue(baseAddress, BASE_ADDRESS, 5);
		setDataToValue('u', ENCODING_LEVEL);
		setDataToValue('a', DESCRIPTIVE_CATALOGUING_FORM);
		setDataToValue(MARC.BLANK_CHAR, MULTIPART_LEVEL);
		setDataToValue(450, DIRECTORY_STRUCTURE, 3);	// directory structure
		data[23] = '0';				// undefined
	}
	
	// record length in bytes
	public int getLength(){
		length = getValueFromData(LENGTH, 5);
		return length;
	}
	public void setLength(int value){
		// five places = 10^5
		if (value >= 0 && value < 100000){
			length = value;
			setDataToValue(length, LENGTH, 5);
		}
	}
	// character coding scheme
	public char getCharacterCodingScheme(){
		return data[CHARACTER_CODING_SCHEME];
	}
	public void setCharacterCodingScheme(char value){
		data[CHARACTER_CODING_SCHEME] = value;
	}
	// base address of data
	public int getBaseAddress(){
		baseAddress = getValueFromData(BASE_ADDRESS, 5);
		return baseAddress;
	}
	public void setBaseAddress(int value){
		// five places = 10^5
		if (value >= 0 && value < 100000){
			baseAddress = value;
			setDataToValue(baseAddress, BASE_ADDRESS, 5);
		}
	}
	
	@Override
	public void clear(){
		Arrays.fill(data, MARC.BLANK_CHAR);
		length = 0;
		baseAddress = FIXED_FIELD_LENGTH;
		initialize();
	}
	
	public Leader copy(){
		Leader copy = new Leader();
		copy.setFieldData(this.data);
		copy.length = this.length;
		copy.baseAddress = this.baseAddress;
		return copy;
	}
}
