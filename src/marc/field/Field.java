package marc.field;

import marc.MARC;

public class Field implements Comparable<Field> {	
	protected String tag;
	protected char indicator1, indicator2;
	
	public Field(){
		tag = MARC.UNKNOWN_TAG;
		indicator1 = MARC.BLANK_CHAR;
		indicator2 = MARC.BLANK_CHAR;
	}
	public Field(String tag, char ind1, char ind2){
		this.tag = tag;
		indicator1 = ind1;
		indicator2 = ind2;
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
	public void setFieldData(char[] value){
		// implementation defined
	}
	public Subfield getSubfield(int index){
		return null;
	}
	public String getSubfield(){
		return null;
	}
	public void setAllSubfields(Subfield[] value){
		// implementation defined
	}
	
	public boolean isControlField(){
		return tag.startsWith("00");
	}
	
	public String toString(){
		String s = String.format("%s%c%c", tag, indicator1, indicator2);
		return s;
	}
	
	public boolean contains(String query, final boolean caseSensitive){
		return false;
	}
	
	@Override
	public int compareTo(Field o) {
		String tag0 = this.tag;
		String tag1 = o.tag;
		int result = tag0.compareTo(tag1);
		if (MARC.LEADER_TAG.equals(tag0)){
			result = -1 * Math.abs(result);
		} else if (MARC.LEADER_TAG.equals(tag1)){
			result = Math.abs(result);
		}
		return result;
	}
}
