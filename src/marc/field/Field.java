package marc.field;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Field implements Comparable<Field> {
	public static final int TAG_LENGTH = 3;
	public static final String UNKNOWN_TAG;
	private static final Pattern DATA_TAG_REGEX = Pattern.compile(String.format("[0-9]{%d}", TAG_LENGTH));

	public static final int INDICATOR_COUNT = 2;
	public static final char BLANK_INDICATOR = 0x20;
	public static final char[] INDICATOR_VALUES;
	static {
		// Compute UNKNOWN_TAG value
		char[] u = new char[TAG_LENGTH];
		Arrays.fill(u, '?');
		UNKNOWN_TAG = new String(u);
		// Compute valid indicator values.
		INDICATOR_VALUES = new char[1 + 10 + 26];	// BLANK + DIGITS + ALPHABET
		int k = 0;
		INDICATOR_VALUES[k++] = BLANK_INDICATOR;
		for (char c = '0'; c <= '9'; ++c){
			INDICATOR_VALUES[k++] = c;
		}
		for (char c = 'a'; c <= 'z'; ++c){
			INDICATOR_VALUES[k++] = c;
		}
	}
	
	protected String tag;
	protected char[] indicator;
	
	public Field(){
		tag = UNKNOWN_TAG;
		indicator = new char[INDICATOR_COUNT];
		Arrays.fill(indicator, BLANK_INDICATOR);
	}
	protected Field(String tag){
		this.tag = tag;
		indicator = new char[INDICATOR_COUNT];
		Arrays.fill(indicator, BLANK_INDICATOR);
	}
	protected Field(String tag, char[] ind){
		this.tag = tag;
		this.indicator = Arrays.copyOf(ind, INDICATOR_COUNT);
		for (int i = ind.length; i < INDICATOR_COUNT; ++i){
			this.indicator[i] = BLANK_INDICATOR;
		}
	}
	
	public final boolean hasTag(String value){
		return tag.equals(value);
	}
	public static final FieldType getFieldType(String t){
		FieldType type = FieldType.UNKNOWN;
		if (t != null && t.length() == TAG_LENGTH){
			if (DATA_TAG_REGEX.matcher(t).matches()){
				if (t.startsWith("00")){
					boolean isFixed = false;
					isFixed |= "006".equals(t);
					isFixed |= "007".equals(t);
					isFixed |= "008".equals(t);
					type = isFixed ? FieldType.FIXED_FIELD : FieldType.CONTROL_FIELD;
				} else {
					type = FieldType.DATA_FIELD;
				}
			} else if (Leader.TAG.equals(t)){
				type = FieldType.FIXED_FIELD;
			}
		}
		return type;
	}
	public static final boolean isControlTag(String t){
		boolean status = (t == null) ? false : t.startsWith("00");
		return status;
	}
	public static final boolean isFixedFieldTag(String t){
		boolean status = false;
		if (t != null){
			status |= Leader.TAG.equals(t);
			status |= "006".equals(t);
			status |= "007".equals(t);
			status |= FixedDataElement.TAG.equals(t);
		}
		return status;
	}
	public final boolean isControlField(){
		return isControlTag(tag);
	}
	public final boolean isFixedField(){
		return isFixedFieldTag(tag);
	}
	public boolean isRepeatable(){
		return true;
	}
	public boolean isRemoveable(){
		return true;
	}
	
	public String getTag(){
		return tag;
	}
	public void setTag(String tag){
		this.tag = tag;
	}
	public char getIndicator(int index){
		if (index >= 0 && index < INDICATOR_COUNT){
			return indicator[index];
		} else {
			throw new IndexOutOfBoundsException(String.format("Index %d not in range [0, %d]",
					index, INDICATOR_COUNT - 1));
		}
	}
	public void setIndicator(int index, char value){
		if (index >= 0 && index < INDICATOR_COUNT){
			indicator[index] = value;
		}
	}
	public int getDataCount(){
		return 0;
	}
	public char[] getFieldData(){
		// implementation defined
		return null;
	}
	public String getFieldString(){
		// implementation defined
		return null;
	}
	public void setFieldData(char[] value){
		// implementation defined
	}
	@Override
	public int compareTo(Field o) {
		String tag0 = this.tag;
		String tag1 = o.tag;
		int result = tag0.compareTo(tag1);
		if (Leader.TAG.equals(tag0)){
			result = -1 * Math.abs(result);
		} else if (Leader.TAG.equals(tag1)){
			result = Math.abs(result);
		}
		return result;
	}
	public boolean contains(Pattern query){
		return false;
	}
	public void clear(){
		// implementation defined
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(indicator);
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Field)) {
			return false;
		}
		Field other = (Field) obj;
		if (!Arrays.equals(indicator, other.indicator)) {
			return false;
		}
		if (tag == null) {
			if (other.tag != null) {
				return false;
			}
		} else if (!tag.equals(other.tag)) {
			return false;
		}
		return true;
	}
	public Field copy(){
		return null;
	}
	public String toString(){
		StringBuilder buf = new StringBuilder();
		buf.append(tag);
		buf.append(indicator);
		String f = getFieldString();
		if (f != null){
			buf.append(f);
		}
		return buf.toString();
	}
}
