package gui.form;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;

public class CharacterTextField extends JFormattedTextField {
	private static final long serialVersionUID = 1L;

	public CharacterTextField(int columns){
		super();
		setColumns(columns);
		
		IndicatorFormatter formatter = new IndicatorFormatter();
		DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
		setFormatterFactory(factory);
	}
}
