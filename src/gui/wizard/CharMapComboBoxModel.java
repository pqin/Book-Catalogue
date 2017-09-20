package gui.wizard;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;

public class CharMapComboBoxModel extends DefaultComboBoxModel<Character> {
	private static final long serialVersionUID = 1L;
	
	private Map<Character, String> map;

	public CharMapComboBoxModel(){
		super();
		map = new HashMap<Character, String>();
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
}
