package gui.wizard;

import javax.swing.JComboBox;

public class CharacterComboBox extends JComboBox<Character> {
	private static final long serialVersionUID = 1L;

	public CharacterComboBox(){
		super(new CharMapComboBoxModel());
		
		setEditable(false);
		setRenderer(new CharMapRenderer());
	}
	
	public void addItem(char c, String label){
		CharMapComboBoxModel model = (CharMapComboBoxModel) getModel();
		model.addElement(c, label);
	};
}
