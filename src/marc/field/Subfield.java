package marc.field;

import marc.Record;

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
	
	public boolean contains(String query, boolean caseSensitive){
		String data0 = null;
		if (caseSensitive){
			data0 = data;
		} else {
			data0 = data.toLowerCase(Record.LOCALE);
			query = query.toLowerCase(Record.LOCALE);
		}
		int index = data0.indexOf(query);
		boolean match = (index != -1);
		return match;
	}
}
