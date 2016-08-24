package gui;

public enum MatchType {
	MATCH_ANY("Any"),
	MATCH_ALL("All"),
	MATCH_PHRASE("Phrase");
	
	private String name;
	private MatchType(String name){
		this.name = name;
	}
	
	@Override
	final public String toString(){
		return name;
	}
}
