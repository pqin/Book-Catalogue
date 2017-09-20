package marc.type;

import java.util.Locale;

public enum RecordType {
	BIBLIOGRAPHIC("Bibliographic", 0),
	AUTHORITY("Authority", 1),
	HOLDINGS("Holdings", 2),
	CLASSIFICATION("Classification", 3),
	COMMUNITY("Community", 4);
	
	private String name, command;
	private int order;
	
	private RecordType(String name, int order){
		this.name= name;
		this.order = order;
		this.command = name.toUpperCase(Locale.ENGLISH);
	}
	public String getName(){
		return name;
	}
	public int getOrder(){
		return order;
	}
	public String getCommand(){
		return command;
	}
	public static String[] getNameValues(){
		RecordType[] values = RecordType.values();
		String[] names = new String[values.length];
		for (int i = 0; i < names.length; ++i){
			names[values[i].order] = values[i].name;
		}
		return names;
	}
}
