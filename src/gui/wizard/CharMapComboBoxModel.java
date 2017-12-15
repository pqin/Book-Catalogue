package gui.wizard;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;

public class CharMapComboBoxModel extends DefaultComboBoxModel<Character> {
	private static final long serialVersionUID = 1L;
	
	private TreeMap<Character, String> map;

	public CharMapComboBoxModel(){
		super();
		map = new TreeMap<Character, String>();
	}
	
	@Override
	public void removeAllElements(){
		super.removeAllElements();
		map.clear();
	}
	public void addElement(char value, String label){
		if (map.containsKey(value)){
			final int index = getIndexOf(value);
			fireContentsChanged(this, index, index);
		} else {
			super.addElement(value);
		}
		map.put(value, label);
	}
	public boolean hasElement(char value){
		return map.containsKey(value);
	}
	
	public String getLabel(char value){
		return map.get(value);
	}
	public void setMap(final Map<Character, String> newMap){
		removeAllElements();
		map.putAll(newMap);
		Iterator<Character> it = map.keySet().iterator();
		while (it.hasNext()){
			addElement(it.next());
		}
		fireContentsChanged(this, 0, map.size() - 1);
	}
}
