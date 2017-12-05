package marc.type;

import marc.field.Field;

public final class ConfigIdentifier {
	private String tag;
	private int index;
	private char code;
	
	public ConfigIdentifier(){
		tag = Field.UNKNOWN_TAG;
		index = -1;
		code = '\0';
	}
	public ConfigIdentifier(String tag, int index, char value){
		this.tag = tag;
		this.index = index;
		this.code = value;
	}
	
	public String getTag(){
		return tag;
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
		return true;
	}
}
