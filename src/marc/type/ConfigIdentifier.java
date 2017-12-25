package marc.type;

import marc.field.Field;

public final class ConfigIdentifier implements Comparable<ConfigIdentifier> {
	private String tag, field;
	private int index;
	private char code;	// TODO find use for variable, removal causes immediate hash collisions
	
	public ConfigIdentifier(String tag, int index){
		this.tag = tag;
		this.index = index;
		this.code = '\0';
		this.field = Field.UNKNOWN_TAG;
	}
	public ConfigIdentifier(String tag, int index, char value, String field){
		this.tag = tag;
		this.index = index;
		this.code = value;
		this.field = field;
	}
	
	public String getTag(){
		return tag;
	}
	public boolean hasTag(String value){
		return tag.equals(value);
	}
	public int getIndex(){
		return index;
	}
	public int getLength(){
		return 1;
	}
	public char getCode(){
		return code;
	}
	public String getFieldTag(){
		return field;
	}
	public boolean hasFieldTag(String value){
		return field.equals(value);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + index;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((field == null) ? 0 : tag.hashCode());
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
		if (!(obj instanceof ConfigIdentifier)) {
			return false;
		}
		ConfigIdentifier other = (ConfigIdentifier) obj;
		if (code != other.code) {
			return false;
		}
		if (index != other.index) {
			return false;
		}
		if (tag == null) {
			if (other.tag != null) {
				return false;
			}
		} else if (!tag.equals(other.tag)) {
			return false;
		}
		if (field == null) {
			if (other.field != null) {
				return false;
			}
		} else if (!field.equals(other.field)) {
			return false;
		}
		return true;
	}
	@Override
	public int compareTo(ConfigIdentifier other) {
		if (this == other){
			return 0;
		} else if (other == null){
			throw new NullPointerException();
		}
		int difference = tag.compareTo(other.tag);
		if (difference != 0){
			return difference;
		}
		difference = index - other.index;
		if (difference != 0){
			return difference;
		}
		difference = field.compareTo(other.field);
		if (difference != 0){
			return difference;
		}
		difference = Character.compare(code, other.code);
		return difference;
	}
}
