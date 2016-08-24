package marc;

import java.nio.charset.Charset;
import java.util.HashMap;

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
	
	public static final int EPOCH_START = 1968;
	
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
	
	public static int parseValue(final byte[] bytes, final Charset charset, final int radix){
		int value = 0;
		String s = new String(bytes, charset);
		try {
			value = Integer.parseInt(s, radix);
		} catch (NumberFormatException e){
			value = 0;
		}
		return value;
	}
}
