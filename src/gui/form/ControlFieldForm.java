package gui.form;

import javax.swing.JPanel;
import javax.swing.JTextField;

import marc.field.ControlField;

public class ControlFieldForm extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextField textfield;
	private ControlField field;

	public ControlFieldForm(){
		super();
		
		field = null;
		layoutComponents();
	}
	private final void layoutComponents(){
		textfield = new JTextField(10);
		add(textfield);
	}	
	
	public ControlField getControlField(){
		return field;
	}
	public void setControlField(ControlField f){
		field = f;
	}
}
