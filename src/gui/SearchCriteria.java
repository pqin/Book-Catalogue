package gui;

public class SearchCriteria {
	private String value;
	private boolean caseSensitive;
	
	public SearchCriteria(){
		value = "";
		caseSensitive = false;
	}
	
	
	/**
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public final void setValue(String value) {
		this.value = value;
	}


	/**
	 * @return the caseSensitive
	 */
	public final boolean isCaseSensitive() {
		return caseSensitive;
	}


	/**
	 * @param caseSensitive the caseSensitive to set
	 */
	public final void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}


	public boolean match(){
		// TODO
		return false;
	}
}
