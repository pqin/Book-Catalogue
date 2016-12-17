package marc.field;

public class Subfield {
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
}
