package gui.search;

public enum MatchType {
	OR("OR"),
	AND("AND"),
	NOT("NOT");
	
	private String name;
	private MatchType(String name){
		this.name = name;
	}
	
	final public boolean matches(String arg0){
		return name.equals(arg0);
	}
	
	@Override
	final public String toString(){
		return name;
	}
}
