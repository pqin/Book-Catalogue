package gui.wizard;

import javax.swing.JComboBox;

public final class CharacterComboBox extends JComboBox<Character> {
	private static final long serialVersionUID = 1L;

	public CharacterComboBox(){
		super(new CharMapComboBoxModel());
		
		setEditable(false);
		setRenderer(new CharMapRenderer());
	}
	
	public CharacterComboBox(CharMapComboBoxModel model) {
		super(model);
		
		setEditable(false);
		setRenderer(new CharMapRenderer());
	}
	
	@Override
	public void setSelectedItem(Object anObject){
		if (anObject == null){
			super.setSelectedItem(null);
		} else if (anObject instanceof String){
			String value = String.valueOf(anObject);
			if (value.length() == 1){
				super.setSelectedItem(value.charAt(0));
			} else {
				super.setSelectedItem(null);
			}
		} else {
			super.setSelectedItem(anObject);
		}
	}
}
