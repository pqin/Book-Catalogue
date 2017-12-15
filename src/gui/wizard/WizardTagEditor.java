package gui.wizard;

import java.awt.Dimension;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import marc.field.Field;
import marc.field.FieldType;

public class WizardTagEditor extends AbstractWizardPanel {
	private JTextComponent textField;
	private String previousID;
	private Map<FieldType, String> nextID;
	
	public WizardTagEditor(String ID){
		super(ID);
		
		layoutComponents();
		
		previousID = null;
		nextID = new EnumMap<FieldType, String>(FieldType.class);
		nextID.put(FieldType.UNKNOWN, ID);	// stay at this panel until valid tag is input
	}
	private void layoutComponents(){
		textField = new JTextField(10);
		textField.setMaximumSize(textField.getPreferredSize());
		textField.setText(Field.UNKNOWN_TAG);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(new JLabel("Tag:"));
		panel.add(Box.createHorizontalStrut(5));
		panel.add(textField);
		
		setComponent(panel);
	}
	
	@Override
	public JTextComponent getEditor(){
		return textField;
	}

	@Override
	public void setData(Object data) {
		String text = (data == null) ? null : data.toString();
		textField.setText(text);
	}
	@Override
	public Object getData() {
		return textField.getText();
	}

	@Override
	public void enterPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNextID() {
		FieldType type = Field.getFieldType(textField.getText());
		return nextID.get(type);
	}
	@Override
	public void setNextID(String ignore){}
	public void setNextID(FieldType type, String ID){
		if (type != null && type != FieldType.UNKNOWN){
			nextID.put(type, ID);
		}
	}

	@Override
	public String getPreviousID() {
		return previousID;
	}
	@Override
	public void setPreviousID(String ID){
		previousID = ID;
	}
}
