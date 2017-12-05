package marc.type;

import java.util.HashMap;
import java.util.Map;

public final class Format {
	private String name;
	private Map<ConfigIdentifier, ConfigNode> map;
	
	public Format(String name){
		this.name = name;
		this.map = new HashMap<ConfigIdentifier, ConfigNode>();
	}
	
	public String getName(){
		return name;
	}
	
	public ConfigType[] getConfiguration(String tag, int index, char key){
		ConfigType[] value = null;
		if (tag == null){
			value = new ConfigType[0];
		} else if (index >= 0){
			ConfigNode node = map.get(new ConfigIdentifier(tag, index, key));
			if (node == null){
				value = new ConfigType[0];
			} else {
				value = node.getConfig(key);
			}
		} else {
			value = new ConfigType[0];
		}
		return value;
	}
	public ConfigType getConfiguration(String tag, int index, String key){
		ConfigType value = null;
		if (tag != null && index >= 0){
			ConfigNode node = map.get(new ConfigIdentifier(tag, index, key.charAt(0)));
			if (node != null){
				value = node.getConfig(key);
			}
		}
		return value;
	}
	public void addConfiguration(String tag, int index, char key, ConfigType value){
		ConfigIdentifier nodeKey = new ConfigIdentifier(tag, index, key);
		if (!map.containsKey(nodeKey) && value != null){
			ConfigNode node = new ConfigNode();
			node.setConfig(key, value);
			map.put(nodeKey, node);
		}
	}
	public void addConfiguration(String tag, int index, String key, ConfigType value){
		char c = key.charAt(0);
		ConfigIdentifier nodeKey = new ConfigIdentifier(tag, index, c);
		ConfigNode node = map.containsKey(nodeKey) ? map.get(nodeKey) : new ConfigNode();
		node.setConfig(key, value);
		map.put(nodeKey, node);
	}
}
