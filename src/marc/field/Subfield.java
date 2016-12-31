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
	@Override
	public String toString(){
		return String.format("$%c%s", code, data);
	}
	@Override
	public boolean equals(Object o){
		boolean status = false;
		if (o == null){
			status = false;
		} else if (o instanceof Subfield){
			Subfield b = (Subfield) o;
			status = (this.code == b.code) & this.data.equals(b.data);
		} else {
			status = false;
		}
		return status;
	}

	public Subfield copy(){
		Subfield clone = new Subfield(code, data);
		return clone;
	}
}
