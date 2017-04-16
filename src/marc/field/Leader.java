package marc.field;

public final class Leader extends FixedField {
	public static final String TAG = "LDR";
	public static final int FIELD_LENGTH = 24;
	// positions of data
	private static final int RECORD_LENGTH = 0;
	public static final int TYPE = 6;
	private static final int CHARACTER_CODING_SCHEME = 9;
	private static final int BASE_ADDRESS = 12;
	
	public Leader(){
		super(TAG, 24);
		initialize();
	}
	
	private final void initialize(){
		setDataToValue(0, RECORD_LENGTH, 5);
		data[TYPE] = FixedField.BLANK;
		data[CHARACTER_CODING_SCHEME] = FixedField.BLANK;
		data[10] = '2';
		data[11] = '2';
		setDataToValue(24, BASE_ADDRESS, 5);
		data[20] = '4';
		data[21] = '5';
		data[22] = '0';
		data[23] = '0';
	}
	
	// record length in bytes
	public int getLength(){
		return getValueFromData(RECORD_LENGTH, 5);
	}
	public void setLength(int value){
		// five places = 10^5
		if (value >= 0 && value < 100000){
			setDataToValue(value, RECORD_LENGTH, 5);
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
		return getValueFromData(BASE_ADDRESS, 5);
	}
	public void setBaseAddress(int value){
		// five places = 10^5
		if (value >= 0 && value < 100000){
			setDataToValue(value, BASE_ADDRESS, 5);
		}
	}
	
	@Override
	public boolean isRepeatable(){
		return false;
	}
	@Override
	public void clear(){
		super.clear();
		initialize();
	}
	
	public Leader copy(){
		Leader copy = new Leader();
		copy.setFieldData(this.data);
		return copy;
	}
}
