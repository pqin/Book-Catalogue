package marc.field;

import marc.MARC;

public class Field implements Comparable<Field> {
	protected String tag;
	protected char indicator1, indicator2;
	protected boolean repeatable;
	
	public Field(){
		tag = MARC.UNKNOWN_TAG;
		indicator1 = MARC.BLANK_CHAR;
		indicator2 = MARC.BLANK_CHAR;
		repeatable = false;
	}
	public Field(String tag){
		this.tag = tag;
		indicator1 = MARC.BLANK_CHAR;
		indicator2 = MARC.BLANK_CHAR;
		repeatable = false;
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
	 * Whether the Field can be repeated in a Record or not.
	 * @return repeatable
	 */
	public boolean isRepeatable(){
		return repeatable;
	}
	public boolean isControlField(){
		return tag.startsWith("00");
	}
	
	public String toString(){
		String s = String.format("%s%c%c", tag, indicator1, indicator2);
		return s;
	}
	
	protected boolean contains(String query, final boolean caseSensitive){
		return false;
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
