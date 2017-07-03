package marc.field;

public final class Subfield {
	private char code;
	private String data;
	
	public Subfield(){
		code = 'a';
		data = "";
	}
	public Subfield(char code, String data){
		this.code = code;
		this.data = data;
	}
	
	/**
	 * @return the code
	 */
	public char getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(char code) {
		this.code = code;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + code;
		result = (prime * result) + ((data == null) ? 0 : data.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (!(obj instanceof Subfield)){
			return false;
		}
		Subfield other = (Subfield) obj;
		if (code != other.code){
			return false;
		}
		if (data == null) {
			if (other.data != null){
				return false;
			}
		} else if (!data.equals(other.data)){
			return false;
		}
		return true;
	}
	public Subfield copy(){
		Subfield clone = new Subfield(code, data);
		return clone;
	}
	@Override
	public String toString(){
		return String.format("$%c%s", code, data);
	}
}
