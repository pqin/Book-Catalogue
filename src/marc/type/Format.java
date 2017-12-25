package marc.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import marc.field.Leader;

public final class Format {
	public static final String DEFAULT_REFERENCE_TAG = Leader.TAG;
	public static final int DEFAULT_REFERENCE_INDEX = Leader.TYPE;
	
	private String name;
	private Map<ConfigIdentifier, ConfigNode> map;
	
	public Format(String name){
		this.name = name;
		this.map = new HashMap<ConfigIdentifier, ConfigNode>();
	}
	
	public String getName(){
		return name;
	}
	
	public ConfigIdentifier[] getIdentifier(String tag){
		List<ConfigIdentifier> list = new ArrayList<ConfigIdentifier>();
		Iterator<ConfigIdentifier> it = map.keySet().iterator();
		ConfigIdentifier id = null;
		while (it.hasNext()){
			id = it.next();
			if (id.hasFieldTag(tag)){
				list.add(id);
			}
		}
		ConfigIdentifier[] output = new ConfigIdentifier[list.size()];
		output = list.toArray(output);
		Arrays.sort(output);
		return output;
	}
	public ConfigType[] getConfiguration(String tag, char key){
		ConfigType[] value = null;
		if (tag == null){
			value = new ConfigType[0];
		} else {
			ConfigNode node = map.get(new ConfigIdentifier(DEFAULT_REFERENCE_TAG, DEFAULT_REFERENCE_INDEX, key, tag));
			if (node == null){
				value = new ConfigType[0];
			} else {
				value = node.getConfig(key);
			}
		}
		return value;
	}
	public ConfigType getConfiguration(String tag, String key){
		ConfigType value = null;
		if (tag != null){
			ConfigNode node = map.get(new ConfigIdentifier(DEFAULT_REFERENCE_TAG, DEFAULT_REFERENCE_INDEX, key.charAt(0), tag));
			if (node != null){
				value = node.getConfig(key);
			}
		}
		return value;
	}
	public ConfigType[] getConfiguration(String reference, int index, String tag, char key){
		ConfigType[] value = null;
		if (tag == null){
			value = new ConfigType[0];
		} else {
			ConfigNode node = map.get(new ConfigIdentifier(reference, index, key, tag));
			if (node == null){
				value = new ConfigType[0];
			} else {
				value = node.getConfig(key);
			}
		}
		return value;
	}
	public ConfigType getConfiguration(String reference, int index, String tag, String key){
		ConfigType value = null;
		if (tag != null){
			ConfigNode node = map.get(new ConfigIdentifier(reference, index, key.charAt(0), tag));
			if (node != null){
				value = node.getConfig(key);
			}
		}
		return value;
	}
	
	public void addConfiguration(Format format){
		map.putAll(format.map);
	}
	public void addConfiguration(String tag, char key, ConfigType value){
		ConfigIdentifier nodeKey = new ConfigIdentifier(DEFAULT_REFERENCE_TAG, DEFAULT_REFERENCE_INDEX, key, tag);
		if (!map.containsKey(nodeKey) && value != null){
			ConfigNode node = new ConfigNode();
			node.setConfig(key, value);
			map.put(nodeKey, node);
		}
	}
	public void addConfiguration(String key, String tag, ConfigType value){
		ConfigIdentifier nodeKey = new ConfigIdentifier(DEFAULT_REFERENCE_TAG, DEFAULT_REFERENCE_INDEX, key.charAt(0), tag);
		ConfigNode node = map.containsKey(nodeKey) ? map.get(nodeKey) : new ConfigNode();
		node.setConfig(key, value);
		map.put(nodeKey, node);
	}
	public void addConfiguration(String reference, int index, String tag, char key, ConfigType value){
		ConfigIdentifier nodeKey = new ConfigIdentifier(reference, index, key, tag);
		if (!map.containsKey(nodeKey) && value != null){
			ConfigNode node = new ConfigNode();
			node.setConfig(key, value);
			map.put(nodeKey, node);
		}
	}
	public void addConfiguration(String reference, int index, String tag, String key, ConfigType value){
		ConfigIdentifier nodeKey = new ConfigIdentifier(reference, index, key.charAt(0), tag);
		ConfigNode node = map.containsKey(nodeKey) ? map.get(nodeKey) : new ConfigNode();
		node.setConfig(key, value);
		map.put(nodeKey, node);
	}
}
