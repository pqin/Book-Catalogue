package marc.type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigNode {
	private char code;
	private ConfigType data;
	private Map<Character, ConfigNode> child;
	
	public ConfigNode(){
		code = '\0';
		data = null;
		child = new HashMap<Character, ConfigNode>();
	}
	public ConfigNode(char code, ConfigType value){
		this.code = code;
		this.data = value;
		child = new HashMap<Character, ConfigNode>();
	}
	
	public ConfigType[] getConfig(char key){
		ConfigType[] result = null;
		if (code == key){
			result = new ConfigType[child.size()];
			Iterator<Character> it = child.keySet().iterator();
			ConfigNode node = null;
			int i = 0;
			while (it.hasNext()){
				node = child.get(it.next());
				result[i] = node.data;
				++i;
			}
		} else {
			result = new ConfigType[0];
		}
		return result;
	}
	public ConfigType getConfig(String key){
		return getConfig(key, 0);
	}
	private ConfigType getConfig(String key, int index){
		ConfigType result = null;
		if (index >= 0 && index < key.length()){
			if (index == key.length() - 1){
				result = data;
			} else {
				char c = key.charAt(index);
				ConfigNode node = child.get(c);
				if (node != null){
					result = node.getConfig(key, index+1);
				}
			}
		}
		return result;
	}
	public void setConfig(char key, ConfigType value){
		code = key;
		data = value;
	}
	public void setConfig(String key, ConfigType value){
		setConfig(key, 0, value);
	}
	private void setConfig(String key, int index, ConfigType value){
		if (index >= 0 && index < key.length()){
			char c = key.charAt(index);
			code = c;
			if (index == key.length() - 1){
				data = value;
			} else {
				c = key.charAt(index + 1);
				ConfigNode node = new ConfigNode();
				child.put(c, node);
				node.setConfig(key, index + 1, value);
			}
		}
	}
}
