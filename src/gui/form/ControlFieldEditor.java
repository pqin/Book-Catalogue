package gui.form;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import marc.field.Field;

public class ControlFieldEditor extends AbstractFieldEditor {
	private JLabel tagField;
	private JTextField textField;

	public ControlFieldEditor(){
		super();
		
		layoutComponents();
	}
	private final void layoutComponents(){
		tagField = new JLabel();
		
		textField = new JTextField(10);
		textField.setMaximumSize(textField.getPreferredSize());
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(tagField);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(textField);
	}
	
	@Override
	public Field getField() {
		String tag = tagField.getText();
		String text = textField.getText();
		if (tag == null){
			field.setTag(Field.UNKNOWN_TAG);
		} else {
			field.setTag(tag);
		}
		if (text == null){
			field.setFieldData(new char[0]);
		} else {
			field.setFieldData(text.toCharArray());
		}
		return field;
	}
	@Override
	public void setField(Field f) {
		field = f;
		char[] data = field.getFieldData();
		tagField.setText(field.getTag());
		textField.setText(String.valueOf(data));
	}
	@Override
	public void clearForm(){
		tagField.setText(null);
		textField.setText(null);
	}
}
