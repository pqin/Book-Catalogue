package marc.resource;

public enum ResourceType {
	UNKNOWN("??", "Unknown"),
	BOOK("BK", "Book"),
	COMPUTER_FILE("CF", "Computer File"),
	CONTINUING_RESOURCE("CR", "Continuing Resource"),
	MAP("MP", "Map"),
	MUSIC("MU", "Music"),
	MIXED_MATERIAL("MX", "Mixed Material"),
	VISUAL_MATERIAL("VM", "Visual Material");
	
	private String code;
	private String name;
	private ResourceType(String code, String name){
		this.code = code;
		this.name = name;
	}
	public String getCode(){
		return code;
	}
	public String getName(){
		return name;
	}
}
