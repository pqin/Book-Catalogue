package marc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;

import marc.resource.ResourceType;


public class MARC {
	private static final HashMap<Character, ResourceType> resourceTypeMap = buildResourceTypeMap();
	
	public static final char BLANK_CHAR = 0x20;
	public static final char FILL_CHAR = 0x7C;
	
	public static final int TAG_LENGTH = 3;
	public static final String UNKNOWN_TAG = "???";
	
	public static final String LEADER_TAG = "LDR";
	public static final int LEADER_FIELD_LENGTH = 24;
	
	public static final String RESOURCE_TAG = "008";
	public static final int RESOURCE_FIELD_LENGTH = 40;
	
	public static final ZoneId TIME_ZONE = ZoneId.systemDefault();
	public static final LocalDate EPOCH_START = LocalDate.of(1968, 1, 1);
	public static final Locale COUNTRY_LOCALE = Locale.US;
	public static final Locale LANGUAGE_LOCALE = Locale.ENGLISH;
	
	private static HashMap<Character, ResourceType> buildResourceTypeMap(){
		HashMap<Character, ResourceType> map = new HashMap<Character, ResourceType>();
		map.put('c', ResourceType.MUSIC);
		map.put('d', ResourceType.MUSIC);
		map.put('e', ResourceType.MAP);
		map.put('f', ResourceType.MAP);
		map.put('g', ResourceType.VISUAL_MATERIAL);
		map.put('i', ResourceType.MUSIC);
		map.put('j', ResourceType.MUSIC);
		map.put('k', ResourceType.VISUAL_MATERIAL);
		map.put('m', ResourceType.COMPUTER_FILE);
		map.put('o', ResourceType.VISUAL_MATERIAL);
		map.put('p', ResourceType.MIXED_MATERIAL);
		map.put('r', ResourceType.VISUAL_MATERIAL);
		map.put('t', ResourceType.BOOK);
		return map;
	}
	public static ResourceType getFormat(final char type, final char level){
		ResourceType format = ResourceType.UNKNOWN;
		String bkCode = "acdm";
		String crCode = "bis";
		if (type == 'a'){
			if (bkCode.indexOf(level) >= 0){
				format = ResourceType.BOOK;
			} else if (crCode.indexOf(level) >= 0){
				format = ResourceType.CONTINUING_RESOURCE;
			} else {
				format = ResourceType.UNKNOWN;
			}
		} else if (resourceTypeMap.containsKey(type)){
			format = resourceTypeMap.get(type);
		} else {
			format = ResourceType.UNKNOWN;
		}
		return format;
	}
}
