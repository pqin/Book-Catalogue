package marc.field;

import java.util.regex.Pattern;

public class Field implements Comparable<Field> {
	public static final String UNKNOWN_TAG = "???";
	public static final char BLANK_INDICATOR = 0x20;
	
	protected String tag;
	protected char indicator1, indicator2;
	
	public Field(){
		tag = UNKNOWN_TAG;
		indicator1 = BLANK_INDICATOR;
		indicator2 = BLANK_INDICATOR;
	}
	protected Field(String tag, char ind1, char ind2){
		this.tag = tag;
		indicator1 = ind1;
		indicator2 = ind2;
	}
	
	public final boolean hasTag(String value){
		return tag.equals(value);
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
	public void setIndicators(char ind1, char ind2){
		indicator1 = ind1;
		indicator2 = ind2;
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
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + indicator1;
		result = prime * result + indicator2;
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
		if (indicator1 != other.indicator1) {
			return false;
		}
		if (indicator2 != other.indicator2) {
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
	public String toString(){
		StringBuilder buf = new StringBuilder();
		buf.append(tag);
		buf.append(indicator1);
		buf.append(indicator2);
		String f = getFieldString();
		if (f != null){
			buf.append(f);
		}
		return buf.toString();
	}
}
